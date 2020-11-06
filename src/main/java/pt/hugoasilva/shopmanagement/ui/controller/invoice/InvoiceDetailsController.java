package pt.hugoasilva.shopmanagement.ui.controller.invoice;

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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.hugoasilva.shopmanagement.data.database.DatabaseHandler;
import pt.hugoasilva.shopmanagement.data.model.Invoice;
import pt.hugoasilva.shopmanagement.data.model.Product;
import pt.hugoasilva.shopmanagement.ui.controller.product.ProductAddController;
import pt.hugoasilva.shopmanagement.ui.dialog.DialogHandler;
import pt.hugoasilva.shopmanagement.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Invoice Details Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class InvoiceDetailsController implements Initializable {

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
    private StackPane mainContainer;

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
        this.tableView.setPlaceholder(new Label("Nenhum produto encontrado"));
        this.productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    /**
     * Cancel button handler
     *
     * @param event cancel event
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
        this.id.setText("Fatura nr: " + invoice.getId());
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

    public void handleRefresh(ActionEvent event) {
        this.getInvoiceProdutcs();
    }

    public void handleProductEdit(ActionEvent event) {
        //Fetch the selected row
        Product selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum produto seleccionado",
                    "Por favor seleccione um produto para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/product/ProductAdd.fxml"));
            Parent parent = loader.load();

            ProductAddController controller = loader.getController();

            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Produto");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.getInvoiceProdutcs();
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }

    public void handleProductDelete(ActionEvent event) {
        //Fetch the selected row
        Product selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum produto seleccionado",
                    "Por favor seleccione um produto para apagar.");
            return;
        }
        boolean option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Produto",
                "Tem a certeza que pretende apagar o produto?");
        if (option) {
            boolean result = DatabaseHandler.deleteInvoiceProduct(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Produto apagado",
                        "Produto apagado com sucesso.", false);
                this.list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Cancelado",
                        new String("Nenhuns dados serão apagados.".getBytes(), StandardCharsets.UTF_8),
                        false);
            }
        }
    }
}