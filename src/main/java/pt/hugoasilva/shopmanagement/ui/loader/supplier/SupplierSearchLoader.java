package pt.hugoasilva.shopmanagement.ui.loader.supplier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Supplier Search Loader Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class SupplierSearchLoader extends Application {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(SupplierSearchLoader.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/supplier/SupplierSearch.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

}
