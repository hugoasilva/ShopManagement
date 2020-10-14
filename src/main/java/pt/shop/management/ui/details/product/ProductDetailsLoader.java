package pt.shop.management.ui.details.product;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Product Details Loader Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */
public class ProductDetailsLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/product/ProductDetails.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}