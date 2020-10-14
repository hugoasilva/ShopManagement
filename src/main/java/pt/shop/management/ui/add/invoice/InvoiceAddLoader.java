package pt.shop.management.ui.add.invoice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Invoice Add Loader Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class InvoiceAddLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/invoice/InvoiceAdd.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}
