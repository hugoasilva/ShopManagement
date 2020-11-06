package pt.hugoasilva.shopmanagement.util;

import com.jcraft.jsch.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Shop Management Util Class
 *
 * @author Hugo Silva
 * @version 2020-11-06
 */

public class ShopManagementUtil {

    //Resources
    public static final String ICON_IMAGE_LOC = "/img/icon.png";
    private static final String OS = System.getProperty("os.name").toLowerCase();
    // Logger
    private static final Logger LOGGER = LogManager.getLogger(ShopManagementUtil.class.getName());
    // SFTP server details
    private static final String SFTP_SERVER_URL = "projecthub.hopto.org";
    private static final String SFTP_SERVER_USERNAME = "pi";
    private static final String SFTP_SERVER_PASSWORD = "server";
    private static final int SFTP_SERVER_PORT = 4400;
    // Path constant
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";
    // Date and time formats
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * Set stage icon
     *
     * @param stage window stage
     */
    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(ICON_IMAGE_LOC));
    }

    /**
     * Load FXML File
     *
     * @param loc         FXML file
     * @param title       window title
     * @param parentStage parent stage
     */
    public static Object loadWindow(URL loc, String title, Stage parentStage) {
        Object controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(loc);
            Parent parent = loader.load();
            controller = loader.getController();
            Stage stage = null;
            if (parentStage != null) {
                stage = parentStage;
            } else {
                stage = new Stage(StageStyle.DECORATED);
            }
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            setStageIcon(stage);
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
        return controller;
    }

    public static String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public static String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }

    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Open file with desktop app
     *
     * @param path file path
     */
    public static void openFile(String path) {
        File file = new File(path);
        try {
            if (OS.contains("Win")) {
                // Windows
                Desktop.getDesktop().open(file);
            } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
                // Ubuntu
                Runtime.getRuntime().exec(new String[]{"evince", file.getAbsolutePath()});
            } else {
                LOGGER.log(Level.INFO, "OS not supported!");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Create local downloads and uploads folder
     */
    public static void createDownloadsUploadsFolder() {
        try {
            Path path = Paths.get("downloads/");
            // Check if folder already exists and delete it
            if (Files.exists(path)) {
                FileUtils.forceDelete(new File("downloads/"));
            }
            // Create downloads folder
            Files.createDirectories(path);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Unable to create downloads folder");
        }
        try {
            Path path = Paths.get("uploads/");
            // Check if folder already exists and delete it
            if (Files.exists(path)) {
                FileUtils.forceDelete(new File("uploads/"));
            }
            // Create uploads folder
            Files.createDirectories(path);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Unable to create uploads folder");
        }
    }

    /**
     * Setup SFTP server connection
     *
     * @return SFTPChannel object
     */
    private static ChannelSftp setupSFTP() {
        ChannelSftp channel = null;
        try {
            JSch jsch = new JSch();
            JSch.setConfig("StrictHostKeyChecking", "no");
            Session jschSession = jsch.getSession(SFTP_SERVER_USERNAME, SFTP_SERVER_URL);
            jschSession.setPassword(SFTP_SERVER_PASSWORD);
            jschSession.setPort(SFTP_SERVER_PORT);
            jschSession.connect();
            channel = (ChannelSftp) jschSession.openChannel("sftp");
        } catch (JSchException e) {
            printJSchException(e);
        }
        return channel;
    }

    /**
     * Log JSch exception
     *
     * @param e JSch exception
     */
    private static void printJSchException(JSchException e) {
        if (e != null) {
            LOGGER.log(Level.ERROR, "{}", "JSch Error: " + e.getMessage());
            Throwable t = e.getCause();
            while (t != null) {
                LOGGER.log(Level.ERROR, "{}", "Cause: " + t);
                t = t.getCause();
            }
        }
    }

    /**
     * Log SFTP exception
     *
     * @param e SFTP exception
     */
    private static void printSFTPException(SftpException e) {
        if (e != null) {
            LOGGER.log(Level.ERROR, "{}", "SFTP Error: " + e.getMessage());
            Throwable t = e.getCause();
            while (t != null) {
                LOGGER.log(Level.ERROR, "{}", "Cause: " + t);
                t = t.getCause();
            }
        }
    }

    /**
     * Download file from SFTP Server
     *
     * @param path     remote file path
     * @param fileName local file name
     */
    public static void downloadFile(String path, String fileName) {
        try {
            ChannelSftp channelSftp = setupSFTP();
            channelSftp.connect();

            // Download file and close connection
            channelSftp.get(path, LOCAL_DOWNLOAD_PATH + fileName);
            channelSftp.exit();
        } catch (SftpException e) {
            printSFTPException(e);
        } catch (JSchException e) {
            printJSchException(e);
        }
    }

    /**
     * Upload file to SFTP Server
     *
     * @param localPath  local file path
     * @param remotePath remote file path
     */
    public static void uploadFile(String localPath, String remotePath) {
        try {
            ChannelSftp channelSftp = setupSFTP();
            channelSftp.connect();

            // Upload file and close connection
            channelSftp.put(localPath, remotePath);
            channelSftp.exit();
        } catch (SftpException e) {
            printSFTPException(e);
        } catch (JSchException e) {
            printJSchException(e);
        }
    }

    /**
     * Convert image to png and upload it
     */
    public static void uploadImage(String localPath, String remotePath) {
        try {
            // Read image
            BufferedImage bufferedImage = ImageIO.read(new File(localPath));
            // Save image
            File localImage = new File(localPath);
            ImageIO.write(bufferedImage, "png", localImage);
            uploadFile(localImage.getAbsolutePath(), remotePath);
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }
}
