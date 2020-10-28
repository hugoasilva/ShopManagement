package pt.shop.management.ui.search.employee;

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
import pt.shop.management.data.model.Employee;
import pt.shop.management.ui.add.employee.EmployeeAddController;
import pt.shop.management.ui.details.employee.EmployeeDetailsController;
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
 * Employee Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class EmployeeSearchController implements Initializable {

    // Employee list object
    ObservableList<Employee> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private JFXComboBox<Label> employeeCombo;
    @FXML
    private TextField employeeSearchInput;
    @FXML
    private TableView<Employee> tableView;
    @FXML
    private TableColumn<Employee, String> idCol;
    @FXML
    private TableColumn<Employee, String> nameCol;
    @FXML
    private TableColumn<Employee, String> addressCol;
    @FXML
    private TableColumn<Employee, String> phoneCol;
    @FXML
    private TableColumn<Employee, String> emailCol;
    @FXML
    private TableColumn<Employee, String> nifCol;
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
     * Assign table columns to employee properties
     */
    private void initCol() {
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        TableColumn<Employee, Void> detailsCol = new TableColumn<>("Ficha");

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Employee employee = getTableView().getItems().get(getIndex());
                                    showEmployeeDetails(employee);
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
     * Assign table columns to employee properties
     */
    private void initCombo() {
        this.employeeCombo.getItems().addAll(new Label("ID ou Nome"), new Label("NIF"),
                new Label("Contacto"), new Label("E-mail"));
        this.employeeCombo.setPromptText("Tipo de pesquisa...");
    }

    /**
     * Show employee details window
     *
     * @param employee - employee object
     */
    private void showEmployeeDetails(Employee employee) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/employee/EmployeeDetails.fxml"));
            Parent parent = loader.load();

            EmployeeDetailsController controller = loader.getController();
            controller.inflateUI(employee);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Ficha de Empregado");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            ShopManagementUtil.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load employee table data
     *
     * @throws SQLException - database exception
     */
    public void loadData() throws SQLException {
        this.list = DatabaseHandler.getEmployeeList();
        this.tableView.setItems(list);
    }

    /**
     * Employee delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleEmployeeDelete(ActionEvent event) throws SQLException {
        //Fetch the selected row
        Employee selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showErrorMessage("Nenhum empregado seleccionado",
                    "Por favor seleccione um empregado para apagar.");
            return;
        }
        boolean option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Empregado",
                "Tem a certeza que pretende apagar o empregado " + selectedForDeletion.getName() + "?");
        if (option) {
            boolean result = DatabaseHandler.deleteEmployee(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Empregado apagado",
                        selectedForDeletion.getName() + " foi apagado com sucesso.", false);
                list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro!",
                        new String("Não foi possível apagar o empregado ".getBytes(), StandardCharsets.UTF_8) +
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
        if (this.employeeCombo.getValue() == null && this.employeeSearchInput.getText().isEmpty()) {
            this.list.clear();
            this.list = DatabaseHandler.getEmployeeList();
            this.tableView.setItems(list);
        } else {
            this.searchEmployee();
        }
    }

    /**
     * Search employee operation
     *
     * @throws SQLException - SQL exception
     */
    public void searchEmployee() throws SQLException {
        // Check if user input is present
        if (this.employeeCombo.getSelectionModel().isEmpty() || this.employeeSearchInput.getText().isEmpty()) {
            DialogHandler.showErrorMessage("Erro!",
                    "Insira dados em todos os campos.");
        } else {
            String comboInput = this.employeeCombo.getSelectionModel().getSelectedItem().getText();
            String searchInput = this.employeeSearchInput.getText();
            this.list = DatabaseHandler.searchEmployee(comboInput, searchInput);
            this.tableView.setItems(list);
        }
    }

    /**
     * Handle search employee key press
     *
     * @param event - key event
     * @throws SQLException - SQL exception
     */
    public void handleSearchEmployeeKeyPress(KeyEvent event) throws SQLException {
        this.searchEmployee();
    }

    /**
     * Handle search employee button press
     *
     * @param event - button click event
     * @throws SQLException - SQL exception
     */
    public void handleSearchEmployeeButtonPress(ActionEvent event) throws SQLException {
        this.searchEmployee();
    }

    /**
     * Employee edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleEmployeeEdit(ActionEvent event) {
        //Fetch the selected row
        Employee selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showErrorMessage("Nenhum empregado seleccionado",
                    "Por favor seleccione um empregado para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/employee/EmployeeAdd.fxml"));
            Parent parent = loader.load();

            EmployeeAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Empregado");
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
            Logger.getLogger(EmployeeSearchController.class.getName()).log(Level.SEVERE, null, ex);
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
