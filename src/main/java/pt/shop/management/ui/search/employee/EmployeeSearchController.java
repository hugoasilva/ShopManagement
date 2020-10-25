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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Employee;
import pt.shop.management.ui.add.employee.EmployeeAddController;
import pt.shop.management.ui.alert.AlertMaker;
import pt.shop.management.ui.details.employee.EmployeeDetailsController;
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
 * Employee Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class EmployeeSearchController implements Initializable {

    // Database queries
    private static final String SEARCH_ID_QUERY = "SELECT * FROM employees WHERE id=?";
    private static final String SEARCH_NAME_QUERY = "SELECT * FROM employees WHERE name LIKE ?";
    private static final String SEARCH_NIF_QUERY = "SELECT * FROM employees WHERE nif=?";
    private static final String SEARCH_PHONE_QUERY = "SELECT * FROM employees WHERE phone=?";
    private static final String SEARCH_EMAIL_QUERY = "SELECT * FROM employees WHERE email=?";
    private static final String SELECT_EMPLOYEES_QUERY = "SELECT * FROM employees";

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
     * Assign table columns to employee properties
     */
    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        TableColumn<Employee, Void> detailsCol = new TableColumn<>("Ficha");

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Employee data = getTableView().getItems().get(getIndex());
                                    try {
                                        showEmployeeDetails(data.getId());
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
        detailsCol.setCellFactory(cellFactory);
        tableView.getColumns().add(detailsCol);
    }

    /**
     * Assign table columns to employee properties
     */
    private void initCombo() {
        employeeCombo.getItems().addAll(new Label("ID"), new Label("Nome"),
                new Label("NIF"), new Label("Contacto"), new Label("E-mail"));
        employeeCombo.setPromptText("Tipo de pesquisa...");
    }

    /**
     * Show employee details window
     *
     * @param id - employee id
     */
    private void showEmployeeDetails(String id) throws IOException {
        EmployeeDetailsController controller = new EmployeeDetailsController(id);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/employee/EmployeeDetails.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Ficha de Empregado");
        stage.setScene(new Scene(parent));
        stage.showAndWait();
        ShopManagementUtil.setStageIcon(stage);
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    /**
     * Load employee table data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_EMPLOYEES_QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String nif = resultSet.getString("nif");

                list.add(new Employee(id, name, address, phone, email, nif));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
    }

    /**
     * Employee delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleEmployeeDelete(ActionEvent event) {
        //Fetch the selected row
        Employee selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("Nenhum empregado seleccionado",
                    "Por favor seleccione um empregado para apagar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Apagar Empregado");
        alert.setContentText("Tem a certeza que pretende apagar o empregado " + selectedForDeletion.getName() + "?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {
            boolean result = DatabaseHandler.getInstance().deleteEmployee(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Empregado apagado",
                        selectedForDeletion.getName() + " foi apagado com sucesso.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Erro!",
                        new String("Não foi possível apagar o empregado ".getBytes(), StandardCharsets.UTF_8) +
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
     * Search employee operation
     *
     * @throws SQLException - SQL exception
     */
    public void searchEmployee() throws SQLException {
        // Check if user input is present
        if (employeeCombo.getSelectionModel().isEmpty() || employeeSearchInput.getText().isEmpty()) {
            AlertMaker.showErrorMessage("Erro!",
                    "Insira dados em todos os campos.");
        } else {
            String comboInput = employeeCombo.getSelectionModel().getSelectedItem().getText();
            String searchInput = employeeSearchInput.getText();
            String query = null;
            switch (comboInput) {
                case "ID":
                    query = SEARCH_ID_QUERY;
                    break;
                case "Nome":
                    query = SEARCH_NAME_QUERY;
                    searchInput = "%" + searchInput + "%";
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
            preparedStatement.setString(1, searchInput);
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    String nif = resultSet.getString("nif");

                    list.add(new Employee(id, name, address, phone, email, nif));
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeSearchController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.setItems(list);
        }
    }

    /**
     * Handle search employee key press
     *
     * @param event - key event
     * @throws IOException - IO exception
     */
    public void handleSearchEmployeeKeyPress(KeyEvent event) throws IOException, SQLException {
        this.searchEmployee();
    }

    /**
     * Handle search employee button press
     *
     * @param event - button click event
     * @throws IOException - IO exception
     */
    public void handleSearchEmployeeButtonPress(ActionEvent event) throws IOException, SQLException {
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
        Employee selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("Nenhum empregado seleccionado",
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
            this.loadData();

            stage.setOnHiding((e) -> {
                try {
                    handleRefresh(new ActionEvent());
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
