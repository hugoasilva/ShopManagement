package pt.shop.management.data.database;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.model.Customer;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Invoice;
import pt.shop.management.data.model.Product;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * Database Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public final class DatabaseHandler {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    // Database details
    private static final String DATABASE_SERVER_URL =
            "jdbc:mysql://projecthub.hopto.org:3306/management?useTimezone=true&serverTimezone=UTC";
    private static final String DATABASE_USERNAME = "admin";
    private static final String DATABASE_PASSWORD = "dbpw";

    // Select Queries
    private static final String GET_CUSTOMER_ID_QUERY = "SELECT COUNT(*) FROM customers";
    private static final String GET_EMPLOYEE_ID_QUERY = "SELECT COUNT(*) FROM employees";
    private static final String GET_INVOICE_ID_QUERY = "SELECT COUNT(*) FROM invoices";
    private static final String GET_PRODUCT_ID_QUERY = "SELECT COUNT(*) FROM products";
    private static final String GET_CUSTOMER_INVOICE_COUNT = "SELECT COUNT(*) FROM invoices WHERE customer_id=?";
    private static final String GET_EMPLOYEE_INVOICE_COUNT = "SELECT COUNT(*) FROM invoices WHERE employee_id=?";

    // Delete Queries
    private static final String DELETE_CUSTOMER_QUERY = "DELETE FROM customers WHERE id = ?";
    private static final String DELETE_EMPLOYEE_QUERY = "DELETE FROM employees WHERE id = ?";
    private static final String DELETE_INVOICE_QUERY = "DELETE FROM invoices WHERE id = ?";
    private static final String DELETE_PRODUCT_QUERY = "DELETE FROM products WHERE id = ?";

    // Insert Queries
    private final static String INSERT_CUSTOMER_QUERY = "INSERT INTO customers " +
            "(name, address, phone, email, nif) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String INSERT_EMPLOYEE_QUERY = "INSERT INTO employees " +
            "(name, address, phone, email, nif) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String INSERT_INVOICE_QUERY = "INSERT INTO invoices " +
            "(customer_id, employee_id, date, products, pdf) VALUES (?, ?, ?, ?, ?)";
    private final static String INSERT_PRODUCT_QUERY = "INSERT INTO products " +
            "(name, price, quantity, image) VALUES (?, ?, ?, ?)";

    // Update Queries
    private static final String UPDATE_CUSTOMER_QUERY =
            "UPDATE customers SET name=?, address=?, phone=?, email=?, nif=? WHERE id=?";
    private static final String UPDATE_EMPLOYEE_QUERY =
            "UPDATE employees SET name=?, address=?, phone=?, email=?, nif=? WHERE id=?";
    private static final String UPDATE_INVOICE_QUERY = "UPDATE invoices SET ?=? WHERE id=?";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE products SET ?=? WHERE id=?";

    // Database instances
    private static DatabaseHandler handler = null;
    private static Connection conn = null;

    /**
     * Constructor
     */
    private DatabaseHandler() {
        createConnection();
    }

    /**
     * Main function
     */
    public static void main(String[] args) {
        DatabaseHandler.getInstance();
    }

    /**
     * Initialize database handler instance
     *
     * @return - database handler instance
     */
    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    /**
     * Create database connection
     */
    private static void createConnection() {
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

    /**
     * Get customer count at database
     *
     * @return new customer's id
     */
    public static int getCustomerId() {
        ResultSet rs;
        try {
            PreparedStatement statement = conn.prepareStatement(GET_CUSTOMER_ID_QUERY);
            rs = statement.executeQuery();
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
            PreparedStatement statement = conn.prepareStatement(GET_EMPLOYEE_ID_QUERY);
            rs = statement.executeQuery();
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
            PreparedStatement statement = conn.prepareStatement(GET_INVOICE_ID_QUERY);
            rs = statement.executeQuery();
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
            PreparedStatement statement = conn.prepareStatement(GET_PRODUCT_ID_QUERY);
            rs = statement.executeQuery();
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
            statement.setString(4, product.getImage());

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
            PreparedStatement statement = conn.prepareStatement(DELETE_CUSTOMER_QUERY);
            statement.setString(1, customer.getId());
            int res = statement.executeUpdate();
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
            PreparedStatement statement = conn.prepareStatement(UPDATE_CUSTOMER_QUERY);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());
            statement.setString(5, customer.getNif());
            statement.setString(6, customer.getId());

            return (statement.executeUpdate() > 0);
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
            PreparedStatement statement = conn.prepareStatement(DELETE_EMPLOYEE_QUERY);
            statement.setString(1, employee.getId());
            int res = statement.executeUpdate();
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
     * @param employee - employee object
     * @return - true if success, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        try {
            PreparedStatement statement = conn.prepareStatement(UPDATE_EMPLOYEE_QUERY);
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getAddress());
            statement.setString(3, employee.getPhone());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getNif());
            statement.setString(6, employee.getId());
            int res = statement.executeUpdate();
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
            PreparedStatement statement = conn.prepareStatement(DELETE_INVOICE_QUERY);
            statement.setString(1, invoice.getId());
            int res = statement.executeUpdate();
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
            PreparedStatement statement = conn.prepareStatement(UPDATE_INVOICE_QUERY);
            statement.setString(1, invoice.getCustomerId());
            statement.setString(2, invoice.getEmployeeId());
            statement.setString(3, invoice.getDate());
            statement.setString(4, invoice.getProducts());
            statement.setString(5, invoice.getPdf());
            statement.setString(6, invoice.getId());
            int res = statement.executeUpdate();
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
            PreparedStatement statement = conn.prepareStatement(DELETE_PRODUCT_QUERY);
            statement.setString(1, product.getId());
            int res = statement.executeUpdate();
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
            PreparedStatement statement = conn.prepareStatement(UPDATE_PRODUCT_QUERY);
            // TODO Update changed fields
            statement.setString(1, product.getName());
            statement.setString(2, product.getPrice());
            statement.setString(3, product.getQuantity());
            statement.setString(4, product.getId());
            int res = statement.executeUpdate();
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
            PreparedStatement statement = conn.prepareStatement(GET_CUSTOMER_INVOICE_COUNT);
            statement.setString(1, customer.getId());
            ResultSet rs = statement.executeQuery();
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
            PreparedStatement statement = conn.prepareStatement(GET_EMPLOYEE_INVOICE_COUNT);
            statement.setString(1, employee.getId());
            ResultSet rs = statement.executeQuery();
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
