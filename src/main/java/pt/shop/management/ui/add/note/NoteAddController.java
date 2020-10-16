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
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Note Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class NoteAddController implements Initializable {

    private final String customerID;
    private final String notesPath;

    // Database handler instance
    DatabaseHandler databaseHandler;
    // UI Content
    @FXML
    private JFXTextField message;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;
    private Boolean isInEditMode = false;

    public NoteAddController(String customerID, String notesPath) {
        this.customerID = customerID;
        this.notesPath = notesPath;
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
     * Add note to JSON file
     *
     * @param event - add note event
     */
    @FXML
    private void addNote(ActionEvent event) {
        System.out.println(message.getText());
        // TODO
        // Add note to JSON and save

        if (message.getText().isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Dados insuficientes",
                    new String("Por favor insira uma descrição para a nota.".getBytes(), StandardCharsets.UTF_8));
            return;
        }

        if (isInEditMode) {
            handleUpdateNote();
        }
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
