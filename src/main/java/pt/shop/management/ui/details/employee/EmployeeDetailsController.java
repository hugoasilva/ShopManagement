package pt.shop.management.ui.details.employee;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
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
import pt.shop.management.data.files.FileHandler;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.add.note.NoteAddController;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Employee Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class EmployeeDetailsController implements Initializable {

    // Database query
    private static final String SELECT_EMPLOYEE_QUERY = "SELECT * FROM empregados WHERE id_empregado=?";

    // Local downloads path
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";

    // Employee data
    private final String employeeID;
    private String notesPath;

    // Notes list
    @FXML
    ObservableList<Note> list = FXCollections.observableArrayList();

    // Database handler instance
    DatabaseHandler databaseHandler;

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
    private TableView<Note> tableView;
    @FXML
    private TableColumn<Note, String> messageCol;

    public EmployeeDetailsController(String id) throws SQLException {
        this.employeeID = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        try {
            tableView.setItems(list);
            loadData();
            initCol();
        } catch (SQLException | IOException | SftpException | JSchException throwables) {
            throwables.printStackTrace();
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
    private void loadData() throws SQLException, SftpException, JSchException, IOException {
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
     * Get employee notes JSON file
     *
     * @param id        - employee id
     * @param notesPath - employee notes path
     * @throws SftpException - SFTP exception
     * @throws JSchException - JSch exception
     * @throws IOException   - IO exception
     */
    private void getEmployeeNotes(String id, String notesPath) throws SftpException, JSchException, IOException {
        String fileName = id + ".json";
        FileHandler.downloadFile(notesPath, fileName);

        // Parse JSON
        this.parseJSON(LOCAL_DOWNLOAD_PATH + fileName);
    }

    /**
     * Parse JSON file to notes list
     *
     * @param filePath - JSON file path
     * @throws IOException - IO exception
     */
    private void parseJSON(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        try (Reader reader = Files.newBufferedReader(path,
                StandardCharsets.UTF_8)) {

            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(reader);

            JsonArray array = tree.getAsJsonArray();

            for (JsonElement element : array) {

                if (element.isJsonObject()) {
                    JsonObject note = element.getAsJsonObject();
                    String id = note.get("id").getAsString();
                    String message = note.get("message").getAsString();
                    list.add(new Note(id, message));
                }
            }
        }
    }

    /**
     * Show add note window
     *
     * @throws IOException - IO exception
     */
    @FXML
    public void addNoteButtonAction() throws IOException {
        NoteAddController controller = new NoteAddController(this.employeeID, this.notesPath);

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
