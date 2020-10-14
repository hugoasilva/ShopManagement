package pt.shop.management.ui.add.invoice;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
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
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.FileHandler;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.ui.alert.AlertMaker;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Invoice Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class InvoiceAddController implements Initializable {

    private final static String REMOTE_INVOICE_PATH = "/home/pi/gestao/faturas/";

    // UI Content
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

    // Database handler instance
    private DatabaseHandler databaseHandler;

    private String id;
    private String invoicePath;
    private Boolean isInEditMode = Boolean.FALSE;

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
    public void addInvoice(ActionEvent event) throws SftpException, JSchException {

        String invoiceId = String.valueOf(DatabaseHandler.getInvoiceId());
        this.id = invoiceId;
        String customerId = customer.getText();
        String employeeId = employee.getText();
        String invoiceDate = date.getValue().toString();
        String invoiceProducts = products.getText();
        String invoicePdf = REMOTE_INVOICE_PATH + DatabaseHandler.getInvoiceId() + ".pdf";

        if (customerId.isEmpty() || employeeId.isEmpty() || invoiceDate.isEmpty()
                || invoiceProducts.isEmpty() || invoicePdf.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.");
            return;
        }

        if (isInEditMode) {
            handleUpdateInvoice();
            return;
        }

        Invoice invoice = new Invoice(invoiceId, customerId, employeeId, invoiceDate, invoiceProducts, invoicePdf);

        FileHandler.uploadFile(this.invoicePath, DatabaseHandler.getInvoiceId() + ".pdf");

        if (DatabaseHandler.insertInvoice(invoice)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Nova fatura adicionada",
                    "Fatura nr " + invoiceId + " adicionada com sucesso.");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Erro ao adicionar fatura",
                    "Verifique todos os campos e tente novamente");
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
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success",
                    "Update complete");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed",
                    "Could not update data");
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
            // TODO
            // Handle no file selected message
            System.out.println("No file selected");
        } else {
            this.invoicePath = file.getAbsolutePath();
        }
    }
}