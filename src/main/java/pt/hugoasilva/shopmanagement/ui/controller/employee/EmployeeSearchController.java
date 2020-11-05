package pt.hugoasilva.shopmanagement.ui.controller.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.javafx.FontIcon;
import pt.hugoasilva.shopmanagement.data.database.DatabaseHandler;
import pt.hugoasilva.shopmanagement.data.model.Employee;
import pt.hugoasilva.shopmanagement.ui.dialog.DialogHandler;
import pt.hugoasilva.shopmanagement.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Employee Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class EmployeeSearchController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(EmployeeSearchController.class.getName());

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
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
        this.initCombo();
        this.loadData();
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
                            private final JFXButton btn = new JFXButton();

                            {
                                FontIcon icon = new FontIcon("mdi-file-document");
                                icon.getStyleClass().add("font-icon-button");
                                icon.setIconSize(30);
                                btn.setGraphic(icon);
                                btn.setAlignment(Pos.CENTER);
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
        detailsCol.setPrefWidth(60);
        detailsCol.setStyle("-fx-alignment: CENTER;");
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
     * @param employee employee object
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
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Get window stage
     *
     * @return window stage object
     */
    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load employee table data
     */
    public void loadData() {
        this.list = DatabaseHandler.getEmployeeList();
        this.tableView.setItems(list);
    }

    /**
     * Employee delete handler
     *
     * @param event delete event
     */
    @FXML
    private void handleEmployeeDelete(ActionEvent event) {
        //Fetch the selected row
        Employee selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum empregado seleccionado",
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
     * @param event refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        this.refreshTable();
    }

    public void refreshTable() {
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
     */
    public void searchEmployee() {
        // Check if user input is present
        if (this.employeeCombo.getSelectionModel().isEmpty() || this.employeeSearchInput.getText().isEmpty()) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Erro!",
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
     * @param event key event
     */
    public void handleSearchEmployeeKeyPress(KeyEvent event) {
        this.searchEmployee();
    }

    /**
     * Handle search employee button press
     *
     * @param event button click event
     */
    public void handleSearchEmployeeButtonPress(ActionEvent event) {
        this.searchEmployee();
    }

    /**
     * Employee edit handler
     *
     * @param event edit event
     */
    @FXML
    private void handleEmployeeEdit(ActionEvent event) {
        //Fetch the selected row
        Employee selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum empregado seleccionado",
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

            stage.setOnHiding((e) -> this.handleRefresh(new ActionEvent()));
        } catch (IOException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Close current window
     *
     * @param event close event
     */
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }
}
