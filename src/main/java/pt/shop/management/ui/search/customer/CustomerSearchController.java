package pt.shop.management.ui.search.customer;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.ui.add.customer.CustomerAddController;
import pt.shop.management.ui.details.customer.CustomerDetailsController;
import pt.shop.management.ui.dialog.DialogHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Customer Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class CustomerSearchController implements Initializable {

    // Customer list object
    ObservableList<Customer> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private JFXComboBox<Label> customerCombo;
    @FXML
    private TextField customerSearchInput;
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
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
        this.initCombo();
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
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        TableColumn<Customer, Void> detailsCol = new TableColumn<>("Ficha");

        Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Customer, Void> call(final TableColumn<Customer, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Customer customer = getTableView().getItems().get(getIndex());
                                    showCustomerDetails(customer);
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
        detailsCol.setPrefWidth(80);
        detailsCol.setCellFactory(cellFactory);
        this.tableView.getColumns().add(detailsCol);
    }

    /**
     * Initialize search combo box
     */
    private void initCombo() {
        this.customerCombo.getItems().addAll(new Label("ID ou Nome"), new Label("NIF"),
                new Label("Contacto"), new Label("E-mail"));
        this.customerCombo.setPromptText("Tipo de pesquisa...");
    }

    /**
     * Show customer details window
     *
     * @param customer - customer object
     */
    private void showCustomerDetails(Customer customer) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/customer/CustomerDetails.fxml"));
            Parent parent = loader.load();

            CustomerDetailsController controller = loader.getController();
            controller.inflateUI(customer);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Ficha de Cliente");
            stage.setScene(new Scene(parent));
            stage.show();
            ShopManagementUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(CustomerSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load customer table data
     *
     * @throws SQLException - database exception
     */
    public void loadData() throws SQLException {
        this.list = DatabaseHandler.getCustomerList();
        this.tableView.setItems(list);
    }

    /**
     * Customer delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleCustomerDelete(ActionEvent event) throws SQLException {
        //Fetch the selected row
        Customer selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showErrorMessage("Nenhum cliente seleccionado",
                    "Por favor seleccione um cliente para apagar.");
            return;
        }
        boolean option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Cliente",
                "Tem a certeza que pretende apagar o cliente " + selectedForDeletion.getName() + "?");
        if (option) {
            boolean result = DatabaseHandler.deleteCustomer(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Cliente apagado",
                        selectedForDeletion.getName() + " foi apagado com sucesso.", false);
                list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro!",
                        new String("Não foi possível apagar o cliente ".getBytes(), StandardCharsets.UTF_8) +
                                selectedForDeletion.getName(), false);
            }
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Cancelado",
                    new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8), false);
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
        this.refreshTable();
    }

    public void refreshTable() throws SQLException {
        if (this.customerCombo.getValue() == null && this.customerSearchInput.getText().isEmpty()) {
            this.list.clear();
            this.list = DatabaseHandler.getCustomerList();
            this.tableView.setItems(list);
        } else {
            this.searchCustomer();
        }

    }

    /**
     * Search customer operation
     *
     * @throws SQLException - SQL exception
     */
    public void searchCustomer() throws SQLException {
        // Check if user input is present
        if (this.customerCombo.getSelectionModel().isEmpty() || this.customerSearchInput.getText().isEmpty()) {
            DialogHandler.showErrorMessage("Erro!",
                    "Insira dados em todos os campos.");
        } else {
            String comboInput = this.customerCombo.getSelectionModel().getSelectedItem().getText();
            String searchInput = this.customerSearchInput.getText();
            this.list = DatabaseHandler.searchCustomer(comboInput, searchInput);
            tableView.setItems(list);
        }
    }

    /**
     * Handle search customer key press
     *
     * @param event - key event
     * @throws IOException - IO exception
     */
    public void handleSearchCustomerKeyPress(KeyEvent event) throws IOException, SQLException {
        this.searchCustomer();
    }

    /**
     * Handle search customer button press
     *
     * @param event - button click event
     * @throws IOException - IO exception
     */
    public void handleSearchCustomerButtonPress(ActionEvent event) throws IOException, SQLException {
        this.searchCustomer();
    }

    /**
     * Customer edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleCustomerEdit(ActionEvent event) {
        //Fetch the selected row
        Customer selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showErrorMessage("Nenhum cliente seleccionado",
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
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.refreshTable();

            stage.setOnHiding((e) -> {
                try {
                    this.handleRefresh(new ActionEvent());
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (IOException | SQLException ex) {
            Logger.getLogger(CustomerSearchController.class.getName()).log(Level.SEVERE, null, ex);
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
