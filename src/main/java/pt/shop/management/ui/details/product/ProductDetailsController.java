package pt.shop.management.ui.details.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.shop.management.data.model.Product;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Product Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class ProductDetailsController implements Initializable {

    // Product data
    private final Product product;
    // UI Content
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label price;
    @FXML
    private Label quantity;

    public ProductDetailsController(Product product) {
        this.product = product;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.inflateUI(this.product);
    }

    /**
     * Cancel button handler
     *
     * @param event - cancel event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    /**
     * Populate interface
     *
     * @param product - product object
     */
    public void inflateUI(Product product) {
        this.id.setText("ID: " + product.getId());
        this.name.setText("Nome: " + product.getName());
        this.price.setText("Preço: " + product.getPrice());
        this.quantity.setText("Quantidade: " + product.getQuantity());
    }
}
