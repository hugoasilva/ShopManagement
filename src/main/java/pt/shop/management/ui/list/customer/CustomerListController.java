package pt.shop.management.ui.list.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.ui.add.customer.CustomerAddController;
import pt.shop.management.ui.alert.AlertMaker;
import pt.shop.management.ui.details.customer.CustomerDetailsController;
import pt.shop.management.ui.main.MainController;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Customer List Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class CustomerListController implements Initializable {

    // Database query
    private static final String SELECT_CUSTOMERS_QUERY = "SELECT * FROM clientes";

    // Customer list object
    ObservableList<Customer> list = FXCollections.observableArrayList();

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
    private StackPane rootPane;
    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        try {
            loadData();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
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
        TableColumn<Customer, Void> detailsCol = new TableColumn<>("Ficha");

        Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Customer, Void> call(final TableColumn<Customer, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Customer data = getTableView().getItems().get(getIndex());
                                    try {
                                        showCustomerDetails(data.getId());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                };

        detailsCol.setCellFactory(cellFactory);
        tableView.getColumns().add(detailsCol);
    }

    /**
     * Show customer details window
     *
     * @param id - customer id
     */
    private void showCustomerDetails(String id) throws IOException {
        CustomerDetailsController controller = new CustomerDetailsController(id);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/customer/CustomerDetails.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Ficha de Cliente");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
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
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_CUSTOMERS_QUERY);
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
            Logger.getLogger(CustomerListController.class.getName()).log(Level.SEVERE, null, ex);
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

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            boolean result = DatabaseHandler.getInstance().deleteCustomer(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Cliente apagado",
                        selectedForDeletion.getName() + " foi apagado com sucesso.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Erro!",
                        new String("Não foi possível apagar o cliente ".getBytes(), StandardCharsets.UTF_8) +
                                selectedForDeletion.getName());
            }
        } else {
            AlertMaker.showSimpleAlert("Cancelado",
                    new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8));
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
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(CustomerListController.class.getName()).log(Level.SEVERE, null, ex);
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
