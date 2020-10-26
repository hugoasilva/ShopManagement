package pt.shop.management.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shop Management Util Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class ShopManagementUtil {

    //Resources
    public static final String ICON_IMAGE_LOC = "/img/icon.png";

    // Date and time formats
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(ICON_IMAGE_LOC));
    }

    /**
     * Load FXML File
     *
     * @param loc         - FXML file
     * @param title       - window title
     * @param parentStage - parent stage
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
            Logger.getLogger(ShopManagementUtil.class.getName()).log(Level.SEVERE, null, ex);
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
     * @param path - file path
     */
    public static void openFile(String path) {
        File file = new File(path);
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(ShopManagementUtil.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ShopManagementUtil.class.getName()).log(Level.INFO,
                    "Não foi possível criar a pasta de transferências");
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
            Logger.getLogger(ShopManagementUtil.class.getName()).log(Level.INFO,
                    "Não foi possível criar a pasta de carregamentos");
        }
    }
}
