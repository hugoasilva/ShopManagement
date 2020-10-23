package pt.shop.management.ui.main.toolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pt.shop.management.util.ShopManagementUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Toolbar Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class ToolbarController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Load add customer window
     *
     * @param event - click event
     */
    @FXML
    private void loadAddCustomer(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/customer/CustomerAdd.fxml"), "Adicionar Cliente", null);
    }

    /**
     * Load add invoice window
     *
     * @param event - click event
     */
    @FXML
    private void loadAddInvoice(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/invoice/InvoiceAdd.fxml"), "Adicionar Fatura", null);
    }

    /**
     * Load add employee window
     *
     * @param event - click event
     */
    @FXML
    private void loadAddEmployee(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/employee/EmployeeAdd.fxml"), "Adicionar Empregado", null);
    }

    /**
     * Load customer table window
     *
     * @param event - click event
     */
    @FXML
    private void loadCustomerTable(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/customer/CustomerList.fxml"), "Lista de Clientes", null);
    }

    /**
     * Load invoice table window
     *
     * @param event - click event
     */
    @FXML
    private void loadInvoiceTable(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/invoice/InvoiceList.fxml"), "Lista de Faturas", null);
    }

    /**
     * Load employee table window
     *
     * @param event - click event
     */
    @FXML
    private void loadEmployeeTable(ActionEvent event) {
        ShopManagementUtil.loadWindow(getClass().getResource(
                "/fxml/employee/EmployeeList.fxml"), "Lista de Empregados", null);
    }
}
