package pt.shop.management.ui.main;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.shop.management.ui.search.customer.CustomerSearchController;
import pt.shop.management.ui.search.employee.EmployeeSearchController;
import pt.shop.management.ui.search.invoice.InvoiceSearchController;
import pt.shop.management.util.ShopManagementUtil;
import pt.shop.management.ui.main.toolbar.ToolbarController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class MainController implements Initializable {

    // Pane
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchorPane;

    // Hamburger menu
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;

    // Tab
    @FXML
    private JFXTabPane mainTabPane;

    // Customer Tab Content
    @FXML
    private JFXComboBox<Label> customerCombo;
    @FXML
    private TextField customerSearchInput;

    // Employee Tab Content
    @FXML
    private JFXComboBox<Label> employeeCombo;
    @FXML
    private TextField employeeSearchInput;

    // Invoice Tab Content
    @FXML
    private JFXComboBox<Label> invoiceCombo;
    @FXML
    private TextField invoiceSearchInput;

    // Product Tab Content
    @FXML
    private JFXComboBox<Label> productCombo;
    @FXML
    private TextField productSearchInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDrawer();
        initComponents();
        try {
            initCombos();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void initCombos() throws UnsupportedEncodingException {
        // Initialize customer combo box
        customerCombo.getItems().addAll(new Label("ID"), new Label("Nome"),
                new Label("NIF"), new Label("Contacto", new Label("E-mail")));
        customerCombo.setPromptText("Pesquisar por...");

        // Initialize employee combo box
        employeeCombo.getItems().addAll(new Label("ID"), new Label("Nome"), new Label("NIF"),
                new Label("Contacto"), new Label("E-mail"));
        employeeCombo.setPromptText("Pesquisar por...");

        // Initialize invoice combo box
        invoiceCombo.getItems().addAll(new Label("ID"), new Label("ID Cliente"),
                new Label("ID Empregado"), new Label("Data"));
        invoiceCombo.setPromptText("Pesquisar por...");

        // Initialize product combo box
        productCombo.getItems().addAll(new Label("ID"), new Label("Nome"),
                new Label(new String("Preço".getBytes(), "UTF-8")));
        productCombo.setPromptText("Pesquisar por...");
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
     * Handle menu add invoice item
     *
     * @param event - click event
     */
    @FXML
    private void handleMenuAddInvoice(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/invoice/InvoiceAdd.fxml"), "Adicionar Fatura", null);
    }

    /**
     * Handle menu add customer item
     *
     * @param event - click event
     */
    @FXML
    private void handleMenuAddCustomer(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/customer/CustomerAdd.fxml"), "Adicionar Cliente", null);
    }

    /**
     * Handle menu view invoice list item
     *
     * @param event - click event
     */
    @FXML
    private void handleMenuViewInvoiceList(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/invoice/InvoiceList.fxml"), "Lista de Faturas", null);
    }

    /**
     * Handle menu view customer list item
     *
     * @param event - click event
     */
    @FXML
    private void handleMenuViewCustomerList(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/customer/CustomerList.fxml"), "Lista de Clientes", null);
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
     * Handle menu settings item
     *
     * @param event - click event
     */
    @FXML
    private void handleMenuSettings(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/shop/management/ui/settings/settings.fxml"), "Definições", null);
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    /**
     * Setup hamburger menu
     */
    private void initDrawer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/main/toolbar/Toolbar.fxml"));
            VBox toolbar = loader.load();
            drawer.setSidePane(toolbar);
            ToolbarController controller = loader.getController();
            //controller.setBookReturnCallback(this);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            drawer.toggle();
        });
        drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.toFront();
        });
        drawer.setOnDrawerClosed((event) -> {
            drawer.toBack();
            task.setRate(task.getRate() * -1);
            task.play();
        });
    }

    private void initComponents() {
        mainTabPane.tabMinWidthProperty().bind(
                rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
    }

    /**
     * Search customer operation
     *
     * @throws IOException - IO exception
     */
    public void searchCustomer() throws IOException {
        String comboInput = customerCombo.getSelectionModel().getSelectedItem().getText();
        String searchInput = customerSearchInput.getText();
        CustomerSearchController controller = new CustomerSearchController(comboInput, searchInput);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/customer/CustomerSearch.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Pesquisa de Clientes");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
    }

    /**
     * Handle search customer key press
     *
     * @param event - key event
     * @throws IOException - IO exception
     */
    public void handleSearchCustomerKeyPress(KeyEvent event) throws IOException {
        this.searchCustomer();
    }

    /**
     * Handle search customer button press
     *
     * @param event - button click event
     * @throws IOException - IO exception
     */
    public void handleSearchCustomerButtonPress(ActionEvent event) throws IOException {
        this.searchCustomer();
    }

    /**
     * Search invoice operation
     *
     * @throws IOException - IO exception
     */
    public void searchInvoice() throws IOException {
        String comboInput = invoiceCombo.getSelectionModel().getSelectedItem().getText();
        String searchInput = invoiceSearchInput.getText();
        InvoiceSearchController controller = new InvoiceSearchController(comboInput, searchInput);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/invoice/InvoiceSearch.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Pesquisa de Empregados");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
    }

    /**
     * Handle search invoice key press
     *
     * @param event - key event
     * @throws IOException - IO exception
     */
    public void handleSearchInvoiceKeyPress(KeyEvent event) throws IOException {
        this.searchInvoice();
    }

    /**
     * Handle search invoice key press
     *
     * @param event - key event
     * @throws IOException - IO exception
     */
    public void handleSearchInvoiceButtonPress(ActionEvent event) throws IOException {
        this.searchInvoice();
    }

    /**
     * Search employee operation
     *
     * @throws IOException - IO exception
     */
    public void searchEmployee() throws IOException {
        String comboInput = employeeCombo.getSelectionModel().getSelectedItem().getText();
        String searchInput = employeeSearchInput.getText();
        EmployeeSearchController controller = new EmployeeSearchController(comboInput, searchInput);

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "/fxml/employee/EmployeeSearch.fxml"));
        loader.setController(controller);

        Parent parent = loader.load();

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Pesquisa de Empregados");
        stage.setScene(new Scene(parent));
        stage.show();
        ShopManagementUtil.setStageIcon(stage);
    }

    /**
     * Handle search employee key press
     *
     * @param event - key event
     * @throws IOException - IO exception
     */
    public void handleSearchEmployeeKeyPress(KeyEvent event) throws IOException {
        this.searchEmployee();
    }

    /**
     * Handle search employee key press
     *
     * @param event - key event
     * @throws IOException - IO exception
     */
    public void handleSearchEmployeeButtonPress(ActionEvent event) throws IOException {
        this.searchEmployee();
    }

    public void handleSearchProductButtonPress(ActionEvent event) {
    }

    public void handleSearchProductKeyPress(KeyEvent keyEvent) {
    }
}
