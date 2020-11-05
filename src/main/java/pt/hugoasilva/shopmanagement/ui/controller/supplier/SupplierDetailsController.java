package pt.hugoasilva.shopmanagement.ui.controller.supplier;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.hugoasilva.shopmanagement.data.database.DatabaseHandler;
import pt.hugoasilva.shopmanagement.data.model.Note;
import pt.hugoasilva.shopmanagement.data.model.Supplier;
import pt.hugoasilva.shopmanagement.ui.controller.note.NoteAddController;
import pt.hugoasilva.shopmanagement.ui.controller.product.ProductSearchController;
import pt.hugoasilva.shopmanagement.ui.dialog.DialogHandler;
import pt.hugoasilva.shopmanagement.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Supplier Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class SupplierDetailsController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(ProductSearchController.class.getName());
    private static boolean option;
    // Notes list
    @FXML
    ObservableList<Note> list = FXCollections.observableArrayList();
    // Supplier data
    private Supplier supplier;
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
    ;
    @FXML
    private StackPane mainContainer;

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
     * @param supplier supplier object
     */
    public void inflateUI(Supplier supplier) {
        this.supplier = supplier;
        this.id.setText("Fornecedor nr: " + supplier.getId());
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
        this.list.clear();
        this.list = DatabaseHandler.getSupplierNotesList(this.supplier);
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
            controller.setPersonId(this.supplier.getId());
            controller.setPersonType("supplier");

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Adicionar Nota");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.getSupplierNotes();
        } catch (IOException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
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
     * @param event refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        this.getSupplierNotes();
    }

    /**
     * Note edit handler
     *
     * @param event edit event
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
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }
}