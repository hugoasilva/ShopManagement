package pt.shop.management.ui.add.product;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Product;
import pt.shop.management.ui.alert.AlertMaker;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Product Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class ProductAddController implements Initializable {

    // Database handler instance
    DatabaseHandler databaseHandler;
    // UI Content
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXTextField quantity;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;
    private String id;
    private Boolean isInEditMode = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
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
     * @throws SQLException - database exception
     */
    @FXML
    private void addProduct(ActionEvent event) throws SQLException {

        String productId = String.valueOf(DatabaseHandler.getProductId());
        this.id = productId;
        String productName = name.getText();
        String productPrice = price.getText();
        String productQuantity = quantity.getText();

        if (productName.isEmpty() || productQuantity.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.");
            return;
        }

        if (isInEditMode) {
            handleUpdateProduct();
            return;
        }

        Product product = new Product(productId, productName, productPrice, productQuantity);
        if (DatabaseHandler.insertProduct(product)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Produto adicionado", productName + " adicionado com sucesso!");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Ocorreu um erro", "Verifique os dados e tente novamente.");
        }
    }

    /**
     * Populate table
     *
     * @param product - product object
     */
    public void inflateUI(Product product) {
        name.setText(product.getName());
        quantity.setText(product.getQuantity());

        isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear table entries
     */
    private void clearEntries() {
        name.clear();
        quantity.clear();
    }

    /**
     * Handle product update
     */
    private void handleUpdateProduct() {
        Product product = new Product(id, name.getText(), price.getText(), quantity.getText());
        if (DatabaseHandler.getInstance().updateProduct(product)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Successo!",
                    "Dados de produto atualizados.");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(), StandardCharsets.UTF_8));
        }
    }
}
