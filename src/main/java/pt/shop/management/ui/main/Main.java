package pt.shop.management.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.exceptions.ExceptionUtil;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Main Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class Main extends Application {

    // Logger
    private final static Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * Main app app main function
     *
     * @param args - cmd arguments
     */
    public static void main(String[] args) {
        ShopManagementUtil.createDownloadsUploadsFolder();
        Long startTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Sistema de Gestão de Loja iniciado às {}",
                ShopManagementUtil.formatDateTimeString(startTime));
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Long exitTime = System.currentTimeMillis();
                LOGGER.log(Level.INFO, "Sistema de Gestão de Loja fechado às {}. Usado por {} ms",
                        ShopManagementUtil.formatDateTimeString(startTime), exitTime);
            }
        });
    }

    /**
     * Show login window
     *
     * @param stage - app's stage
     * @ - exception
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
