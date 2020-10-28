package pt.shop.management.ui.search.supplier;

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
import pt.shop.management.data.model.Supplier;
import pt.shop.management.ui.add.supplier.SupplierAddController;
import pt.shop.management.ui.dialog.DialogHandler;
import pt.shop.management.ui.details.supplier.SupplierDetailsController;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Supplier Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class SupplierSearchController implements Initializable {

    // Supplier list object
    ObservableList<Supplier> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private JFXComboBox<Label> supplierCombo;
    @FXML
    private TextField supplierSearchInput;
    @FXML
    private TableView<Supplier> tableView;
    @FXML
    private TableColumn<Supplier, String> idCol;
    @FXML
    private TableColumn<Supplier, String> nameCol;
    @FXML
    private TableColumn<Supplier, String> addressCol;
    @FXML
    private TableColumn<Supplier, String> phoneCol;
    @FXML
    private TableColumn<Supplier, String> emailCol;
    @FXML
    private TableColumn<Supplier, String> nifCol;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
        this.initCombo();
        try {
            this.loadData();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Assign table columns to supplier properties
     */
    private void initCol() {
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        TableColumn<Supplier, Void> detailsCol = new TableColumn<>("Ficha");
        Callback<TableColumn<Supplier, Void>, TableCell<Supplier, Void>> cellFactoryDetails =
                new Callback<>() {
                    @Override
                    public TableCell<Supplier, Void> call(final TableColumn<Supplier, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Supplier supplier = getTableView().getItems().get(getIndex());
                                    try {
                                        showSupplierDetails(supplier);
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
        detailsCol.setPrefWidth(80);
        detailsCol.setCellFactory(cellFactoryDetails);
        this.tableView.getColumns().add(detailsCol);
    }

    /**
     * Initialize search combo box
     */
    private void initCombo() {
        this.supplierCombo.getItems().addAll(new Label("ID ou Nome"),
                new Label("Contacto"), new Label("E-mail"), new Label("NIF"));
        this.supplierCombo.setPromptText("Tipo de pesquisa...");
    }

    private void showSupplierDetails(Supplier supplier) throws IOException {
        SupplierDetailsController controller = new SupplierDetailsController(supplier);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/supplier/SupplierDetails.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Ficha de Fornecedor");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
    }

    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load supplier table data
     *
     * @throws SQLException - database exception
     */
    public void loadData() throws SQLException {
        this.list = DatabaseHandler.getSupplierList();
        this.tableView.setItems(list);
    }

    /**
     * Supplier delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleSupplierDelete(ActionEvent event) throws SQLException {
        //Fetch the selected row
        Supplier selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showErrorMessage("Nenhum fornecedor seleccionado",
                    "Por favor seleccione um fornecedor para apagar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Apagar Fornecedor");
        alert.setContentText("Tem a certeza que pretende apagar o fornecedor nr " + selectedForDeletion.getId() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            boolean result = DatabaseHandler.deleteSupplier(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialAlert(this.mainContainer, "Fornecedor apagado",
                        "Fornecedor nr " + selectedForDeletion.getId() + " apagado com sucesso.");
                list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialAlert(this.mainContainer, "Erro!",
                        new String("Não foi possível apagar o fornecedor nr ".getBytes(), StandardCharsets.UTF_8)
                                + selectedForDeletion.getId());
            }
        } else {
            DialogHandler.showMaterialAlert(this.mainContainer, "Cancelado",
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
        this.refreshTable();
    }

    public void refreshTable() throws SQLException {
        if (this.supplierCombo.getValue() == null && this.supplierSearchInput.getText().isEmpty()) {
            this.list.clear();
            this.list = DatabaseHandler.getSupplierList();
            this.tableView.setItems(list);
        } else {
            this.searchSupplier();
        }
    }

    /**
     * Search supplier operation
     *
     * @throws SQLException - SQL exception
     */
    public void searchSupplier() throws SQLException {
        // Check if user input is present
        if (this.supplierCombo.getSelectionModel().isEmpty() || this.supplierSearchInput.getText().isEmpty()) {
            DialogHandler.showErrorMessage("Erro!",
                    "Insira dados em todos os campos.");
        } else {
            String comboInput = this.supplierCombo.getSelectionModel().getSelectedItem().getText();
            String searchInput = this.supplierSearchInput.getText();
            this.list = DatabaseHandler.searchSupplier(comboInput, searchInput);
            this.tableView.setItems(list);
        }
    }

    /**
     * Handle search supplier key press
     *
     * @param event - key event
     * @throws SQLException - SQL exception
     */
    public void handleSearchSupplierKeyPress(KeyEvent event) throws SQLException {
        this.searchSupplier();
    }

    /**
     * Handle search supplier key press
     *
     * @param event - key event
     * @throws SQLException - SQL exception
     */
    public void handleSearchSupplierButtonPress(ActionEvent event) throws SQLException {
        this.searchSupplier();
    }

    /**
     * Supplier edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleSupplierEdit(ActionEvent event) {
        //Fetch the selected row
        Supplier selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showErrorMessage("Nenhum fornecedor seleccionado",
                    "Por favor seleccione um fornecedor para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/supplier/SupplierAdd.fxml"));
            Parent parent = loader.load();

            SupplierAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Fornecedor");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.refreshTable();

            stage.setOnHiding((e) -> {
                try {
                    handleRefresh(new ActionEvent());
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (IOException | SQLException ex) {
            Logger.getLogger(SupplierSearchController.class.getName()).log(Level.SEVERE, null, ex);
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
