package pt.hugoasilva.shopmanagement.ui.controller.main;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class MainController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(MainController.class.getName());

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

    // Task tab
    @FXML
    private JFXTabPane taskTabPane;
    @FXML
    private Tab taskAddTab;
    @FXML
    private Tab taskSearchTab;

    // Work tab
    @FXML
    private JFXTabPane workTabPane;
    @FXML
    private Tab workAddTab;
    @FXML
    private Tab workSearchTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initComponents();
    }

    /**
     * Get window stage
     *
     * @return window stage object
     */
    private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }

    /**
     * Initialize tab panes
     */
    private void initComponents() {
        this.mainTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.mainTabPane.getTabs().size()).subtract(22));
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
        this.taskTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.taskTabPane.getTabs().size()).subtract(50));
        this.workTabPane.tabMinWidthProperty().bind(
                this.rootAnchorPane.widthProperty().divide(
                        this.workTabPane.getTabs().size()).subtract(50));
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
            // TODO Task and Work Tabs
//            // Initialize task tab content
//            this.taskAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
//                    "/fxml/task/TaskAdd.fxml")));
//            this.taskSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
//                    "/fxml/task/TaskSearch.fxml")));
//            // Initialize work tab content
//            this.workAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
//                    "/fxml/work/WorkAdd.fxml")));
//            this.workSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
//                    "/fxml/work/WorkSearch.fxml")));
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", "IO Exception: " + ex.getMessage());
        }
    }
}
