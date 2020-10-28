package pt.shop.management.ui.add.product;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Product;
import pt.shop.management.ui.dialog.DialogHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Product Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class ProductAddController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(ProductAddController.class.getName());
    // Directory paths
    private final static String LOCAL_UPLOAD_PATH = "uploads/";
    private final static String REMOTE_PRODUCT_PATH = "/home/pi/management/products/";
    // Product data
    private String id;
    private String imagePath;
    private Boolean isInEditMode = Boolean.FALSE;
    ;
    // UI Content
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXTextField supplier;
    @FXML
    private JFXTextField quantity;
    @FXML
    private JFXButton image;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
     * Add product to table
     *
     * @param event - add product event
     */
    @FXML
    private void addProduct(ActionEvent event) throws IOException, SQLException {
        String productId = String.valueOf(DatabaseHandler.getProductId());
        this.id = productId;
        String productName = name.getText();
        String productPrice = price.getText();
        String productSupplierId = supplier.getText();
        String productQuantity = quantity.getText();
        String productImage = REMOTE_PRODUCT_PATH + this.id + ".png";

        if (productName.isEmpty() || productPrice.isEmpty() || productQuantity.isEmpty()) {
            DialogHandler.showMaterialDialog(this.rootPane, this.mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (this.isInEditMode) {
            this.handleUpdateProduct();
            return;
        }

        Product product = new Product(productId, productName,
                productPrice, productSupplierId, productQuantity, productImage);
        if (DatabaseHandler.insertProduct(product)) {
            String path = LOCAL_UPLOAD_PATH + this.id + ".png";
            this.imageToPNG(path);
            SFTPHandler.uploadFile(path, productImage);
            DialogHandler.showMaterialDialog(this.rootPane, this.mainContainer,
                    new ArrayList<>(), "Produto adicionado",
                    productName + " adicionado com sucesso!", true);
            clearEntries();
        } else {
            DialogHandler.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
        }
    }

    /**
     * Read chosen image and save it as PNG
     *
     * @throws IOException - IO Exception
     */
    private void imageToPNG(String image) throws IOException {
        // Read image
        BufferedImage bufferedImage = ImageIO.read(new File(this.imagePath));
        // Save image
        File localImage = new File(image);
        ImageIO.write(bufferedImage, "png", localImage);
    }

    /**
     * Populate table
     *
     * @param product - product object
     */
    public void inflateUI(Product product) {
        this.name.setText(product.getName());
        this.price.setText(product.getPrice());
        this.supplier.setText(product.getSupplierId());
        this.quantity.setText(product.getQuantity());

        this.isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear table entries
     */
    private void clearEntries() {
        this.name.clear();
        this.price.clear();
        this.supplier.clear();
        this.quantity.clear();
    }

    /**
     * Handle product update
     */
    private void handleUpdateProduct() throws SQLException {
        Product product = new Product(this.id, this.name.getText(), this.price.getText(),
                this.supplier.getText(), this.quantity.getText(), this.image.getText());
        if (DatabaseHandler.updateProduct(product)) {
            DialogHandler.showMaterialDialog(this.rootPane, this.mainContainer,
                    new ArrayList<>(), "Successo!",
                    "Dados de produto atualizados.", false);
        } else {
            DialogHandler.showMaterialDialog(this.rootPane, this.mainContainer,
                    new ArrayList<>(), "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(),
                            StandardCharsets.UTF_8), false);
        }
    }

    public void chooseImage(ActionEvent actionEvent) {
        // Open File Chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Imagem");
        fileChooser.getExtensionFilters().addAll(new
                FileChooser.ExtensionFilter(
                "Imagem do produto", "*.jpg", "*.jpeg", "*.png"));
        Stage stage = new Stage(StageStyle.DECORATED);

        // Store File Path
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            LOGGER.log(Level.INFO, "No image file was selected.");
        } else {
            this.imagePath = file.getAbsolutePath();
        }
    }
}
