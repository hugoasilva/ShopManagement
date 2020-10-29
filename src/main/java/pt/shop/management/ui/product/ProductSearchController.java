package pt.shop.management.ui.product;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Product;
import pt.shop.management.ui.material.DialogHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Product Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class ProductSearchController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(ProductSearchController.class.getName());
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";
    // Product list object
    ObservableList<Product> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private JFXComboBox<Label> productCombo;
    @FXML
    private TextField productSearchInput;
    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Product, String> idCol;
    @FXML
    private TableColumn<Product, String> nameCol;
    @FXML
    private TableColumn<Product, String> priceCol;
    @FXML
    private TableColumn<Product, String> supplierCol;
    @FXML
    private TableColumn<Product, String> quantityCol;
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
     * Assign table columns to product properties
     */
    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Product, Void> detailsCol = new TableColumn<>("Ficha");
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactoryDetails =
                new Callback<>() {
                    @Override
                    public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                        return new TableCell<>() {
                            private final Button btn = new Button("Abrir Ficha");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Product product = getTableView().getItems().get(getIndex());
                                    showProductDetails(product);
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
        detailsCol.setPrefWidth(80);
        detailsCol.setCellFactory(cellFactoryDetails);
        tableView.getColumns().add(detailsCol);
    }

    /**
     * Initialize search combo box
     */
    private void initCombo() {
        productCombo.getItems().addAll(new Label("ID ou Nome"),
                new Label(new String("Preço".getBytes(), StandardCharsets.UTF_8)), new Label("Quantidade"));
        productCombo.setPromptText("Tipo de pesquisa...");
    }

    private void showProductDetails(Product product) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/fxml/product/ProductDetails.fxml"));
            Parent parent = loader.load();

            ProductDetailsController controller = loader.getController();
            controller.inflateUI(product);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Ficha de Produto");
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
     * Load product table data
     *
     * @ - database exception
     */
    public void loadData() {
        this.list = DatabaseHandler.getProductList();
        tableView.setItems(list);
    }

    /**
     * Product delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleProductDelete(ActionEvent event) {
        //Fetch the selected row
        Product selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum produto seleccionado",
                    "Por favor seleccione um produto para apagar.");
            return;
        }
        boolean option = DialogHandler.showMaterialConfirmationDialog(this.mainContainer,
                "Apagar Produto",
                "Tem a certeza que pretende apagar o produto " + selectedForDeletion.getName() + "?");
        if (option) {
            boolean result = DatabaseHandler.deleteProduct(selectedForDeletion);
            if (result) {
                DialogHandler.showMaterialInformationDialog(this.mainContainer,
                        "Produto apagado", "Produto nr " + selectedForDeletion.getId() +
                                " apagado com sucesso.", false);
                list.remove(selectedForDeletion);
            } else {
                DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro!",
                        new String("Não foi possível apagar o produto nr ".getBytes(), StandardCharsets.UTF_8)
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
        if (this.productCombo.getValue() == null && this.productSearchInput.getText().isEmpty()) {
            this.list.clear();
            this.list = DatabaseHandler.getProductList();
            this.tableView.setItems(list);
        } else {
            this.searchProduct();
        }
    }

    /**
     * Search product operation
     *
     * @ - SQL exception
     */
    public void searchProduct() {
        // Check if user input is present
        if (this.productCombo.getSelectionModel().isEmpty() || this.productSearchInput.getText().isEmpty()) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Erro!",
                    "Insira dados em todos os campos.");
        } else {
            String comboInput =
                    StringUtils.stripAccents(this.productCombo.getSelectionModel().getSelectedItem().getText());
            String searchInput = this.productSearchInput.getText();
            this.list = DatabaseHandler.searchProduct(comboInput, searchInput);
            this.tableView.setItems(list);
        }
    }

    /**
     * Handle search product key press
     *
     * @param event - key event
     * @ - SQL exception
     */
    public void handleSearchProductKeyPress(KeyEvent event) {
        this.searchProduct();
    }

    /**
     * Handle search product key press
     *
     * @param event - key event
     * @ - SQL exception
     */
    public void handleSearchProductButtonPress(ActionEvent event) {
        this.searchProduct();
    }

    /**
     * Product edit handler
     *
     * @param event - edit event
     */
    @FXML
    private void handleProductEdit(ActionEvent event) {
        //Fetch the selected row
        Product selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer, "Nenhum produto seleccionado",
                    "Por favor seleccione um produto para editar.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/product/ProductAdd.fxml"));
            Parent parent = loader.load();

            ProductAddController controller = loader.getController();
            controller.inflateUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Editar Produto");
            stage.setScene(new Scene(parent));
            ShopManagementUtil.setStageIcon(stage);
            stage.showAndWait();
            this.refreshTable();

            stage.setOnHiding((e) -> this.handleRefresh(new ActionEvent()));
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
