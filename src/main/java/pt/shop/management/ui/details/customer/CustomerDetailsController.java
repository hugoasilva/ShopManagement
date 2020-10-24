package pt.shop.management.ui.details.customer;

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
import pt.shop.management.data.files.JSONHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.add.note.NoteAddController;
import pt.shop.management.ui.alert.AlertMaker;
import pt.shop.management.ui.main.MainController;
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
 * Customer Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class CustomerDetailsController implements Initializable {

    // Database query
    private static final String SELECT_CUSTOMER_QUERY = "SELECT * FROM clientes WHERE id_cliente=?";

    // Local downloads path
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";

    // Customer data
    private final String customerID;
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

    public CustomerDetailsController(String id) {
        this.customerID = id;
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
    private void loadData() throws SQLException {
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
     * Get customer notes from JSON file
     *
     * @param id        - customer id
     * @param notesPath - customer notes path
     */
    private void getCustomerNotes(String id, String notesPath) {
        String fileName = id + ".json";
        // Download notes JSON file
        SFTPHandler.downloadFile(notesPath, fileName);
        // Parse JSON
        List<Note> notes = new LinkedList<>(JSONHandler.JSONToNotes(LOCAL_DOWNLOAD_PATH + fileName));
        notes.remove(0);
        // Delete notes list
        this.list = FXCollections.observableArrayList();
        // Add notes to list
        for (Note note : notes) {
            list.add(new Note(note.getId(), note.getMessage()));
        }
        // Add list to table
        tableView.setItems(list);
    }

    /**
     * Show add note window
     *
     * @throws IOException - IO exception
     */
    @FXML
    public void addNoteButtonAction() throws IOException {
        NoteAddController controller = new NoteAddController(
                LOCAL_DOWNLOAD_PATH + this.customerID + ".json", this.notesPath);

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
        this.getCustomerNotes(this.customerID, this.notesPath);
    }

    /**
     * Note delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleNoteDelete(ActionEvent event) {
        //Fetch the selected row
        Note selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Apagar Nota");
        alert.setContentText("Tem a certeza que pretende apagar a nota?");
        Optional<ButtonType> answer = alert.showAndWait();

        String fileName = LOCAL_DOWNLOAD_PATH + customerID + ".json";

        // Parse JSON
        List<Note> notes = new LinkedList<>(JSONHandler.JSONToNotes(fileName));

        System.out.println(selectedForDeletion.getId());
        // Remove note
        notes.remove(Integer.parseInt(selectedForDeletion.getId()));
        for (int i = 0; i < notes.size(); i++) {
            notes.get(i).setId(String.valueOf(i));
        }

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            JSONHandler.notesToJSON(notes, fileName);
            SFTPHandler.uploadFile(fileName, this.notesPath);
            AlertMaker.showSimpleAlert("Nota apagada",
                    "Nota apagada com sucesso.");
            list.remove(selectedForDeletion);
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
     * Note edit handler
     *
     * @param event - edit event
     */
    @FXML
    public void handleNoteEdit(ActionEvent event) {
        //Fetch the selected row
        Note selectedForEdit = tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }
        try {

            NoteAddController controller = new NoteAddController(
                    LOCAL_DOWNLOAD_PATH + this.customerID + ".json", this.notesPath);

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/note/NoteAdd.fxml"));
            loader.setController(controller);

            Parent parent = loader.load();

            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Adicionar Nota");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.getCustomerNotes(this.customerID, this.notesPath);

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}