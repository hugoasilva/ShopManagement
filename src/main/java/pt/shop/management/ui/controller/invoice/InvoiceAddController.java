package pt.shop.management.ui.controller.invoice;

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
import pt.shop.management.data.model.Invoice;
import pt.shop.management.ui.dialog.DialogHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Invoice Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-29
 */

public class InvoiceAddController implements Initializable {

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
            ShopManagementUtil.uploadFile(this.invoicePath, invoicePdf);
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