package pt.hugoasilva.shopmanagement.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.hugoasilva.shopmanagement.util.ShopManagementUtil;
import pt.hugoasilva.shopmanagement.util.exception.ExceptionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Main Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class Main extends Application {

    // Logger
    private final static Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * Main app app main function
     *
     * @param args cmd arguments
     */
    public static void main(String[] args) {
        ShopManagementUtil.createDownloadsUploadsFolder();
        Long startTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Management System started at {}",
                ShopManagementUtil.formatDateTimeString(startTime));
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Long exitTime = System.currentTimeMillis();
            LOGGER.log(Level.INFO, "Management System closed at {}. Used for {} ms",
                    ShopManagementUtil.formatDateTimeString(startTime), exitTime);
        }));
    }

    /**
     * Show login window
     *
     * @param stage app's stage
     */
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle(new String("Sistema de Gestão de Loja".getBytes(), StandardCharsets.UTF_8));
            ShopManagementUtil.setStageIcon(stage);
            new Thread(ExceptionUtil::init).start();
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }
}
