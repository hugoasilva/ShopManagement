package pt.shop.management.ui.controller.note;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.dialog.DialogHandler;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Note Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class NoteAddController implements Initializable {

    // Note data
    private Note note;
    private String personId;
    private String personType;
    private Boolean isInEditMode = false;
    // UI Content
    @FXML
    private JFXTextField message;
    @FXML
    private StackPane rootPane;
    @FXML
    private StackPane mainContainer;

    public NoteAddController() {
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

    public void setPersonId(String id) {
        this.personId = id;
    }

    public void setPersontype(String type) {
        this.personType = type;
    }

    /**
     * Add note
     *
     * @param event - add note event
     */
    @FXML
    private void addNote(ActionEvent event) {

        String message = this.message.getText();

        if (message.isEmpty()) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Dados insuficientes",
                    new String("Por favor insira uma descrição para a nota.".getBytes(),
                            StandardCharsets.UTF_8));
            return;
        }

        if (this.isInEditMode) {
            this.handleUpdateNote();
            return;
        }
        Note note = null;
        switch (this.personType) {
            case "customer":
                String noteId = String.valueOf(DatabaseHandler.getCustomerNotesId());
                note = new Note(noteId, message);
                note.setPersonId(this.personId);
                note.setPersonType(this.personType);
                if (DatabaseHandler.insertCustomerNote(note)) {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Nota adicionada",
                            "Nota adicionada com sucesso!", true);
                } else {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                            "Verifique os dados e tente novamente.", false);
                }
                break;
            case "employee":
                noteId = String.valueOf(DatabaseHandler.getEmployeeNotesId());
                note = new Note(noteId, message);
                note.setPersonId(this.personId);
                note.setPersonType(this.personType);
                if (DatabaseHandler.insertEmployeeNote(note)) {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Nota adicionada",
                            "Nota adicionada com sucesso!", true);
                } else {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                            "Verifique os dados e tente novamente.", false);
                }
                break;
            case "supplier":
                noteId = String.valueOf(DatabaseHandler.getSupplierNotesId());
                note = new Note(noteId, message);
                note.setPersonId(this.personId);
                note.setPersonType(this.personType);
                if (DatabaseHandler.insertSupplierNote(note)) {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Nota adicionada",
                            "Nota adicionada com sucesso!", true);
                } else {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                            "Verifique os dados e tente novamente.", false);
                }
                break;
        }
    }

    /**
     * Populate table
     *
     * @param note - customer object
     */
    public void inflateUI(Note note) {
        this.message.setText(note.getMessage());
        this.note = note;

        this.isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear entry
     */
    private void clearEntries() {
        this.message.clear();
    }

    /**
     * Handle note update
     */
    private void handleUpdateNote() {

        String message = this.message.getText();

        // Check if note is empty
        if (message.isEmpty()) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Dados insuficientes",
                    new String("Por favor insira uma descrição para a nota.".getBytes(),
                            StandardCharsets.UTF_8), false);
            return;
        }
        switch (this.note.getPersonType()) {
            case "customer":
                note = new Note(this.note.getId(), message);
                boolean result = DatabaseHandler.updateCustomerNote(note);
                if (result) {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo",
                            "Nota editada com sucesso!", true);
                } else {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                            "Não foi possível atualizar a nota.", false);
                }
                break;
            case "employee":
                note = new Note(this.note.getId(), message);
                result = DatabaseHandler.updateEmployeeNote(note);
                if (result) {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo",
                            "Nota editada com sucesso!", true);
                } else {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                            "Não foi possível atualizar a nota.", false);
                }
                break;
            case "supplier":
                note = new Note(this.note.getId(), message);
                result = DatabaseHandler.updateSupplierNote(note);
                if (result) {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo",
                            "Nota editada com sucesso!", true);
                } else {
                    DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                            "Não foi possível atualizar a nota.", false);
                }
                break;
        }
    }
}
