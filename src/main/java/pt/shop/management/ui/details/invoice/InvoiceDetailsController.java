package pt.shop.management.ui.details.invoice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Invoice;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Invoice Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-24
 */

public class InvoiceDetailsController implements Initializable {

    // Database query
    private static final String SELECT_CUSTOMER_QUERY = "SELECT * FROM faturas WHERE id=?";
    // Database handler instance
    DatabaseHandler databaseHandler;
    // Invoice data
    private final String invoiceID;
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

    public InvoiceDetailsController(String id) {
        this.invoiceID = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        try {
            loadData();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
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
        id.setText("ID: " + invoice.getId());
        customer.setText("Cliente: " + invoice.getCustomerId());
        employee.setText("Empregado: " + invoice.getEmployeeId());
        date.setText("Data: " + invoice.getDate());
        products.setText("Produtos: " + invoice.getProducts());
    }

    /**
     * Load customer details data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_CUSTOMER_QUERY);
        preparedStatement.setString(1, this.invoiceID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String id = resultSet.getString("id");
        String customerId = resultSet.getString("id_cliente");
        String employeeId = resultSet.getString("id_empregado");
        String date = resultSet.getString("data_fatura");
        String products = resultSet.getString("produtos");
        String pdf = resultSet.getString("pdf");
        this.inflateUI(new Invoice(id, customerId, employeeId, date, products, pdf));
    }
}
