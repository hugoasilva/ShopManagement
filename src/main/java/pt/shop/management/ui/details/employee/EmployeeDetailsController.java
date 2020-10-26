package pt.shop.management.ui.details.employee;

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
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.add.note.NoteAddController;
import pt.shop.management.ui.alert.AlertMaker;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Employee Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class EmployeeDetailsController implements Initializable {

    // Employee data
    private final Employee employee;
    // Notes list
    @FXML
    ObservableList<Note> list = FXCollections.observableArrayList();
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

    public EmployeeDetailsController(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.initCol();
            this.getEmployeeNotes();
            this.inflateUI(this.employee);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
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
        this.id.setText("ID: " + employee.getId());
        this.name.setText("Nome: " + employee.getName());
        this.address.setText("Morada: " + employee.getAddress());
        this.phone.setText("Contacto: " + employee.getPhone());
        this.email.setText("E-mail: " + employee.getEmail());
        this.nif.setText("NIF: " + employee.getNif());
    }

    /**
     * Get employee notes from database
     */
    private void getEmployeeNotes() throws SQLException {
        this.list.clear();
        this.list = DatabaseHandler.getEmployeeNotesList(this.employee);
        this.tableView.setItems(list);
    }

    /**
     * Show add note window
     *
     * @throws IOException - IO exception
     */
    @FXML
    public void addNoteButtonAction() throws IOException, SQLException {
        NoteAddController controller = new NoteAddController(this.employee.getId(), "employee");

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
        this.getEmployeeNotes();
    }

    /**
     * Note delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleNoteDelete(ActionEvent event) throws SQLException {
        //Fetch the selected row
        Note selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Apagar Nota");
        alert.setContentText("Tem a certeza que pretende apagar a nota?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            boolean result = DatabaseHandler.deleteEmployeeNote(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Nota apagada",
                        "Nota apagada com sucesso.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Cancelado",
                        new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * Refresh handler
     *
     * @param event - refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) throws SQLException {
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
        selectedForEdit.setPersonType("employee");

        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }
        try {

            NoteAddController controller = new NoteAddController(this.employee.getId(), "employee");

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
            this.getEmployeeNotes();

        } catch (IOException | SQLException ex) {
            Logger.getLogger(EmployeeDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}