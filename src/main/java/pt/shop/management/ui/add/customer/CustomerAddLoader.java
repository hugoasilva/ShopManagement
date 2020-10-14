package pt.shop.management.ui.add.customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Customer Add Loader Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */
public class CustomerAddLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/customer/CustomerAdd.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}
