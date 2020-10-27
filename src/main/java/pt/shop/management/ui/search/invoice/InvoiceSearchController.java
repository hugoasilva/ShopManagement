package pt.shop.management.ui.search.invoice;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.ui.add.invoice.InvoiceAddController;
import pt.shop.management.ui.alert.AlertMaker;
import pt.shop.management.ui.details.invoice.InvoiceDetailsController;
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
 * Invoice Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class InvoiceSearchController implements Initializable {

    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";
    // Invoice list object
    ObservableList<Invoice> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private JFXComboBox<Label> invoiceCombo;
    @FXML
    private TextField invoiceSearchInput;
    @FXML
    private JFXDatePicker initDate;
    @FXML
    private JFXDatePicker finalDate;
    @FXML
    private TableView<Invoice> tableView;
    @FXML
    private TableColumn<Invoice, String> idCol;
    @FXML
    private TableColumn<Invoice, String> customerCol;
    @FXML
    private TableColumn<Invoice, String> employeeCol;
    @FXML
    private TableColumn<Invoice, String> dateCol;
    @FXML
    private TableColumn<Invoice, String> productsCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
        this.initCombo();
        try {
            this.loadData();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Assign table columns to invoice properties
     */
    private void initCol() {
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        this.employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.productsCol.setCellValueFactory(new PropertyValueFactory<>("products"));
        TableColumn<Invoice, Void> pdfCol = new TableColumn<>("PDF");
        Callback<TableColumn<Invoice, Void>, TableCell<Invoice, Void>> cellFactoryPDF =
                new Callback<>() {
                    @Override
                    public TableCell<Invoice, Void> call(final TableColumn<Invoice, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir PDF");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Invoice data = getTableView().getItems().get(getIndex());
                                    showInvoicePDF(data.getId(), data.getPdf());
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                };
        TableColumn<Invoice, Void> detailsCol = new TableColumn<>("Ficha");
        Callback<TableColumn<Invoice, Void>, TableCell<Invoice, Void>> cellFactoryDetails =
                new Callback<>() {
                    @Override
                    public TableCell<Invoice, Void> call(final TableColumn<Invoice, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Invoice invoice = getTableView().getItems().get(getIndex());
                                    try {
                                        showInvoiceDetails(invoice);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                    }
                };
        pdfCol.setPrefWidth(80);
        pdfCol.setCellFactory(cellFactoryPDF);
        detailsCol.setPrefWidth(80);
        detailsCol.setCellFactory(cellFactoryDetails);
        this.tableView.getColumns().add(pdfCol);
        this.tableView.getColumns().add(detailsCol);
    }

    /**
     * Initialize search combo box
     */
    private void initCombo() {
        this.invoiceCombo.getItems().addAll(new Label("ID"), new Label("ID Cliente"),
                new Label("ID Empregado"), new Label("Data"));
        this.invoiceCombo.setPromptText("Tipo de pesquisa...");
    }

    /**
     * Show Invoice PDF
     *
     * @param pdfPath - invoice pdf file path
     */
    private void showInvoicePDF(String id, String pdfPath) {
        String fileName = id + ".pdf";
        SFTPHandler.downloadFile(pdfPath, fileName);

        // Open file
        ShopManagementUtil.openFile(LOCAL_DOWNLOAD_PATH + fileName);
    }

    private void showInvoiceDetails(Invoice invoice) throws IOException {
        InvoiceDetailsController controller = new InvoiceDetailsController(invoice);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/invoice/InvoiceDetails.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Ficha de Fatura");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
    }

    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load invoice search table data
     *
     * @throws SQLException - database exception
     */
    public void loadData() throws SQLException {
        this.list = DatabaseHandler.getInvoiceList();
        this.tableView.setItems(list);
    }

    /**
     * Invoice delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleInvoiceDelete(ActionEvent event) throws SQLException {
        //Fetch the selected row
        Invoice selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("Nenhuma fatura seleccionada",
                    "Por favor seleccione uma fatura para apagar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Apagar Fatura");
        alert.setContentText("Tem a certeza que pretende apagar a fatura nr " + selectedForDeletion.getId() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            boolean result = DatabaseHandler.deleteInvoice(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Fatura apagada", "Fatura nr " + selectedForDeletion.getId() +
                        " apagada com sucesso.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Erro!",
                        new String("Não foi possível apagar a fatura nr ".getBytes(), StandardCharsets.UTF_8)
                                + selectedForDeletion.getId());
            }
        } else {
            AlertMaker.showSimpleAlert("Cancelado",
                    new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8));
        }
    }

    /**
     * Refresh handler
     *
     * @param event - refresh event
     * @throws SQLException - database exception
     */
    @FXML
    private void handleRefresh(ActionEvent event) throws SQLException {
        this.refreshTable();
    }

    public void refreshTable() throws SQLException {
        if (this.invoiceCombo.getValue() == null && this.invoiceSearchInput.getText().isEmpty()) {
            this.list.clear();
            this.list = DatabaseHandler.getInvoiceList();
            this.tableView.setItems(list);
        } else {
            this.searchInvoice();
        }
    }

    /**
     * Search invoice operation
     *
     * @throws SQLException - SQL exception
     */
    public void searchInvoice() throws SQLException {
        // Check if user input is present
        if (this.invoiceCombo.getSelectionModel().isEmpty() || this.invoiceSearchInput.getText().isEmpty()) {
            AlertMaker.showErrorMessage("Erro!",
                    "Insira dados em todos os campos.");
        } else {
            System.out.println(this.initDate.getValue().toString());
            System.out.println(this.finalDate.getValue().toString());
            String comboInput = this.invoiceCombo.getSelectionModel().getSelectedItem().getText();
            String searchInput = this.invoiceSearchInput.getText();
            this.list = DatabaseHandler.searchInvoice(comboInput, searchInput);
            this.tableView.setItems(list);
        }
    }

    /**
     * Handle search invoice key press
     *
     * @param event - key event
     * @throws SQLException - SQL exception
     */
    public void handleSearchInvoiceKeyPress(KeyEvent event) throws SQLException {
        this.searchInvoice();
    }

    /**
     * Handle search invoice key press
     *
     * @param event - key event
     * @throws SQLException - SQL exception
     */
    public void handleSearchInvoiceButtonPress(ActionEvent event) throws SQLException {
        this.searchInvoice();
    }

    /**
     * Invoice edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleInvoiceEdit(ActionEvent event) {
        //Fetch the selected row
        Invoice selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("Nenhuma fatura seleccionada",
                    "Por favor seleccione uma fatura para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/invoice/InvoiceAdd.fxml"));
            Parent parent = loader.load();

            InvoiceAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Fatura");
            stage.setScene(new Scene(parent));
            stage.show();
            ShopManagementUtil.setStageIcon(stage);

            stage.setOnHiding((e) -> {
                try {
                    handleRefresh(new ActionEvent());
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(InvoiceSearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Close current window
     *
     * @param event - close event
     */
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }
}
