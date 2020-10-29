package pt.shop.management.ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.javafx.FontIcon;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.data.model.Product;
import pt.shop.management.ui.material.DialogHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Employee Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class EmployeeSearchController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(EmployeeSearchController.class.getName());

    // Employee list object
    ObservableList<Employee> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private JFXComboBox<Label> employeeCombo;
    @FXML
    private TextField employeeSearchInput;
    @FXML
    private TableView<Employee> tableView;
    @FXML
    private TableColumn<Employee, String> idCol;
    @FXML
    private TableColumn<Employee, String> nameCol;
    @FXML
    private TableColumn<Employee, String> addressCol;
    @FXML
    private TableColumn<Employee, String> phoneCol;
    @FXML
    private TableColumn<Employee, String> emailCol;
    @FXML
    private TableColumn<Employee, String> nifCol;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
        this.initCombo();
        this.loadData();
    }

    /**
     * Assign table columns to employee properties
     */
    private void initCol() {
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        TableColumn<Employee, Void> detailsCol = new TableColumn<>("Ficha");

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                        return new TableCell<>() {
                            private final JFXButton btn = new JFXButton();

                            {
                                FontIcon icon = new FontIcon("mdi-file-document");
                                icon.setIconSize(30);
                                btn.setGraphic(icon);
                                btn.setAlignment(Pos.CENTER);
                                btn.setOnAction((ActionEvent event) -> {
                                    Employee employee = getTableView().getItems().get(getIndex());
                                    showEmployeeDetails(employee);
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
        detailsCol.setPrefWidth(60);
        detailsCol.setStyle("-fx-alignment: CENTER;");
        detailsCol.setCellFactory(cellFactory);
        this.tableView.getColumns().add(detailsCol);
    }

    /**
     * Assign table columns to employee properties
     */
    private void initCombo() {
        this.employeeCombo.getItems().addAll(new Label("ID ou Nome"), new Label("NIF"),
                new Label("Contacto"), new Label("E-mail"));
        this.employeeCombo.setPromptText("Tipo de pesquisa...");
    }

    /**
     * Show employee details window
     *
     * @param employee - employee object
     */
    private void showEmployeeDetails(Employee employee) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/employee/EmployeeDetails.fxml"));
            Parent parent = loader.load();

            EmployeeDetailsController controller = loader.getController();
            controller.inflateUI(employee);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Ficha de Empregado");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            ShopManagementUtil.setStageIcon(stage);
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load employee table data
     *
     * @ - database exception
     */
    public void loadData() {
        this.list = DatabaseHandler.getEmployeeList();
        this.tableView.setItems(list);
    }

    /**
     * Employee delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleEmployeeDelete(ActionEvent event) {
        //Fetch the selected row
        Employee selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum empregado seleccionado",
                    "Por favor seleccione um empregado para apagar.");
            return;
        }
        boolean option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Empregado",
                "Tem a certeza que pretende apagar o empregado " + selectedForDeletion.getName() + "?");
        if (option) {
            boolean result = DatabaseHandler.deleteEmployee(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Empregado apagado",
                        selectedForDeletion.getName() + " foi apagado com sucesso.", false);
                list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro!",
                        new String("Não foi possível apagar o empregado ".getBytes(), StandardCharsets.UTF_8) +
                                selectedForDeletion.getName(), false);
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
     * @ - database exception
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        this.refreshTable();
    }

    public void refreshTable() {
        if (this.employeeCombo.getValue() == null && this.employeeSearchInput.getText().isEmpty()) {
            this.list.clear();
            this.list = DatabaseHandler.getEmployeeList();
            this.tableView.setItems(list);
        } else {
            this.searchEmployee();
        }
    }

    /**
     * Search employee operation
     *
     * @ - SQL exception
     */
    public void searchEmployee() {
        // Check if user input is present
        if (this.employeeCombo.getSelectionModel().isEmpty() || this.employeeSearchInput.getText().isEmpty()) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Erro!",
                    "Insira dados em todos os campos.");
        } else {
            String comboInput = this.employeeCombo.getSelectionModel().getSelectedItem().getText();
            String searchInput = this.employeeSearchInput.getText();
            this.list = DatabaseHandler.searchEmployee(comboInput, searchInput);
            this.tableView.setItems(list);
        }
    }

    /**
     * Handle search employee key press
     *
     * @param event - key event
     * @ - SQL exception
     */
    public void handleSearchEmployeeKeyPress(KeyEvent event) {
        this.searchEmployee();
    }

    /**
     * Handle search employee button press
     *
     * @param event - button click event
     * @ - SQL exception
     */
    public void handleSearchEmployeeButtonPress(ActionEvent event) {
        this.searchEmployee();
    }

    /**
     * Employee edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleEmployeeEdit(ActionEvent event) {
        //Fetch the selected row
        Employee selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum empregado seleccionado",
                    "Por favor seleccione um empregado para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/employee/EmployeeAdd.fxml"));
            Parent parent = loader.load();

            EmployeeAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Empregado");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.refreshTable();

            stage.setOnHiding((e) -> this.handleRefresh(new ActionEvent()));
        } catch (IOException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
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

    /**
     * Invoice Add Controller Class
     *
     * @author Hugo Silva
     * @version 2020-10-25
     */

    public static class InvoiceAddController implements Initializable {

        // Logger
        private static final Logger LOGGER = LogManager.getLogger(InvoiceAddController.class.getName());
        // Directory paths
        private final static String REMOTE_INVOICE_PATH = "/home/pi/gestao/faturas/";
        // Invoice data
        private Invoice invoice;
        private String invoicePath;
        private Boolean isInEditMode = Boolean.FALSE;
        // UI content
        @FXML
        private JFXTextField customer;
        @FXML
        private JFXTextField employee;
        @FXML
        private JFXDatePicker date;
        @FXML
        private JFXTextField products;
        @FXML
        private JFXButton pdf;
        @FXML
        private StackPane rootPane;
        @FXML
        private AnchorPane mainContainer;

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
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }

        /**
         * Add invoice to table
         *
         * @param event - add invoice event
         */
        @FXML
        public void addInvoice(ActionEvent event) {
            String invoiceId = String.valueOf(DatabaseHandler.getInvoiceId());
            String customerId = customer.getText();
            String employeeId = employee.getText();
            String invoiceDate = date.getValue().toString();
            String invoiceProducts = products.getText();
            String invoicePdf = REMOTE_INVOICE_PATH + invoiceId + ".pdf";

            if (customerId.isEmpty() || employeeId.isEmpty() || invoiceDate.isEmpty()
                    || invoiceProducts.isEmpty()) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Dados insuficientes",
                        "Por favor insira dados em todos os campos.", false);
                return;
            }

            if (this.isInEditMode) {
                this.handleUpdateInvoice();
                return;
            }

            Invoice invoice = new Invoice(invoiceId, customerId, employeeId, invoiceDate, invoicePdf);
            if (DatabaseHandler.insertInvoice(invoice)) {
                SFTPHandler.uploadFile(this.invoicePath, invoicePdf);
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Nova fatura adicionada",
                        "Fatura nr " + invoiceId + " adicionada com sucesso.", false);
                clearEntries();
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro ao adicionar fatura",
                        "Verifique todos os campos e tente novamente", false);
            }
        }

        /**
         * Populate table
         *
         * @param invoice - invoice object
         */
        public void inflateUI(Invoice invoice) {
            this.customer.setText(invoice.getCustomerId());
            this.employee.setText(invoice.getEmployeeId());
            this.date.setValue(LocalDate.parse(invoice.getDate()));
            this.products.setText(invoice.getProducts());
            this.pdf.setText(invoice.getPdf());
            this.invoice = invoice;

            this.isInEditMode = Boolean.TRUE;
        }

        /**
         * Clear table entries
         */
        private void clearEntries() {
            this.customer.clear();
            this.employee.clear();
            this.products.clear();
        }

        /**
         * Handle invoice update
         */
        private void handleUpdateInvoice() {
            Invoice invoice = new Invoice(this.invoice.getId(), this.customer.getText(), this.employee.getText(),
                    this.date.getValue().toString(), this.pdf.getText());
            invoice.setProducts(products.getText());
            if (DatabaseHandler.updateInvoice(invoice)) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo!",
                        "Dados de fatura atualizados.", true);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro",
                        new String("Não foi possível atualizar os dados.".getBytes(),
                                StandardCharsets.UTF_8), false);
            }
        }

        public void chooseInvoice(ActionEvent actionEvent) {
            // Open File Chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir Fatura");
            fileChooser.getExtensionFilters().addAll(new
                    FileChooser.ExtensionFilter("Ficheiro PDF", "*.pdf"));
            Stage stage = new Stage(StageStyle.DECORATED);
            // Store File Path
            File file = fileChooser.showOpenDialog(stage);
            if (file == null) {
                LOGGER.log(Level.INFO, "No pdf file was selected.");
            } else {
                this.invoicePath = file.getAbsolutePath();
            }
        }
    }

    /**
     * Invoice Details Controller Class
     *
     * @author Hugo Silva
     * @version 2020-10-25
     */

    public static class InvoiceDetailsController implements Initializable {

        // Logger
        private static final Logger LOGGER = LogManager.getLogger(InvoiceDetailsController.class.getName());
        // Products list
        @FXML
        ObservableList<Product> list = FXCollections.observableArrayList();
        // Invoice data
        private Invoice invoice;
        // UI Content
        @FXML
        private Label id;
        @FXML
        private Label customer;
        @FXML
        private Label employee;
        @FXML
        private Label date;
        @FXML
        private TableView<Product> tableView;
        @FXML
        private TableColumn<Product, String> productIdCol;
        @FXML
        private TableColumn<Product, String> productNameCol;
        @FXML
        private TableColumn<Product, String> productPriceCol;
        @FXML
        private TableColumn<Product, String> productQuantityCol;
        @FXML
        private StackPane rootPane;
        @FXML
        private AnchorPane mainContainer;

        public InvoiceDetailsController() {
        }

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            this.initCol();
        }

        /**
         * Init invoice products table columns
         */
        private void initCol() {
            this.tableView.setPlaceholder(new Label("Nenhum produto adicionado"));
            this.productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            this.productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            this.productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        }

        /**
         * Cancel button handler
         *
         * @param event - cancel event
         */
        @FXML
        private void cancel(ActionEvent event) {
            Stage stage = (Stage) customer.getScene().getWindow();
            stage.close();
        }

        /**
         * Populate table
         *
         * @param invoice - invoice object
         */
        public void inflateUI(Invoice invoice) {
            this.id.setText("ID: " + invoice.getId());
            this.customer.setText("Cliente: " + invoice.getCustomerId());
            this.employee.setText("Empregado: " + invoice.getEmployeeId());
            this.date.setText("Data: " + invoice.getDate());
            this.invoice = invoice;
            this.getInvoiceProdutcs();
        }

        private void getInvoiceProdutcs() {
            this.list.clear();
            this.list = DatabaseHandler.getInvoiceProductList(this.invoice);
            this.tableView.setItems(list);
        }

        // TODO
        public void handleRefresh(ActionEvent event) {
        }

        // TODO
        public void handleProductEdit(ActionEvent event) {
        }

        // TODO
        public void handleProductDelete(ActionEvent event) {
        }
    }

    /**
     * Invoice Search Controller Class
     *
     * @author Hugo Silva
     * @version 2020-10-25
     */

    public static class InvoiceSearchController implements Initializable {

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
            this.productsCol.setCellValueFactory(new PropertyValueFactory<>("products"));
            TableColumn<Invoice, Void> pdfCol = new TableColumn<>("PDF");
            Callback<TableColumn<Invoice, Void>, TableCell<Invoice, Void>> cellFactoryPDF =
                    new Callback<>() {
                        @Override
                        public TableCell<Invoice, Void> call(final TableColumn<Invoice, Void> param) {
                            return new TableCell<>() {
                                private final Button btn = new Button();

                                {
                                    FontIcon icon = new FontIcon("mdi-file-pdf");
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
                                private final Button btn = new Button();

                                {
                                    FontIcon icon = new FontIcon("mdi-file-document");
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
            SFTPHandler.downloadFile(pdfPath, fileName);

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

        private Stage getStage() {
            return (Stage) this.tableView.getScene().getWindow();
        }

        /**
         * Load invoice search table data
         *
         * @ - database exception
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
         * @ - database exception
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
         *
         * @ - SQL exception
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
         * @param event - key event
         * @ - SQL exception
         */
        public void handleSearchInvoiceKeyPress(KeyEvent event) {
            this.searchInvoice();
        }

        /**
         * Handle search invoice key press
         *
         * @param event - key event
         * @ - SQL exception
         */
        public void handleSearchInvoiceButtonPress(ActionEvent event) {
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
         * @param event - close event
         */
        @FXML
        private void closeStage(ActionEvent event) {
            getStage().close();
        }
    }
}
