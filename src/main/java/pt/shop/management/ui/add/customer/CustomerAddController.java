package pt.shop.management.ui.add.customer;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.ui.alert.AlertMaker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Customer Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-16
 */

public class CustomerAddController implements Initializable {

    // Directory paths
    private final static String LOCAL_CUSTOMER_PATH = "uploads/";
    private final static String REMOTE_CUSTOMER_PATH = "/home/pi/gestao/clientes/";

    // Customer data
    private String id;
    private String notesPath;
    private Boolean isInEditMode = false;

    // Database handler instance
    DatabaseHandler databaseHandler;

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
     * Add customer to table
     *
     * @param event - add customer event
     * @throws SQLException - database exception
     */
    @FXML
    private void addCustomer(ActionEvent event) throws SQLException {

        String customerId = String.valueOf(DatabaseHandler.getCustomerId());
        this.id = customerId;
        String customerName = name.getText();
        String customerAddress = address.getText();
        String customerPhone = phone.getText();
        String customerEmail = email.getText();
        String customerNif = nif.getText();
        String customerNotes = REMOTE_CUSTOMER_PATH + this.id + ".json";
        this.notesPath = customerNotes;

        if (customerName.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty()
                || customerEmail.isEmpty() || customerNif.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.");
            return;
        }

        if (isInEditMode) {
            handleUpdateCustomer();
            return;
        }

        Customer customer = new Customer(customerId, customerName,
                customerAddress, customerPhone, customerEmail, customerNif, customerNotes);
        if (DatabaseHandler.insertCustomer(customer)) {
            this.createNotesJSON();
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Cliente adicionado", customerName + " adicionado com sucesso!");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Ocorreu um erro", "Verifique os dados e tente novamente.");
        }
    }

    /**
     * Populate table
     *
     * @param customer - customer object
     */
    public void inflateUI(Customer customer) {
        name.setText(customer.getName());
        address.setText(customer.getAddress());
        phone.setText(customer.getPhone());
        email.setText(customer.getEmail());
        nif.setText(customer.getNif());

        isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear table entries
     */
    private void clearEntries() {
        name.clear();
        address.clear();
        phone.clear();
        email.clear();
        nif.clear();
    }

    /**
     * Handle customer update
     */
    private void handleUpdateCustomer() {
        Customer customer = new Customer(id, name.getText(), address.getText(),
                phone.getText(), email.getText(), nif.getText(), this.notesPath);
        if (DatabaseHandler.getInstance().updateCustomer(customer)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Successo!",
                    "Dados de cliente atualizados.");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(), StandardCharsets.UTF_8));
        }
    }

    /**
     * Create empty notes JSON file
     */
    private void createNotesJSON() {
        try {
            File file = new File(LOCAL_CUSTOMER_PATH + this.id + ".json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("[]");
            fileWriter.close();
            SFTPHandler.uploadFile(file.getPath(), this.notesPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
