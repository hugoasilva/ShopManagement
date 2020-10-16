package pt.shop.management.data.database;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.data.model.Product;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * Database Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public final class DatabaseHandler {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    // Database details
    private static final String DATABASE_SERVER_URL = "jdbc:mysql://projecthub.hopto.org:3306/gestao" +
            "?useTimezone=true&serverTimezone=UTC";
    private static final String DATABASE_USERNAME = "admin";
    private static final String DATABASE_PASSWORD = "dbpw";

    // Select Queries
    private static final String GET_CUSTOMER_ID_QUERY = "SELECT COUNT(*) FROM clientes";
    private static final String GET_EMPLOYEE_ID_QUERY = "SELECT COUNT(*) FROM empregados";
    private static final String GET_INVOICE_ID_QUERY = "SELECT COUNT(*) FROM faturas";
    private static final String GET_PRODUCT_ID_QUERY = "SELECT COUNT(*) FROM produtos";
    private static final String GET_CUSTOMER_INVOICE_COUNT = "SELECT COUNT(*) FROM faturas WHERE id_cliente=?";
    private static final String GET_EMPLOYEE_INVOICE_COUNT = "SELECT COUNT(*) FROM faturas WHERE id_empregado=?";

    // Delete Queries
    private static final String DELETE_CUSTOMER_QUERY = "DELETE FROM clientes WHERE id_cliente = ?";
    private static final String DELETE_EMPLOYEE_QUERY = "DELETE FROM empregados WHERE id_empregado = ?";
    private static final String DELETE_INVOICE_QUERY = "DELETE FROM faturas WHERE id_fatura = ?";
    private static final String DELETE_PRODUCT_QUERY = "DELETE FROM produtos WHERE id_produto = ?";

    // Insert Queries
    private final static String INSERT_CUSTOMER_QUERY = "INSERT INTO clientes " +
            "(nome, morada, contacto, email, nif, notas) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String INSERT_EMPLOYEE_QUERY = "INSERT INTO empregados " +
            "(nome, morada, contacto, email, nif, notas) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String INSERT_INVOICE_QUERY = "INSERT INTO faturas " +
            "(id_cliente, id_empregado, data_fatura, produtos, pdf) VALUES (?, ?, ?, ?, ?)";
    private final static String INSERT_PRODUCT_QUERY = "INSERT INTO produtos " +
            "(nome, quantidade) VALUES (?, ?)";

    // Update Queries
    private static final String UPDATE_CUSTOMER_QUERY = "UPDATE clientes SET ?=? WHERE id_cliente=?";
    private static final String UPDATE_EMPLOYEE_QUERY = "UPDATE empregados SET ?=? WHERE id_empregado=?";
    private static final String UPDATE_INVOICE_QUERY = "UPDATE faturas SET ?=? WHERE id_fatura=?";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE produtos SET ?=? WHERE id_produto=?";
    private static final Statement stmt = null;
    private static DatabaseHandler handler = null;
    private static Connection conn = null;

    static {
        try {
            createConnection();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private DatabaseHandler() {
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    /**
     * Create database connection
     */
    private static void createConnection() throws UnsupportedEncodingException {
        try {
            conn = DriverManager.getConnection(DATABASE_SERVER_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        } catch (SQLException e) {
            printSQLException(e);
            JOptionPane.showMessageDialog(null,
                    (new String("Não foi possível aceder à base de dados".getBytes(), StandardCharsets.UTF_8)),
                    "Erro de Base de Dados", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Log SQL Exception
     *
     * @param ex - SQLException object
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                LOGGER.log(Level.ERROR, "{}", "SQLState: " + ((SQLException) e).getSQLState());
                LOGGER.log(Level.ERROR, "{}", "Error Code: " + ((SQLException) e).getErrorCode());
                LOGGER.log(Level.ERROR, "{}", "Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    LOGGER.log(Level.ERROR, "{}", "Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DatabaseHandler.getInstance();
    }

    /**
     * Get customer count at database
     *
     * @return new customer's id
     */
    public static int getCustomerId() {
        ResultSet rs;
        try {
            PreparedStatement stmt = conn.prepareStatement(GET_CUSTOMER_ID_QUERY);
            rs = stmt.executeQuery();
            //Retrieving the result
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException ex) {
            printSQLException(ex);
            return 0;
        }
    }

    /**
     * Insert new customer
     *
     * @param customer - customer object
     * @return - true if success, false otherwise
     */
    public static boolean insertCustomer(Customer customer) {
        try {
            PreparedStatement statement = conn.prepareStatement(INSERT_CUSTOMER_QUERY);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());
            statement.setString(5, customer.getNif());
            statement.setString(6, customer.getNotes());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Get employee count at database
     *
     * @return new employee's id
     */
    public static int getEmployeeId() {
        ResultSet rs;
        try {
            PreparedStatement stmt = conn.prepareStatement(GET_EMPLOYEE_ID_QUERY);
            rs = stmt.executeQuery();
            //Retrieving the result
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException ex) {
            printSQLException(ex);
            return 0;
        }
    }

    /**
     * Insert new employee
     *
     * @param employee - employee object
     * @return - true if success, false otherwise
     */
    public static boolean insertEmployee(Employee employee) {
        try {
            PreparedStatement statement = conn.prepareStatement(INSERT_EMPLOYEE_QUERY);
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getAddress());
            statement.setString(3, employee.getPhone());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getNif());
            statement.setString(6, employee.getNotes());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Get invoice count in database
     *
     * @return new invoice's id
     */
    public static int getInvoiceId() {
        ResultSet rs;
        try {
            PreparedStatement stmt = conn.prepareStatement(GET_INVOICE_ID_QUERY);
            rs = stmt.executeQuery();
            //Retrieving the result
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException ex) {
            printSQLException(ex);
            return 0;
        }
    }

    /**
     * Insert new invoice
     *
     * @param invoice - invoice object
     * @return - true if success, false otherwise
     */
    public static boolean insertInvoice(Invoice invoice) {
        try {
            PreparedStatement statement = conn.prepareStatement(INSERT_INVOICE_QUERY);
            statement.setString(1, invoice.getCustomerId());
            statement.setString(2, invoice.getEmployeeId());
            statement.setString(3, invoice.getDate());
            statement.setString(4, invoice.getProducts());
            statement.setString(5, invoice.getPdf());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Get product count in database
     *
     * @return new product's id
     */
    public static int getProductId() {
        ResultSet rs;
        try {
            PreparedStatement stmt = conn.prepareStatement(GET_PRODUCT_ID_QUERY);
            rs = stmt.executeQuery();
            //Retrieving the result
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException ex) {
            printSQLException(ex);
            return 0;
        }
    }

    /**
     * Insert new product
     *
     * @param product - product object
     * @return - true if success, false otherwise
     */
    public static boolean insertProduct(Product product) {
        try {
            PreparedStatement statement = conn.prepareStatement(INSERT_PRODUCT_QUERY);
            statement.setString(1, product.getName());
            statement.setString(2, product.getPrice());
            statement.setString(3, product.getQuantity());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Delete customer from database
     *
     * @param customer - customer object
     * @return - true if success, false otherwise
     */
    public boolean deleteCustomer(Customer customer) {
        try {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CUSTOMER_QUERY);
            stmt.setString(1, customer.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Update customer data at database
     *
     * @param customer - customer object
     * @return - true if success, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        try {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CUSTOMER_QUERY);
            // TODO Update changed fields
//            stmt.setString(1, customer.getName());
//            stmt.setString(2, customer.getAddress());
//            stmt.setString(3, customer.getPhone());
//            stmt.setString(4, customer.getEmail());
//            stmt.setString(5, customer.getNif());
//            stmt.setString(6, customer.getNotes());
//            stmt.setString(7, customer.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Delete employee from database
     *
     * @param employee - employee object
     * @return - true if success, false otherwise
     */
    public boolean deleteEmployee(Employee employee) {
        try {
            PreparedStatement stmt = conn.prepareStatement(DELETE_EMPLOYEE_QUERY);
            stmt.setString(1, employee.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Update employee data at database
     *
     * @param employee - customer object
     * @return - true if success, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        try {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_EMPLOYEE_QUERY);
            // TODO Update changed fields
//            stmt.setString(1, employee.getName());
//            stmt.setString(2, employee.getAddress());
//            stmt.setString(3, employee.getPhone());
//            stmt.setString(4, employee.getEmail());
//            stmt.setString(5, employee.getNif());
//            stmt.setString(6, employee.getNotes());
//            stmt.setString(7, employee.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Delete invoice from database
     *
     * @param invoice - invoice object
     * @return - true if success, false otherwise
     */
    public boolean deleteInvoice(Invoice invoice) {
        try {
            PreparedStatement stmt = conn.prepareStatement(DELETE_INVOICE_QUERY);
            stmt.setString(1, invoice.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Update invoice data at database
     *
     * @param invoice - invoice object
     * @return - true if success, false otherwise
     */
    public boolean updateInvoice(Invoice invoice) {
        try {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_INVOICE_QUERY);
            // TODO Update changed fields
//            stmt.setString(1, invoice.getCustomerId());
//            stmt.setString(2, invoice.getEmployeeId());
//            stmt.setString(3, invoice.getDate());
//            stmt.setString(4, invoice.getProducts());
//            stmt.setString(5, invoice.getPdf());
//            stmt.setString(6, invoice.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Delete product from database
     *
     * @param product - product object
     * @return - true if success, false otherwise
     */
    public boolean deleteProduct(Product product) {
        try {
            PreparedStatement stmt = conn.prepareStatement(DELETE_PRODUCT_QUERY);
            stmt.setString(1, product.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Update product data at database
     *
     * @param product - product object
     * @return - true if success, false otherwise
     */
    public boolean updateProduct(Product product) {
        try {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PRODUCT_QUERY);
            // TODO Update changed fields
//            stmt.setString(1, product.getName());
//            stmt.setString(2, product.getQuantity());
//            stmt.setString(6, product.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Get customer invoice count at database
     *
     * @param customer - customer object
     * @return - invoice count
     */
    public boolean getCustomerInvoiceCount(Customer customer) {
        try {
            PreparedStatement stmt = conn.prepareStatement(GET_CUSTOMER_INVOICE_COUNT);
            stmt.setString(1, customer.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return (count > 0);
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Get employee invoice count at database
     *
     * @param employee - employee object
     * @return - invoice count
     */
    public boolean getEmployeeInvoiceCount(Employee employee) {
        try {
            PreparedStatement stmt = conn.prepareStatement(GET_EMPLOYEE_INVOICE_COUNT);
            stmt.setString(1, employee.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return (count > 0);
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return false;
    }

    /**
     * Get database connection
     *
     * @return - database connection
     */
    public Connection getConnection() {
        return conn;
    }
}
