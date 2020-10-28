package pt.shop.management.ui.details.customer;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Note;
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
 * Customer Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class CustomerDetailsController implements Initializable {

    // Customer data
    private final Customer customer;
    private static boolean option;
    private static boolean isChosen;
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

    public CustomerDetailsController(Customer customer) {
        this.customer = customer;
    }

    public static void setOption(boolean choice) {
        option = choice;
        isChosen = true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.initCol();
            this.getCustomerNotes();
            this.inflateUI(this.customer);
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
     * @param customer - customer object
     */
    public void inflateUI(Customer customer) {
        this.id.setText("ID: " + customer.getId());
        this.name.setText("Nome: " + customer.getName());
        this.address.setText("Morada: " + customer.getAddress());
        this.phone.setText("Contacto: " + customer.getPhone());
        this.email.setText("E-mail: " + customer.getEmail());
        this.nif.setText("NIF: " + customer.getNif());
    }

    /**
     * Get customer notes
     */
    private void getCustomerNotes() throws SQLException {
        this.list.clear();
        this.list = DatabaseHandler.getCustomerNotesList(this.customer);
        this.tableView.setItems(list);
    }

    /**
     * Show add note window
     *
     * @throws IOException - IO exception
     */
    @FXML
    public void addNoteButtonAction() throws IOException, SQLException {
        NoteAddController controller = new NoteAddController(this.customer.getId(), "customer");

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
        this.getCustomerNotes();
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
        option = DialogHandler.showMaterialAlertWithCancel(this.mainContainer,
                "Apagar Nota",
                "Tem a certeza que pretende apagar a nota?", false);

        System.out.println(option);
        if(option) {
            boolean result = DatabaseHandler.deleteCustomerNote(selectedForDeletion);
            if (result) {
                this.list.remove(selectedForDeletion);
                DialogHandler.showMaterialAlert(this.mainContainer,
                        "Nota apagada",
                        "Nota apagada com sucesso.");
            } else {
                DialogHandler.showMaterialAlert(this.mainContainer,
                        "Cancelado",
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
        this.getCustomerNotes();
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
        selectedForEdit.setPersonType("customer");

        if (selectedForEdit == null) {
            DialogHandler.showErrorMessage("Nenhuma nota seleccionada",
                    "Por favor seleccione uma nota para editar.");
            return;
        }
        try {

            NoteAddController controller = new NoteAddController(this.customer.getId(), "customer");

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
            this.getCustomerNotes();

        } catch (IOException | SQLException ex) {
            Logger.getLogger(CustomerDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}