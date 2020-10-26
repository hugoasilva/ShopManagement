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

    // Note data
    private final String id;
    // Database handler instance
    DatabaseHandler databaseHandler;
    private String noteId;
    private String type;
    private Boolean isInEditMode = false;
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

        String message = this.message.getText();

        if (message.isEmpty()) {
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
        Note note = null;
        if (this.type.equals("customer")) {
            this.noteId = String.valueOf(DatabaseHandler.getCustomerNotesId());
            note = new Note(this.noteId, message);
            note.setPersonId(this.id);
            if (DatabaseHandler.insertCustomerNote(note)) {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Nota adicionada",
                        "Nota adicionada com sucesso!", true);
            } else {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Ocorreu um erro",
                        "Verifique os dados e tente novamente.", false);
            }
        } else {
            this.noteId = String.valueOf(DatabaseHandler.getEmployeeNotesId());
            note = new Note(this.noteId, message);
            note.setPersonId(this.id);
            if (DatabaseHandler.insertEmployeeNote(note)) {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Nota adicionada",
                        "Nota adicionada com sucesso!", true);
            } else {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Ocorreu um erro",
                        "Verifique os dados e tente novamente.", false);
            }
        }
    }

    /**
     * Populate table
     *
     * @param note - customer object
     */
    public void inflateUI(Note note) {
        this.message.setText(note.getMessage());
        this.noteId = note.getId();
        this.type = note.getPersonType();

        this.isInEditMode = Boolean.TRUE;
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

        String message = this.message.getText();

        // Check if note is empty
        if (message.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    new String("Por favor insira uma descrição para a nota.".getBytes(),
                            StandardCharsets.UTF_8), false);
            return;
        }
        Note note = null;
        if (this.type.equals("customer")) {
            note = new Note(this.noteId, message);
            boolean result = DatabaseHandler.updateCustomerNote(note);
            if (result) {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Successo",
                        "Nota editada com sucesso!", true);
            } else {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Ocorreu um erro",
                        "Não foi possível atualizar a nota.", false);
            }
        } else {
            note = new Note(this.noteId, message);
            boolean result = DatabaseHandler.updateEmployeeNote(note);
            if (result) {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Successo",
                        "Nota editada com sucesso!", true);
            } else {
                AlertMaker.showMaterialDialog(rootPane, mainContainer,
                        new ArrayList<>(), "Ocorreu um erro",
                        "Não foi possível atualizar a nota.", false);
            }
        }
    }
}
