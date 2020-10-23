package pt.shop.management.data.files;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SFTP Server Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class SFTPHandler {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(SFTPHandler.class.getName());

    // SFTP server details
    private static final String SFTP_SERVER_URL = "projecthub.hopto.org";
    private static final String SFTP_SERVER_USERNAME = "pi";
    private static final String SFTP_SERVER_PASSWORD = "server";

    // Path constant
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";

    /**
     * Setup SFTP server connection
     *
     * @return - SFTPChannel object
     * @throws JSchException - JSch exception
     */
    private static ChannelSftp setupSFTP() throws JSchException {
        JSch jsch = new JSch();
        JSch.setConfig("StrictHostKeyChecking", "no");
        Session jschSession = jsch.getSession(SFTP_SERVER_USERNAME, SFTP_SERVER_URL);
        jschSession.setPassword(SFTP_SERVER_PASSWORD);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    /**
     * Log JSch exception
     *
     * @param e - JSch exception
     */
    private static void printJSchException(JSchException e) {
        if (e != null) {
            e.printStackTrace(System.err);
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
     * @param e - SFTP exception
     */
    private static void printSFTPException(SftpException e) {
        if (e != null) {
            e.printStackTrace(System.err);
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
     * @param path     - remote file path
     * @param fileName - local file name
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
     * @param localPath  - local file path
     * @param remotePath - remote file path
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
}
