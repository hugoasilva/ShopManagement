package pt.shop.management.ui.controller.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.model.Product;
import pt.shop.management.util.ShopManagementUtil;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Product Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class ProductDetailsController implements Initializable {

    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";

    // Product object
    private Product product;
    // UI Content
    @FXML
    private ImageView imageView;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label supplier;
    @FXML
    private Label price;
    @FXML
    private Label quantity;
    @FXML
    private StackPane mainContainer;

    public ProductDetailsController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Cancel button handler
     *
     * @param event cancel event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    /**
     * Populate interface
     *
     * @param product product object
     */
    public void inflateUI(Product product) {
        this.product = product;
        this.id.setText("Produto nr: " + product.getId());
        this.name.setText("Nome: " + product.getName());
        this.price.setText(new String("Preço: ".getBytes(), StandardCharsets.UTF_8) + product.getPrice());
        this.supplier.setText("Fornecedor: " + product.getSupplierName());
        this.quantity.setText("Quantidade: " + product.getQuantity());
        this.setImage();
    }

    /**
     * Retrieve image and set it to imageView
     */
    private void setImage() {
        // Download image
        String fileName = id + ".png";
        ShopManagementUtil.downloadFile(this.product.getImage(), fileName);

        // Set image
        File file = new File(LOCAL_DOWNLOAD_PATH + fileName);
        Image image = new Image(file.toURI().toString());
        this.imageView.setImage(image);
    }
}
