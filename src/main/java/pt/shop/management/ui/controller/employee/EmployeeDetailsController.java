package pt.shop.management.ui.controller.employee;

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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.controller.note.NoteAddController;
import pt.shop.management.ui.dialog.DialogHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Employee Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class EmployeeDetailsController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(EmployeeDetailsController.class.getName());
    // Notes list
    @FXML
    ObservableList<Note> list = FXCollections.observableArrayList();
    // Employee data
    private Employee employee;
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
    @FXML
    private StackPane mainContainer;

    public EmployeeDetailsController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
    }

    /**
     * Init notes table columns
     */
    private void initCol() {
        this.tableView.setPlaceholder(new Label("Nenhuma nota adicionada"));
        this.messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
    }

    /**
     * Cancel button handler
     *
     * @param event cancel event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    /**
     * Populate interface
     *
     * @param employee employee object
     */
    public void inflateUI(Employee employee) {
        this.employee = employee;
        this.id.setText("Empregado nr: " + employee.getId());
        this.name.setText("Nome: " + employee.getName());
        this.address.setText("Morada: " + employee.getAddress());
        this.phone.setText("Contacto: " + employee.getPhone());
        this.email.setText("E-mail: " + employee.getEmail());
        this.nif.setText("NIF: " + employee.getNif());
        this.getEmployeeNotes();
    }

    /**
     * Get employee notes from database
     */
    private void getEmployeeNotes() {
        this.list.clear();
        this.list = DatabaseHandler.getEmployeeNotesList(this.employee);
        this.tableView.setItems(list);
    }

    /**
     * Show add note window
     */
    @FXML
    public void addNoteButtonAction() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/note/NoteAdd.fxml"));
            Parent parent = loader.load();

            NoteAddController controller = loader.getController();

            controller.setPersonId(this.employee.getId());
            controller.setPersonType("employee");

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Adicionar Nota");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.getEmployeeNotes();
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Note delete handler
     *
     * @param event delete event
     */
    @FXML
    private void handleNoteDelete(ActionEvent event) {
        //Fetch the selected row
        Note selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }
        boolean option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Nota",
                "Tem a certeza que pretende apagar a nota?");
        if (option) {
            boolean result = DatabaseHandler.deleteEmployeeNote(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Nota apagada",
                        "Nota apagada com sucesso.", false);
                list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Cancelado",
                        new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8),
                        false);
            }
        }
    }

    /**
     * Refresh handler
     *
     * @param event refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        this.getEmployeeNotes();
    }

    /**
     * Note edit handler
     *
     * @param event - edit event
     */
    @FXML
    public void handleNoteEdit(ActionEvent event) {
        //Fetch the selected row
        Note selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }
        selectedForEdit.setPersonType("employee");
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/note/NoteAdd.fxml"));
            Parent parent = loader.load();

            NoteAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Adicionar Nota");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.getEmployeeNotes();
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }
}