package pt.shop.management.ui.details.invoice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.data.model.Note;

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
    private Invoice invoice;
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
    private TableView<Note> tableView;
    @FXML
    private TableColumn<Note, String> productIdCol;
    @FXML
    private TableColumn<Note, String> productNameCol;
    @FXML
    private TableColumn<Note, String> productPriceCol;
    @FXML
    private TableColumn<Note, String> productQuantityCol;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    public InvoiceDetailsController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
    }

    /**
     * Init invoice products table columns
     */
    private void initCol() {
        this.tableView.setPlaceholder(new Label("Nenhum produto adicionado"));
        this.productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
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
        this.invoice = invoice;
        this.id.setText("ID: " + invoice.getId());
        this.customer.setText("Cliente: " + invoice.getCustomerId());
        this.employee.setText("Empregado: " + invoice.getEmployeeId());
        this.date.setText("Data: " + invoice.getDate());
        this.getInvoiceProdutcs();
    }

    // TODO
    private void getInvoiceProdutcs() {
    }

    // TODO
    public void handleRefresh(ActionEvent event) {
    }

    // TODO
    public void handleProductEdit(ActionEvent event) {
    }

    // TODO
    public void handleProductDelete(ActionEvent event) {
    }
}
