package pt.shop.management.ui.add.customer;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.JSONHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.alert.AlertMaker;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Customer Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class CustomerAddController implements Initializable {

    // Directory paths
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";
    private final static String REMOTE_CUSTOMER_PATH = "/home/pi/gestao/clientes/";
    // Database handler instance
    DatabaseHandler databaseHandler;
    // Customer data
    private String id;
    private String notesPath;
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
     */
    @FXML
    private void addCustomer(ActionEvent event) {

        String customerId = String.valueOf(DatabaseHandler.getCustomerId());
        this.id = customerId;
        String customerName = name.getText();
        String customerAddress = address.getText();
        String customerPhone = phone.getText();
        String customerEmail = email.getText();
        String customerNif = nif.getText();

        if (customerName.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty()
                || customerEmail.isEmpty() || customerNif.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (isInEditMode) {
            handleUpdateCustomer();
            return;
        }

        Customer customer = new Customer(customerId, customerName,
                customerAddress, customerPhone, customerEmail, customerNif);
        if (DatabaseHandler.insertCustomer(customer)) {
            this.createNotesJSON();
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Cliente adicionado",
                    customerName + " adicionado com sucesso!", false);
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
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
                phone.getText(), email.getText(), nif.getText());
        if (DatabaseHandler.getInstance().updateCustomer(customer)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Successo!",
                    "Dados de cliente atualizados.", false);
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(),
                            StandardCharsets.UTF_8), false);
        }
    }

    /**
     * Create empty notes JSON file
     */
    private void createNotesJSON() {
        // Create notes list
        List<Note> notes = new ArrayList<>();
        // Add null message to notes list
        notes.add(new Note("0", "Message zero"));
        String path = LOCAL_DOWNLOAD_PATH + this.id + ".json";
        // Convert notes list to JSON
        JSONHandler.notesToJSON(notes, path);
        // Upload file to server
        File file = new File(path);
        SFTPHandler.uploadFile(file.getPath(), this.notesPath);
    }
}