package pt.shop.management.ui.details.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Employee;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Employee Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class EmployeeDetailsController implements Initializable {

    // Database query
    private static final String SELECT_EMPLOYEE_QUERY = "SELECT * FROM empregados WHERE id_empregado=?";
    private final String employeeID;
    // Database handler instance
    DatabaseHandler databaseHandler;
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
    @FXML
    private Label notes;
    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, String> idCol;
    @FXML
    private TableColumn<Customer, String> descriptionCol;
    @FXML
    private TableColumn<Customer, String> dateCol;

    public EmployeeDetailsController(String id) throws SQLException {
        this.employeeID = id;
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
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    /**
     * Populate table
     *
     * @param employee - employee object
     */
    public void inflateUI(Employee employee) {
        id.setText("ID: " + employee.getId());
        name.setText("Nome: " + employee.getName());
        address.setText("Morada: " + employee.getAddress());
        phone.setText("Contacto: " + employee.getPhone());
        email.setText("E-mail: " + employee.getEmail());
        nif.setText("NIF: " + employee.getNif());
        notes.setText("Notas:");
    }

    /**
     * Load customer details data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_EMPLOYEE_QUERY);
        preparedStatement.setString(1, employeeID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String id = resultSet.getString("id_empregado");
        String name = resultSet.getString("nome");
        String address = resultSet.getString("morada");
        String phone = resultSet.getString("contacto");
        String email = resultSet.getString("email");
        String nif = resultSet.getString("nif");
        String notes = resultSet.getString("notas");
        this.inflateUI(new Employee(id, name, address, phone, email, nif, notes));
    }
}
