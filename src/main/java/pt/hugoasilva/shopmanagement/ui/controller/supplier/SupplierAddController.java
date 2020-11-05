package pt.hugoasilva.shopmanagement.ui.controller.supplier;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pt.hugoasilva.shopmanagement.data.database.DatabaseHandler;
import pt.hugoasilva.shopmanagement.data.model.Supplier;
import pt.hugoasilva.shopmanagement.ui.dialog.DialogHandler;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Supplier Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class SupplierAddController implements Initializable {

    // Supplier data
    private Supplier supplier;
    private Boolean isInEditMode = Boolean.FALSE;
    ;
    // UI Content
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField address;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField nif;
    @FXML
    private AnchorPane mainContainer;

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
     * Add supplier to table
     *
     * @param event add supplier event
     */
    @FXML
    private void addSupplier(ActionEvent event) {
        String supplierId = String.valueOf(DatabaseHandler.getSupplierId());
        String supplierName = name.getText();
        String supplierAddress = address.getText();
        String supplierPhone = phone.getText();
        String supplierEmail = email.getText();
        String supplierNif = nif.getText();

        if (supplierName.isEmpty() || supplierAddress.isEmpty() || supplierPhone.isEmpty()
                || supplierEmail.isEmpty() || supplierNif.isEmpty()) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (this.isInEditMode) {
            this.handleUpdateSupplier();
            return;
        }

        Supplier supplier = new Supplier(supplierId, supplierName,
                supplierAddress, supplierPhone, supplierEmail, supplierNif);
        if (DatabaseHandler.insertSupplier(supplier)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Fornecedor adicionado",
                    supplierName + " adicionado com sucesso!", false);
            clearEntries();
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
        }
    }

    /**
     * Populate table
     *
     * @param supplier supplier object
     */
    public void inflateUI(Supplier supplier) {
        this.name.setText(supplier.getName());
        this.address.setText(supplier.getAddress());
        this.phone.setText(supplier.getPhone());
        this.email.setText(supplier.getEmail());
        this.nif.setText(supplier.getNif());
        this.supplier = supplier;

        this.isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear table entries
     */
    private void clearEntries() {
        this.name.clear();
        this.address.clear();
        this.phone.clear();
        this.email.clear();
        this.nif.clear();
    }

    /**
     * Handle supplier update
     */
    private void handleUpdateSupplier() {
        Supplier supplier =
                new Supplier(this.supplier.getId(), this.name.getText(), this.address.getText(),
                        this.phone.getText(), this.email.getText(), this.nif.getText());
        if (DatabaseHandler.updateSupplier(supplier)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo!",
                    "Dados de fornecedor atualizados.", false);
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(),
                            StandardCharsets.UTF_8), false);
        }
    }
}
