package pt.shop.management.ui.details.customer;

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
import pt.shop.management.data.files.JSONHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Customer;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Customer Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-15
 */

public class CustomerDetailsController implements Initializable {

    // Database query
    private static final String SELECT_CUSTOMER_QUERY = "SELECT * FROM clientes WHERE id_cliente=?";

    // Local downloads path
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";

    // Customer data
    private final String customerID;
    private String notesPath;

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

    public CustomerDetailsController(String id) throws SQLException {
        this.customerID = id;
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
     * @param customer - customer object
     */
    public void inflateUI(Customer customer) {
        id.setText("ID: " + customer.getId());
        name.setText("Nome: " + customer.getName());
        address.setText("Morada: " + customer.getAddress());
        phone.setText("Contacto: " + customer.getPhone());
        email.setText("E-mail: " + customer.getEmail());
        nif.setText("NIF: " + customer.getNif());
    }

    /**
     * Load customer details data
     *
     * @throws SQLException - database exception
     */
    private void loadData() throws SQLException, SftpException, JSchException, IOException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = handler.getConnection().prepareStatement(SELECT_CUSTOMER_QUERY);
        preparedStatement.setString(1, customerID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String id = resultSet.getString("id_cliente");
        String name = resultSet.getString("nome");
        String address = resultSet.getString("morada");
        String phone = resultSet.getString("contacto");
        String email = resultSet.getString("email");
        String nif = resultSet.getString("nif");
        String notes = resultSet.getString("notas");
        this.notesPath = notes;
        this.getCustomerNotes(id, notes);
        this.inflateUI(new Customer(id, name, address, phone, email, nif, notes));
    }

    /**
     * Get customer notes JSON file
     *
     * @param id        - customer id
     * @param notesPath - customer notes path
     */
    private void getCustomerNotes(String id, String notesPath) {
        String fileName = id + ".json";
        SFTPHandler.downloadFile(notesPath, fileName);

        // Parse JSON
        List<Note> notes = JSONHandler.JSONToNotes(LOCAL_DOWNLOAD_PATH + fileName);

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
        NoteAddController controller = new NoteAddController(this.customerID, this.notesPath);

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
