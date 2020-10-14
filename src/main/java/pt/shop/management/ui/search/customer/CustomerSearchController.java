package main.java.pt.shop.management.ui.search.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.pt.shop.management.ui.alert.AlertMaker;
import main.java.pt.shop.management.data.database.DatabaseHandler;
import main.java.pt.shop.management.data.model.Customer;
import main.java.pt.shop.management.ui.add.customer.CustomerAddController;
import main.java.pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Customer Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class CustomerSearchController implements Initializable {

    // Database queries
    private static final String SEARCH_ID_QUERY = "SELECT * FROM clientes WHERE id_cliente=?";
    private static final String SEARCH_NAME_QUERY = "SELECT * FROM clientes WHERE nome LIKE ?";
    private static final String SEARCH_NIF_QUERY = "SELECT * FROM clientes WHERE nif=?";
    private static final String SEARCH_PHONE_QUERY = "SELECT * FROM clientes WHERE contacto=?";
    private static final String SEARCH_EMAIL_QUERY = "SELECT * FROM clientes WHERE email=?";

    // Customer list object
    ObservableList<Customer> list = FXCollections.observableArrayList();

    private String type;
    private String search;
    // UI Content
    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, String> idCol;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private TableColumn<Customer, String> emailCol;
    @FXML
    private TableColumn<Customer, String> nifCol;
    @FXML
    private TableColumn<Customer, String> notesCol;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane contentPane;

    public CustomerSearchController(String type, String search) {
        this.type = type;
        this.search = search;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        try {
            loadData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Assign table columns to customer properties
     */
    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    /**
     * Load customer table data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        String query = null;
        switch (this.type) {
            case "ID":
                query = SEARCH_ID_QUERY;
                break;
            case "Nome":
                query = SEARCH_NAME_QUERY;
                this.search = "%" + this.search + "%";
                break;
            case "NIF":
                query = SEARCH_NIF_QUERY;
                break;
            case "Contacto":
                query = SEARCH_PHONE_QUERY;
                break;
            case "E-mail":
                query = SEARCH_EMAIL_QUERY;
                break;
        }
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(query);
        preparedStatement.setString(1, this.search);
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id_cliente");
                String name = resultSet.getString("nome");
                String address = resultSet.getString("morada");
                String phone = resultSet.getString("contacto");
                String email = resultSet.getString("email");
                String nif = resultSet.getString("nif");
                String notes = resultSet.getString("notas");

                list.add(new Customer(id, name, address, phone, email, nif, notes));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
    }

    /**
     * Customer delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleCustomerDelete(ActionEvent event) {
        //Fetch the selected row
        Customer selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("Nenhum cliente seleccionado",
                    "Por favor seleccione um cliente para apagar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Apagar Cliente");
        alert.setContentText("Tem a certeza que pretende apagar o cliente " + selectedForDeletion.getName() + "?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandler.getInstance().deleteCustomer(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Cliente apagado",
                        selectedForDeletion.getName() + " foi apagado com sucesso.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Erro!",
                        "Não foi possível apagar o cliente " + selectedForDeletion.getName());
            }
        } else {
            AlertMaker.showSimpleAlert("Cancelado", "Nenhuns dados serão apagados.");
        }
    }

    /**
     * Refresh handler
     *
     * @param event - refresh event
     * @throws SQLException - database exception
     */
    @FXML
    private void handleRefresh(ActionEvent event) throws SQLException {
        loadData();
    }

    /**
     * Customer edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleCustomerEdit(ActionEvent event) {
        //Fetch the selected row
        Customer selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("Nenhum cliente seleccionado",
                    "Por favor seleccione um cliente para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/customer/CustomerAdd.fxml"));
            Parent parent = loader.load();

            CustomerAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Cliente");
            stage.setScene(new Scene(parent));
            stage.show();
            ShopManagementUtil.setStageIcon(stage);

            stage.setOnHiding((e) -> {
                try {
                    handleRefresh(new ActionEvent());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(CustomerAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Close current window
     *
     * @param event - close event
     */
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }
}
