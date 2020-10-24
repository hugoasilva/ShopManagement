package pt.shop.management.ui.details.employee;

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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.add.note.NoteAddController;
import pt.shop.management.ui.alert.AlertMaker;
import pt.shop.management.ui.search.invoice.InvoiceSearchController;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Employee Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class EmployeeDetailsController implements Initializable {

    // Database query
    private static final String SELECT_EMPLOYEE_NOTES_QUERY = "SELECT * FROM notes_employees WHERE employee_id=?";
    private static final String SELECT_EMPLOYEE_QUERY = "SELECT * FROM employees WHERE id=?";

    // Employee data
    private final String employeeID;
    // Notes list
    @FXML
    ObservableList<Note> list = FXCollections.observableArrayList();
    // Database handler instance
    DatabaseHandler databaseHandler;
    // UI content
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
    private TableView<Note> tableView;
    @FXML
    private TableColumn<Note, String> messageCol;

    public EmployeeDetailsController(String id) {
        this.employeeID = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        try {
            tableView.setPlaceholder(new Label("Nenhuma nota adicionada"));
            loadData();
            initCol();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Init notes table columns
     */
    private void initCol() {
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
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
     * Populate interface
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
    }

    /**
     * Load employee details data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_EMPLOYEE_QUERY);
        preparedStatement.setString(1, employeeID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String address = resultSet.getString("address");
        String phone = resultSet.getString("phone");
        String email = resultSet.getString("email");
        String nif = resultSet.getString("nif");
        this.getEmployeeNotes();
        this.inflateUI(new Employee(id, name, address, phone, email, nif));
    }

    /**
     * Get employee notes from JSON file
     */
    private void getEmployeeNotes() throws SQLException {
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_EMPLOYEE_NOTES_QUERY);
        preparedStatement.setString(1, employeeID);
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String message = resultSet.getString("message");

                list.add(new Note(id, message));
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
    }

    /**
     * Show add note window
     *
     * @throws IOException - IO exception
     */
    @FXML
    public void addNoteButtonAction() throws IOException, SQLException {
        NoteAddController controller = new NoteAddController(this.employeeID, "employee");

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/note/NoteAdd.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Adicionar Nota");
        stage.setScene(new Scene(parent));
        ShopManagementUtil.setStageIcon(stage);
        stage.showAndWait();
        this.getEmployeeNotes();
    }

    /**
     * Note delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleNoteDelete(ActionEvent event) {
//        //Fetch the selected row
//        Note selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
//        if (selectedForDeletion == null) {
//            AlertMaker.showErrorMessage("Nenhuma nota seleccionada",
//                    "Por favor seleccione uma nota para editar.");
//            return;
//        }
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Apagar Nota");
//        alert.setContentText("Tem a certeza que pretende apagar a nota?");
//        Optional<ButtonType> answer = alert.showAndWait();
//
//        String fileName = LOCAL_DOWNLOAD_PATH + employeeID + ".json";
//
//        // Parse JSON
//        List<Note> notes = new LinkedList<>(JSONHandler.JSONToNotes(fileName));
//
//        System.out.println(selectedForDeletion.getId());
//        // Remove note
//        notes.remove(Integer.parseInt(selectedForDeletion.getId()));
//        for (int i = 0; i < notes.size(); i++) {
//            notes.get(i).setId(String.valueOf(i));
//        }
//
//        if (answer.isPresent() && answer.get() == ButtonType.OK) {
//            JSONHandler.notesToJSON(notes, fileName);
//            SFTPHandler.uploadFile(fileName, this.notesPath);
//            AlertMaker.showSimpleAlert("Nota apagada",
//                    "Nota apagada com sucesso.");
//            list.remove(selectedForDeletion);
//        } else {
//            AlertMaker.showSimpleAlert("Cancelado",
//                    new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8));
//        }
    }

    /**
     * Refresh handler
     *
     * @param event - refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) throws SQLException {
        this.getEmployeeNotes();
    }

    /**
     * Note edit handler
     *
     * @param event - edit event
     */
    @FXML
    public void handleNoteEdit(ActionEvent event) {
//        //Fetch the selected row
//        Note selectedForEdit = tableView.getSelectionModel().getSelectedItem();
//
//        if (selectedForEdit == null) {
//            AlertMaker.showErrorMessage("Nenhuma nota seleccionada",
//                    "Por favor seleccione uma nota para editar.");
//            return;
//        }
//        try {
//
//            NoteAddController controller = new NoteAddController(
//                    LOCAL_DOWNLOAD_PATH + this.employeeID + ".json", this.notesPath);
//
//            FXMLLoader loader =
//                    new FXMLLoader(getClass().getResource(
//                            "/fxml/note/NoteAdd.fxml"));
//            loader.setController(controller);
//
//            Parent parent = loader.load();
//
//            controller.inflateUI(selectedForEdit);
//
//            Stage stage = new Stage(StageStyle.DECORATED);
//            stage.setTitle("Adicionar Nota");
//            stage.setScene(new Scene(parent));
//            ShopManagementUtil.setStageIcon(stage);
//            stage.showAndWait();
//            this.getEmployeeNotes(this.employeeID, this.notesPath);
//
//        } catch (IOException ex) {
//            Logger.getLogger(EmployeeDetailsController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}