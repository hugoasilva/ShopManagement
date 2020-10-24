package pt.shop.management.ui.add.note;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.files.JSONHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.alert.AlertMaker;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Note Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class NoteAddController implements Initializable {

    // Note data
    private final String localPath;
    private final String remotePath;

    private Boolean isInEditMode = false;
    private String id;

    // UI Content
    @FXML
    private JFXTextField message;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    public NoteAddController(String localPath, String remotePath) {
        this.localPath = localPath;
        this.remotePath = remotePath;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void addNote(ActionEvent event) {
        // Initialize a list of type DataObject
        List<Note> notes = new LinkedList<>(JSONHandler.JSONToNotes(this.localPath));

        if (!notes.get(0).getMessage().equals("error")) {
            String noteId = String.valueOf(notes.size());
            String noteMessage = message.getText();

            if (noteMessage.isEmpty()) {
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

            notes.add(new Note(noteId, noteMessage));
            if (JSONHandler.notesToJSON(notes, this.localPath)) {
                SFTPHandler.uploadFile(this.localPath, this.remotePath);
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
        Note note = new Note(this.id, message.getText());

        // Check if note is empty
        if (note.getMessage().isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    new String("Por favor insira uma descrição para a nota.".getBytes(),
                            StandardCharsets.UTF_8), false);
            return;
        }

        // Parse JSON
        List<Note> notes = new LinkedList<>(JSONHandler.JSONToNotes(this.localPath));

        // Edit note message
        notes.set(Integer.parseInt(note.getId()), note);

        // Convert notes to JSON and upload to server
        if (JSONHandler.notesToJSON(notes, this.localPath)) {
            SFTPHandler.uploadFile(this.localPath, this.remotePath);
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
