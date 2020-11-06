package pt.hugoasilva.shopmanagement.ui.controller.product;

import com.jfoenix.controls.JFXButton;
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
import pt.hugoasilva.shopmanagement.data.database.DatabaseHandler;
import pt.hugoasilva.shopmanagement.data.model.Product;
import pt.hugoasilva.shopmanagement.ui.dialog.DialogHandler;
import pt.hugoasilva.shopmanagement.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Product Search Controller Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class ProductSearchController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(ProductSearchController.class.getName());
    // Product list object
    ObservableList<Product> list = FXCollections.observableArrayList();
    // UI Content
    @FXML
    private TextField idSearchInput;
    @FXML
    private TextField nameSearchInput;
    @FXML
    private TextField priceSearchInput;
    @FXML
    private TextField supplierSearchInput;
    @FXML
    private TextField quantitySearchInput;
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
    private StackPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initCol();
        this.loadData();
    }

    /**
     * Assign table columns to product properties
     */
    private void initCol() {
        this.tableView.setPlaceholder(new Label("Nenhum produto encontrado"));
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
                            private final JFXButton btn = new JFXButton();

                            {
                                FontIcon icon = new FontIcon("mdi-file-document");
                                icon.getStyleClass().add("font-icon-button");
                                icon.setIconSize(30);
                                btn.setGraphic(icon);
                                btn.setAlignment(Pos.CENTER);
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
        detailsCol.setPrefWidth(60);
        detailsCol.setStyle("-fx-alignment: CENTER;");
        detailsCol.setCellFactory(cellFactoryDetails);
        tableView.getColumns().add(detailsCol);
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

    /**
     * Get window stage
     *
     * @return window stage object
     */
    private Stage getStage() {
        return (Stage) this.tableView.getScene().getWindow();
    }

    /**
     * Load product table data
     */
    public void loadData() {
        this.list = DatabaseHandler.getProductList();
        this.tableView.setItems(this.list);
    }

    /**
     * Product delete handler
     *
     * @param event - delete event
     */
    @FXML
    private void handleProductDelete(ActionEvent event) {
        //Fetch the selected row
        Product selectedForDeletion = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer,
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
     * @param event refresh event
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        this.refreshTable();
    }

    public void refreshTable() {
        this.list.clear();
        this.list = DatabaseHandler.getProductList();
        this.tableView.setItems(list);
    }

    /**
     * Clear search filters handler
     */
    @FXML
    private void handleClearFilters(ActionEvent event) {
        this.loadData();
        this.idSearchInput.clear();
        this.nameSearchInput.clear();
        this.priceSearchInput.clear();
        this.supplierSearchInput.clear();
        this.quantitySearchInput.clear();
    }

    /**
     * Search product operation
     */
    public void searchProduct() {
        // Check if user input is present
        if (!this.idSearchInput.getText().isEmpty()
                || !this.nameSearchInput.getText().isEmpty()
                || !this.priceSearchInput.getText().isEmpty()
                || !this.supplierSearchInput.getText().isEmpty()
                || !this.quantitySearchInput.getText().isEmpty()) {
            String id = null;
            String name = null;
            String price = null;
            String supplier = null;
            String quantity = null;
            if (!this.idSearchInput.getText().isEmpty()) {
                id = this.idSearchInput.getText();
            }
            if (!this.nameSearchInput.getText().isEmpty()) {
                name = this.nameSearchInput.getText();
            }
            if (!this.priceSearchInput.getText().isEmpty()) {
                price = this.priceSearchInput.getText();
            }
            if (!this.supplierSearchInput.getText().isEmpty()) {
                supplier = this.supplierSearchInput.getText();
            }
            if (!this.quantitySearchInput.getText().isEmpty()) {
                quantity = this.quantitySearchInput.getText();
            }
            this.list = DatabaseHandler.searchProduct(id, name, price, supplier, quantity);
            this.tableView.setItems(this.list);
        } else {
            DialogHandler.showMaterialErrorDialog(this.mainContainer,
                    "Insira dados para pesquisar.");
        }
    }

    /**
     * Handle search product key press
     *
     * @param event key event
     */
    public void handleSearchProductKeyPress(KeyEvent event) {
        this.searchProduct();
    }

    /**
     * Handle search product key press
     *
     * @param event key event
     */
    public void handleSearchProductButtonPress(ActionEvent event) {
        this.searchProduct();
    }

    /**
     * Product edit handler
     *
     * @param event edit event
     */
    @FXML
    private void handleProductEdit(ActionEvent event) {
        //Fetch the selected row
        Product selectedForEdit = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            DialogHandler.showMaterialErrorDialog(this.mainContainer,
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
     * @param event close event
     */
    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }
}
