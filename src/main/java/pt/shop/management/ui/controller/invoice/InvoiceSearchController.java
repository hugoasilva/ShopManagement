package pt.shop.management.ui.controller.invoice;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.javafx.FontIcon;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.ui.dialog.DialogHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Invoice Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class InvoiceSearchController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(InvoiceSearchController.class.getName());

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
    @FXML
    private StackPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
        this.initCombo();
        this.loadData();
    }

    /**
     * Assign table columns to invoice properties
     */
    private void initCol() {
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        this.employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.productsCol.setText("Produtos???"); //setCellValueFactory(new PropertyValueFactory<>("products"));
        TableColumn<Invoice, Void> pdfCol = new TableColumn<>("PDF");
        Callback<TableColumn<Invoice, Void>, TableCell<Invoice, Void>> cellFactoryPDF =
                new Callback<>() {
                    @Override
                    public TableCell<Invoice, Void> call(final TableColumn<Invoice, Void> param) {
                        return new TableCell<>() {
                            private final JFXButton btn = new JFXButton();

                            {
                                FontIcon icon = new FontIcon("mdi-file-pdf");
                                icon.getStyleClass().add("font-icon-button");
                                icon.setIconSize(30);
                                btn.setGraphic(icon);
                                btn.setAlignment(Pos.CENTER);
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
                            private final JFXButton btn = new JFXButton();

                            {
                                FontIcon icon = new FontIcon("mdi-file-document");
                                icon.getStyleClass().add("font-icon-button");
                                icon.setIconSize(30);
                                btn.setGraphic(icon);
                                btn.setAlignment(Pos.CENTER);
                                btn.setOnAction((ActionEvent event) -> {
                                    Invoice invoice = getTableView().getItems().get(getIndex());
                                    showInvoiceDetails(invoice);
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
        ShopManagementUtil.downloadFile(pdfPath, fileName);

        // Open file
        ShopManagementUtil.openFile(LOCAL_DOWNLOAD_PATH + fileName);
    }

    private void showInvoiceDetails(Invoice invoice) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/invoice/InvoiceDetails.fxml"));
            Parent parent = loader.load();

            InvoiceDetailsController controller = loader.getController();
            controller.inflateUI(invoice);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Ficha de Fatura");
            stage.setScene(new Scene(parent));
            stage.show();
            ShopManagementUtil.setStageIcon(stage);
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Get window stage
     *
     * @return window stage object
     */
    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load invoice search table data
     */
    public void loadData() {
        this.list = DatabaseHandler.getInvoiceList();
        this.tableView.setItems(list);
    }

    /**
     * Invoice delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleInvoiceDelete(ActionEvent event) {
        //Fetch the selected row
        Invoice selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhuma fatura seleccionada",
                    "Por favor seleccione uma fatura para apagar.");
            return;
        }
        boolean option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Fatura",
                "Tem a certeza que pretende apagar a fatura nr " + selectedForDeletion.getId() + "?");
        if (option) {
            boolean result = DatabaseHandler.deleteInvoice(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer,
                        "Fatura apagada", "Fatura nr " + selectedForDeletion.getId() +
                                " apagada com sucesso.", false);
                list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro!",
                        new String("Não foi possível apagar a fatura nr ".getBytes(), StandardCharsets.UTF_8)
                                + selectedForDeletion.getId(), false);
            }
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Cancelado",
                    new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8), false);
        }
    }

    /**
     * Refresh handler
     *
     * @param event - refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        this.refreshTable();
    }

    public void refreshTable() {
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
     */
    public void searchInvoice() {
        // Check if user input is present
        if (this.invoiceCombo.getSelectionModel().isEmpty() || this.invoiceSearchInput.getText().isEmpty()) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Erro!",
                    "Insira dados em todos os campos.");
        } else {
            // TODO Pesquisa por data
            if (this.initDate.getValue() != null) {
                // TODO Data selecionada até hoje
                System.out.println(this.initDate.getValue().toString());
                if (this.finalDate.getValue() != null) {
                    // TODO Entre as datas seleccionadas
                    System.out.println(this.finalDate.getValue().toString());
                }
            }
            String comboInput = this.invoiceCombo.getSelectionModel().getSelectedItem().getText();
            String searchInput = this.invoiceSearchInput.getText();
            this.list = DatabaseHandler.searchInvoice(comboInput, searchInput);
            this.tableView.setItems(list);
        }
    }

    /**
     * Handle search invoice key press
     *
     * @param event key event
     */
    public void handleSearchInvoiceKeyPress(KeyEvent event) {
        this.searchInvoice();
    }

    /**
     * Handle search invoice key press
     *
     * @param event key event
     */
    public void handleSearchInvoiceButtonPress(ActionEvent event) {
        this.searchInvoice();
    }

    /**
     * Invoice edit handler
     *
     * @param event edit event
     */
    @FXML
    private void handleInvoiceEdit(ActionEvent event) {
        //Fetch the selected row
        Invoice selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhuma fatura seleccionada",
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

            stage.setOnHiding((e) -> handleRefresh(new ActionEvent()));
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    /**
     * Close current window
     *
     * @param event close event
     */
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }
}