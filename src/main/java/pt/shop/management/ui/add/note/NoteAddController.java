package pt.shop.management.ui.add.note;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.alert.AlertMaker;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Note Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-16
 */

public class NoteAddController implements Initializable {

    // Note data
    private final String id;
    private final String path;
    private Boolean isInEditMode = false;

    // UI Content
    @FXML
    private JFXTextField message;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    public NoteAddController(String id, String path) {
        this.id = id;
        this.path = path;
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
     * Add note
     *
     * @param event - add note event
     */
    @FXML
    private void addNote(ActionEvent event) {

        String noteId = String.valueOf(this.getNoteId());
        String noteMessage = message.getText();

        if (noteMessage.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Dados insuficientes",
                    new String("Por favor insira uma descrição para a nota.".getBytes(), StandardCharsets.UTF_8));
            return;
        }

        if (isInEditMode) {
            handleUpdateNote();
            return;
        }

        Note note = new Note(noteId, noteMessage);
        if (this.addNoteToJSON(note)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Nota adicionada", "Nota adicionado com sucesso!");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Ocorreu um erro", "Verifique os dados e tente novamente.");
        }
    }

    /**
     * Get notes count at JSON file
     *
     * @return - new note's id
     */
    private int getNoteId() {
        return 1;
    }

    /**
     * Add note to JSON file
     *
     * @param note - note object
     * @return - true if successful, false otherwise
     */
    private boolean addNoteToJSON(Note note) {
        // TODO
        return true;
    }

    /**
     * Populate table
     *
     * @param note - customer object
     */
    public void inflateUI(Note note) {
        this.message.setText(note.getMessage());

        isInEditMode = Boolean.TRUE;
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
//        Customer customer = new Customer(id, name.getText(), address.getText(),
//                phone.getText(), email.getText(), nif.getText(), REMOTE_CUSTOMER_PATH + id + ".json");
//        if (DatabaseHandler.getInstance().updateCustomer(customer)) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Successo!",
//                    "Dados de cliente atualizados.");
//        } else {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Erro",
//                    "Não foi possível atualizar os dados.");
//        }
    }
}
