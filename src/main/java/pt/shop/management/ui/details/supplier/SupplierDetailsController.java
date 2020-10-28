package pt.shop.management.ui.details.supplier;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Note;
import pt.shop.management.data.model.Supplier;
import pt.shop.management.ui.add.note.NoteAddController;
import pt.shop.management.ui.dialog.DialogHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Supplier Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class SupplierDetailsController implements Initializable {

    // Supplier data
    private Supplier supplier;
    private static boolean option;
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
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    public SupplierDetailsController() {
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
     * @param supplier - supplier object
     */
    public void inflateUI(Supplier supplier) {
        this.supplier = supplier;
        this.id.setText("ID: " + supplier.getId());
        this.name.setText("Nome: " + supplier.getName());
        this.address.setText("Morada: " + supplier.getAddress());
        this.phone.setText("Contacto: " + supplier.getPhone());
        this.email.setText("E-mail: " + supplier.getEmail());
        this.nif.setText("NIF: " + supplier.getNif());
        this.getSupplierNotes();
    }

    /**
     * Get supplier notes
     */
    private void getSupplierNotes() {
        try {
            this.list.clear();
            this.list = DatabaseHandler.getSupplierNotesList(this.supplier);
            this.tableView.setItems(list);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Show add note window
     *
     * @throws SQLException - SQL exception
     */
    @FXML
    public void addNoteButtonAction() throws SQLException {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/note/NoteAdd.fxml"));
            Parent parent = loader.load();

            NoteAddController controller = loader.getController();
            controller.setPersonId(this.supplier.getId());
            controller.setPersontype("supplier");

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Adicionar Nota");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.getSupplierNotes();
        } catch (IOException ex) {
            Logger.getLogger(SupplierDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Note delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleNoteDelete(ActionEvent event) throws SQLException, InterruptedException {
        //Fetch the selected row
        Note selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showErrorMessage("Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }
        option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Nota",
                "Tem a certeza que pretende apagar a nota?");
        if (option) {
            boolean result = DatabaseHandler.deleteSupplierNote(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Nota apagada",
                        "Nota apagada com sucesso.", false);
                this.list.remove(selectedForDeletion);
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
     * @param event - refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) throws SQLException {
        this.getSupplierNotes();
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
            DialogHandler.showErrorMessage("Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }
        selectedForEdit.setPersonType("supplier");
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
            this.getSupplierNotes();

        } catch (IOException ex) {
            Logger.getLogger(SupplierDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}