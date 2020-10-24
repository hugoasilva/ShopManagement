package pt.shop.management.ui.add.note;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.alert.AlertMaker;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Note Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class NoteAddController implements Initializable {

    // Database queries
    private static final String INSERT_CUSTOMER_NOTE_QUERY = "INSERT INTO notes_customers " +
            "(customer_id, message) VALUES (?, ?)";
    private static final String INSERT_EMPLOYEE_NOTE_QUERY = "INSERT INTO notes_employees " +
            "(employee_id, message) VALUES (?, ?)";

    // Database handler instance
    DatabaseHandler databaseHandler;

    private Boolean isInEditMode = false;
    private String id;
    private final String type;

    // UI Content
    @FXML
    private JFXTextField message;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    public NoteAddController(String id, String type) {
        this.id = id;
        this.type = type;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    /**
     * Cancel button handler
     *
     * @param event - cancel event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.close();
    }

    /**
     * Close parent
     */
    @FXML
    private void closeParent() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Add note
     *
     * @param event - add note event
     */
    @FXML
    private void addNote(ActionEvent event) throws SQLException {
        String query = null;
        switch (this.type) {
            case "customer":
                query = INSERT_CUSTOMER_NOTE_QUERY;
                break;
            case "employee":
                query = INSERT_EMPLOYEE_NOTE_QUERY;
                break;
        }

        databaseHandler = DatabaseHandler.getInstance();
        PreparedStatement preparedStatement = databaseHandler.getConnection().prepareStatement(query);
        preparedStatement.setString(1, this.id);
        preparedStatement.setString(2, message.getText());

        if (message.getText().isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    new String("Por favor insira uma descrição para a nota.".getBytes(),
                            StandardCharsets.UTF_8), false);
            return;
        }

        if (isInEditMode) {
            handleUpdateNote();
            return;
        }
        if (preparedStatement.executeUpdate() > 0) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Nota adicionada",
                    "Nota adicionada com sucesso!", true);
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
        }
    }

    /**
     * Populate table
     *
     * @param note - customer object
     */
    public void inflateUI(Note note) {
        this.message.setText(note.getMessage());

        this.isInEditMode = Boolean.TRUE;
        this.id = note.getId();
    }

    /**
     * Clear entry
     */
    private void clearEntries() {
        message.clear();
    }

    /**
     * Handle customer update
     */
    private void handleUpdateNote() {
//        Note note = new Note(this.id, message.getText());
//
//        // Check if note is empty
//        if (note.getMessage().isEmpty()) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer,
//                    new ArrayList<>(), "Dados insuficientes",
//                    new String("Por favor insira uma descrição para a nota.".getBytes(),
//                            StandardCharsets.UTF_8), false);
//            return;
//        }
//
//        // Parse JSON
//        List<Note> notes = new LinkedList<>(JSONHandler.JSONToNotes(this.localPath));
//
//        // Edit note message
//        notes.set(Integer.parseInt(note.getId()), note);
//
//        // Convert notes to JSON and upload to server
//        if (JSONHandler.notesToJSON(notes, this.localPath)) {
//            SFTPHandler.uploadFile(this.localPath, this.remotePath);
//            AlertMaker.showMaterialDialog(rootPane, mainContainer,
//                    new ArrayList<>(), "Successo",
//                    "Nota editada com sucesso!", true);
//        } else {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer,
//                    new ArrayList<>(), "Ocorreu um erro",
//                    "Não foi possível atualizar a nota.", false);
//        }
    }
}
