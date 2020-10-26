package pt.shop.management.ui.details.supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.shop.management.data.model.Supplier;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Supplier Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class SupplierDetailsController implements Initializable {

    // Supplier data
    private final Supplier supplier;
    // UI Content
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label address;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label nif;

    public SupplierDetailsController(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.inflateUI(this.supplier);
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
     * @param supplier - supplier object
     */
    public void inflateUI(Supplier supplier) {
        id.setText("ID: " + supplier.getId());
        name.setText("Nome: " + supplier.getName());
        address.setText("Morada: " + supplier.getAddress());
        phone.setText("Contacto: " + supplier.getPhone());
        email.setText("E-mail: " + supplier.getEmail());
        nif.setText("NIF: " + supplier.getNif());
    }
}
