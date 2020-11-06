package pt.hugoasilva.shopmanagement.ui.controller.product;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.hugoasilva.shopmanagement.data.database.DatabaseHandler;
import pt.hugoasilva.shopmanagement.ui.dialog.DialogHandler;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Product Invoice Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-06
 */

public class ProductInvoiceAddController implements Initializable {

    // Product data
    private String invoiceId;
    private String invoiceProductId;
    private Boolean isInEditMode = false;
    // UI Content
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField quantity;
    @FXML
    private StackPane rootPane;
    @FXML
    private StackPane mainContainer;

    public ProductInvoiceAddController() {
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
        Stage stage = (Stage) id.getScene().getWindow();
        stage.close();
    }

    /**
     * Close parent window
     */
    @FXML
    private void closeParent() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Set invoice id
     *
     * @param id invoice id
     */
    public void setInvoiceId(String id) {
        this.invoiceId = id;
    }

    /**
     * Add product
     *
     * @param event add note event
     */
    @FXML
    private void addProduct(ActionEvent event) {

        String productId = this.id.getText();
        String quantity = this.quantity.getText();

        if (productId.isEmpty() || quantity.isEmpty()) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer,
                    new String("Por favor insira dados em todos os campos.".getBytes(),
                            StandardCharsets.UTF_8));
            return;
        }

        if (this.isInEditMode) {
            this.handleUpdateProduct();
            return;
        }
        if (DatabaseHandler.insertProductInvoice(invoiceId, productId, quantity)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Produto adicionado",
                    "Produto adicionado com sucesso!", true);
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
        }
    }

    /**
     * Populate form
     *
     * @param id       product id
     * @param quantity product quantity
     */
    public void inflateUI(String invoiceProductId, String id, String quantity) {
        this.id.setText(id);
        this.quantity.setText(quantity);
        this.invoiceProductId = invoiceProductId;

        this.isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear entries
     */
    private void clearEntries() {
        this.id.clear();
        this.quantity.clear();
    }

    /**
     * Handle product update
     */
    private void handleUpdateProduct() {

        String productId = this.id.getText();
        String quantity = this.quantity.getText();

        // Check if note is empty
        if (productId.isEmpty() || quantity.isEmpty()) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Dados insuficientes",
                    new String("Por favor insira dados em todos os campos.".getBytes(),
                            StandardCharsets.UTF_8), false);
            return;
        }
        if (DatabaseHandler.updateProductInvoice(this.invoiceProductId, productId, quantity)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo",
                    "Produto editado com sucesso!", true);
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                    "Não foi possível atualizar o produto.", false);

        }
    }
}
