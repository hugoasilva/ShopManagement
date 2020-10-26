package pt.shop.management.ui.main;

import com.jfoenix.controls.JFXTabPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class MainController implements Initializable {

    // Pane
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;

    // TabPane
    @FXML
    private JFXTabPane mainTabPane;

    // Customer tab
    @FXML
    private JFXTabPane customerTabPane;
    @FXML
    private Tab customerAddTab;
    @FXML
    private Tab customerSearchTab;

    // Employee tab
    @FXML
    private JFXTabPane employeeTabPane;
    @FXML
    private Tab employeeAddTab;
    @FXML
    private Tab employeeSearchTab;

    // Invoice tab
    @FXML
    private JFXTabPane invoiceTabPane;
    @FXML
    private Tab invoiceAddTab;
    @FXML
    private Tab invoiceSearchTab;

    // Product tab
    @FXML
    private JFXTabPane productTabPane;
    @FXML
    private Tab productAddTab;
    @FXML
    private Tab productSearchTab;

    // Supplier tab
    @FXML
    private JFXTabPane supplierTabPane;
    @FXML
    private Tab supplierAddTab;
    @FXML
    private Tab supplierSearchTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initComponents();
    }

    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    /**
     * Handle menu close item
     *
     * @param event - click event
     */
    @FXML
    private void handleMenuClose(ActionEvent event) {
        getStage().close();
    }

    /**
     * Handle menu about item
     *
     * @param event - click event
     */
    @FXML
    private void handleAboutMenu(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/about/About.fxml"), "Sobre", null);
    }

    /**
     * Handle menu full screen item
     *
     * @param event - click event
     */
    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    /**
     * Initialize tab panes
     */
    private void initComponents() {
        this.mainTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.mainTabPane.getTabs().size()).subtract(40));
        this.customerTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.customerTabPane.getTabs().size()).subtract(50));
        this.employeeTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.employeeTabPane.getTabs().size()).subtract(50));
        this.invoiceTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.invoiceTabPane.getTabs().size()).subtract(50));
        this.productTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.productTabPane.getTabs().size()).subtract(50));
        this.supplierTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.supplierTabPane.getTabs().size()).subtract(50));
        try {
            // Initialize customer tab content
            this.customerAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/customer/CustomerAdd.fxml")));
            this.customerSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/customer/CustomerSearch.fxml")));
            // Initialize employee tab content
            this.employeeAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/employee/EmployeeAdd.fxml")));
            this.employeeSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/employee/EmployeeSearch.fxml")));
            // Initialize invoice tab content
            this.invoiceAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/invoice/InvoiceAdd.fxml")));
            this.invoiceSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/invoice/InvoiceSearch.fxml")));
            // Initialize product tab content
            this.productAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/product/ProductAdd.fxml")));
            this.productSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/product/ProductSearch.fxml")));
            // Initialize supplier tab content
            this.supplierAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/supplier/SupplierAdd.fxml")));
            this.supplierSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/supplier/SupplierSearch.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
