package pt.shop.management.ui.main;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
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
 * @version 2020-10-23
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

    // Customer Tab
    @FXML
    private JFXTabPane customerTabPane;
    @FXML
    private Tab customerAddTab;
    @FXML
    private Tab customerSearchTab;

    // Employee Tab
    @FXML
    private JFXTabPane employeeTabPane;
    @FXML
    private Tab employeeAddTab;
    @FXML
    private Tab employeeSearchTab;

    // Invoice Tab
    @FXML
    private JFXTabPane invoiceTabPane;
    @FXML
    private Tab invoiceAddTab;
    @FXML
    private Tab invoiceSearchTab;

    // Product Tab Content
    @FXML
    private JFXTabPane productTabPane;
    @FXML
    private JFXComboBox<Label> productCombo;
    @FXML
    private TextField productSearchInput;
    @FXML
    private Tab productAddTab;
    @FXML
    private Tab productSearchTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComponents();
        this.initTabs();
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

    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    private void initCombos() {
        //

//
//        // Initialize product combo box
//        productCombo.getItems().addAll(new Label("ID"), new Label("Nome"),
//                new Label(new String("Preço".getBytes(), StandardCharsets.UTF_8)));
//        productCombo.setPromptText("Tipo de pesquisa...");
    }

    private void initComponents() {
        mainTabPane.tabMinWidthProperty().bind(
                rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
        customerTabPane.tabMinWidthProperty().bind(
                rootAnchorPane.widthProperty().divide(customerTabPane.getTabs().size()).subtract(15));
        employeeTabPane.tabMinWidthProperty().bind(
                rootAnchorPane.widthProperty().divide(employeeTabPane.getTabs().size()).subtract(15));
        invoiceTabPane.tabMinWidthProperty().bind(
                rootAnchorPane.widthProperty().divide(invoiceTabPane.getTabs().size()).subtract(15));
//        productTabPane.tabMinWidthProperty().bind(
//                rootAnchorPane.widthProperty().divide(productTabPane.getTabs().size()).subtract(15));
    }


    private void initTabs() {
        try {
            // Initialize customer tab content
            customerAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/customer/CustomerAdd.fxml")));
            customerSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/customer/CustomerSearch.fxml")));
            // Initialize employee tab content
            employeeAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/employee/EmployeeAdd.fxml")));
            employeeSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/employee/EmployeeSearch.fxml")));
            // Initialize invoice tab content
            invoiceAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/invoice/InvoiceAdd.fxml")));
            invoiceSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
                    "/fxml/invoice/InvoiceSearch.fxml")));
            // Initialize product tab content
//            productAddTab.setContent(FXMLLoader.load(this.getClass().getResource(
//                    "/fxml/product/ProductAdd.fxml")));
//            productSearchTab.setContent(FXMLLoader.load(this.getClass().getResource(
//                    "/fxml/product/ProductSearch.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
