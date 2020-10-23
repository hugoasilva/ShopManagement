package pt.shop.management.ui.details.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.JSONHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.add.note.NoteAddController;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Employee Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class EmployeeDetailsController implements Initializable {

    // Database query
    private static final String SELECT_EMPLOYEE_QUERY = "SELECT * FROM empregados WHERE id_empregado=?";

    // Local downloads path
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";

    // Employee data
    private final String employeeID;
    // Notes list
    @FXML
    ObservableList<Note> list = FXCollections.observableArrayList();
    // Database handler instance
    DatabaseHandler databaseHandler;
    private String notesPath;
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
            tableView.setItems(list);
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

        String id = resultSet.getString("id_empregado");
        String name = resultSet.getString("nome");
        String address = resultSet.getString("morada");
        String phone = resultSet.getString("contacto");
        String email = resultSet.getString("email");
        String nif = resultSet.getString("nif");
        String notes = resultSet.getString("notas");
        this.notesPath = notes;
        this.getEmployeeNotes(id, notes);
        this.inflateUI(new Employee(id, name, address, phone, email, nif, notes));
    }

    /**
     * Get employee notes from JSON file
     *
     * @param id        - employee id
     * @param notesPath - employee notes path
     */
    private void getEmployeeNotes(String id, String notesPath) {
        String fileName = id + ".json";
        SFTPHandler.downloadFile(notesPath, fileName);

        // Parse JSON
        List<Note> notes = JSONHandler.JSONToNotes(LOCAL_DOWNLOAD_PATH + id + ".json");

        for (Note note : notes) {
            list.add(new Note(note.getId(), note.getMessage()));
        }
    }

    /**
     * Show add note window
     *
     * @throws IOException - IO exception
     */
    @FXML
    public void addNoteButtonAction() throws IOException {
        String fileName = this.employeeID + ".json";
        NoteAddController controller = new NoteAddController(LOCAL_DOWNLOAD_PATH + fileName, this.notesPath);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/note/NoteAdd.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Adicionar Nota");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
    }
}
