package pt.shop.management.ui.details.invoice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.shop.management.data.model.Invoice;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Invoice Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class InvoiceDetailsController implements Initializable {

    // Invoice data
    private final Invoice invoice;
    // UI Content
    @FXML
    private Label id;
    @FXML
    private Label customer;
    @FXML
    private Label employee;
    @FXML
    private Label date;
    @FXML
    private Label products;
    @FXML
    private Label pdf;

    public InvoiceDetailsController(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.inflateUI(this.invoice);
    }

    /**
     * Cancel button handler
     *
     * @param event - cancel event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) customer.getScene().getWindow();
        stage.close();
    }

    /**
     * Populate table
     *
     * @param invoice - invoice object
     */
    public void inflateUI(Invoice invoice) {
        this.id.setText("ID: " + invoice.getId());
        this.customer.setText("Cliente: " + invoice.getCustomerId());
        this.employee.setText("Empregado: " + invoice.getEmployeeId());
        this.date.setText("Data: " + invoice.getDate());
        this.products.setText("Produtos: " + invoice.getProducts());
    }
}
