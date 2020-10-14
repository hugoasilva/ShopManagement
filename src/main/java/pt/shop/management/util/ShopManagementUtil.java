package main.java.pt.shop.management.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.pt.shop.management.ui.main.MainController;

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
import java.util.regex.Pattern;

/**
 * Shop Management Util Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class ShopManagementUtil {

    //Resources
    public static final String ICON_IMAGE_LOC = "/img/ballot.png";

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
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
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
     * Check if email address is valid
     *
     * @param emailID - email address
     * @return - true if valid, false otherwise
     */
    public static boolean validateEmailAddress(String emailID) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(emailID).matches();
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
     * Create downloads folder
     */
    public static void createDownloadsFolder() {
        try {
            Path path = Paths.get("downloads/");
            Files.createDirectories(path);
        } catch (IOException e) {
            Logger.getLogger(ShopManagementUtil.class.getName()).log(Level.INFO,
                    "Não foi possível criar a pasta de transferências");
        }

    }
}
