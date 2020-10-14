package pt.shop.management.ui.details.invoice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Customer;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Invoice Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class InvoiceDetailsController implements Initializable {

    // Database query
    private static final String SELECT_CUSTOMER_QUERY = "SELECT * FROM clientes WHERE id_cliente=?";
    // Database handler instance
    DatabaseHandler databaseHandler;
    private String customerID;
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

    public InvoiceDetailsController(String id) throws SQLException {
        this.customerID = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        try {
            loadData();
//            initLabels();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

//    private void initLabels() {
//        this.id.setText("ID: ");
//        this.name.setText("Nome: ");
//        this.address.setText("Morada: ");
//        this.phone.setText("Contacto: ");
//        this.email.setText("Email: ");
//
//    }

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
     * @param customer - customer object
     */
    public void inflateUI(Customer customer) {
        id.setText("ID: " + customer.getId());
        name.setText("Nome: " + customer.getName());
        address.setText("Morada: " + customer.getAddress());
        phone.setText("Contacto: " + customer.getPhone());
        email.setText("E-mail: " + customer.getEmail());
        nif.setText("NIF: " + customer.getNif());
    }

    /**
     * Load customer details data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_CUSTOMER_QUERY);
        preparedStatement.setString(1, customerID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String id = resultSet.getString("id_cliente");
        String name = resultSet.getString("nome");
        String address = resultSet.getString("morada");
        String phone = resultSet.getString("contacto");
        String email = resultSet.getString("email");
        String nif = resultSet.getString("nif");
        String notes = resultSet.getString("notas");
        this.inflateUI(new Customer(id, name, address, phone, email, nif, notes));

    }

//    /**
//     * Customer delete handler
//     *
//     * @param event - delete event
//     */
//    @FXML
//    private void handleCustomerDelete(ActionEvent event) {
//        //Fetch the selected row
//        Customer selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
//        if (selectedForDeletion == null) {
//            AlertMaker.showErrorMessage("Nenhum cliente seleccionado",
//                    "Por favor seleccione um cliente para apagar.");
//            return;
//        }
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Apagar Cliente");
//        alert.setContentText("Tem a certeza que pretende apagar o cliente " + selectedForDeletion.getName() + "?");
//        Optional<ButtonType> answer = alert.showAndWait();
//
//        if (answer.get() == ButtonType.OK) {
//            Boolean result = DatabaseHandler.getInstance().deleteCustomer(selectedForDeletion);
//            if (result) {
//                AlertMaker.showSimpleAlert("Cliente apagado",
//                        selectedForDeletion.getName() + " foi apagado com sucesso.");
//                list.remove(selectedForDeletion);
//            } else {
//                AlertMaker.showSimpleAlert("Erro!",
//                        "Não foi possível apagar o cliente " + selectedForDeletion.getName());
//            }
//        } else {
//            AlertMaker.showSimpleAlert("Cancelado", "Nenhuns dados serão apagados.");
//        }
//    }

//    /**
//     * Refresh handler
//     *
//     * @param event - refresh event
//     * @throws SQLException - database exception
//     */
//    @FXML
//    private void handleRefresh(ActionEvent event) throws SQLException {
//        loadData();
//    }

//    /**
//     * Customer edit handler
//     *
//     * @param event - edit event
//     */
//    @FXML
//    private void handleCustomerEdit(ActionEvent event) {
//        //Fetch the selected row
//        Customer selectedForEdit = tableView.getSelectionModel().getSelectedItem();
//        if (selectedForEdit == null) {
//            AlertMaker.showErrorMessage("Nenhum cliente seleccionado",
//                    "Por favor seleccione um cliente para editar.");
//            return;
//        }
//        try {
//            FXMLLoader loader =
//                    new FXMLLoader(getClass().getResource("/shop/management/ui/add/customer/NoteAdd.fxml"));
//            Parent parent = loader.load();
//
//            CustomerAddController controller = loader.getController();
//            controller.inflateUI(selectedForEdit);
//
//            Stage stage = new Stage(StageStyle.DECORATED);
//            stage.setTitle("Editar Cliente");
//            stage.setScene(new Scene(parent));
//            stage.show();
//            ShopManagementUtil.setStageIcon(stage);
//
//            stage.setOnHiding((e) -> {
//                try {
//                    handleRefresh(new ActionEvent());
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            });
//        } catch (IOException ex) {
//            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
