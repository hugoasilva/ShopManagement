package pt.shop.management.ui.add.invoice;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.ui.alert.AlertMaker;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Invoice Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class InvoiceAddController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(InvoiceAddController.class.getName());
    // Directory paths
    private final static String REMOTE_INVOICE_PATH = "/home/pi/gestao/faturas/";
    // Database handler instance
    private DatabaseHandler databaseHandler;
    // Invoice data
    private String id;
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
        databaseHandler = DatabaseHandler.getInstance();
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
        this.id = invoiceId;
        String customerId = customer.getText();
        String employeeId = employee.getText();
        String invoiceDate = date.getValue().toString();
        String invoiceProducts = products.getText();
        String invoicePdf = REMOTE_INVOICE_PATH + this.id + ".pdf";

        if (customerId.isEmpty() || employeeId.isEmpty() || invoiceDate.isEmpty()
                || invoiceProducts.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (isInEditMode) {
            handleUpdateInvoice();
            return;
        }

        Invoice invoice = new Invoice(invoiceId, customerId,
                employeeId, invoiceDate, invoiceProducts, invoicePdf);
        if (DatabaseHandler.insertInvoice(invoice)) {
            SFTPHandler.uploadFile(this.invoicePath, invoicePdf);
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Nova fatura adicionada",
                    "Fatura nr " + invoiceId + " adicionada com sucesso.", false);
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Erro ao adicionar fatura",
                    "Verifique todos os campos e tente novamente", false);
        }
    }

    /**
     * Populate table
     *
     * @param invoice - invoice object
     */
    public void inflateUI(Invoice invoice) {
        customer.setText(invoice.getCustomerId());
        employee.setText(invoice.getEmployeeId());
        date.setValue(LocalDate.parse(invoice.getDate()));
        products.setText(invoice.getProducts());
        pdf.setText(invoice.getPdf());

        isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear table entries
     */
    private void clearEntries() {
        customer.clear();
        employee.clear();
        products.clear();
    }

    /**
     * Handle invoice update
     */
    private void handleUpdateInvoice() {
        Invoice invoice = new Invoice(id, customer.getText(), employee.getText(),
                date.getValue().toString(), products.getText(), pdf.getText());
        if (databaseHandler.updateInvoice(invoice)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Successo!",
                    "Dados de fatura atualizados.", false);
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed",
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