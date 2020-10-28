package pt.shop.management.ui.add.customer;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.ui.material.DialogHandler;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Customer Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class CustomerAddController implements Initializable {

    // Customer data
    private Customer customer;
    private Boolean isInEditMode = false;
    // UI content
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
     * Add customer to table
     *
     * @param event - add customer event
     */
    @FXML
    private void addCustomer(ActionEvent event) {
        String customerId = String.valueOf(DatabaseHandler.getCustomerId());
        String customerName = name.getText();
        String customerAddress = address.getText();
        String customerPhone = phone.getText();
        String customerEmail = email.getText();
        String customerNif = nif.getText();

        if (customerName.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty()
                || customerEmail.isEmpty() || customerNif.isEmpty()) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (this.isInEditMode) {
            this.handleUpdateCustomer();
            return;
        }

        Customer customer = new Customer(customerId, customerName,
                customerAddress, customerPhone, customerEmail, customerNif);
        if (DatabaseHandler.insertCustomer(customer)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Cliente adicionado",
                    customerName + " adicionado com sucesso!", false);
            clearEntries();
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
        }
    }

    /**
     * Populate table
     *
     * @param customer - customer object
     */
    public void inflateUI(Customer customer) {
        this.name.setText(customer.getName());
        this.address.setText(customer.getAddress());
        this.phone.setText(customer.getPhone());
        this.email.setText(customer.getEmail());
        this.nif.setText(customer.getNif());
        this.customer = customer;

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
     * Handle customer update
     */
    private void handleUpdateCustomer() {
        Customer customer = new Customer(this.customer.getId(), this.name.getText(), this.address.getText(),
                this.phone.getText(), this.email.getText(), this.nif.getText());
        if (DatabaseHandler.updateCustomer(customer)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo!",
                    "Dados de cliente atualizados.", true);
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(),
                            StandardCharsets.UTF_8), false);
        }
    }
}