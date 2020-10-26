package pt.shop.management.data.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.model.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public final class DatabaseHandler {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    // Login query
    private static final String LOGIN_QUERY =
            "SELECT * FROM users WHERE username = ? and password = ?";

    // Customer select queries
    private static final String GET_CUSTOMER_ID_QUERY =
            "SELECT COUNT(*) FROM customers";
    private static final String GET_CUSTOMER_INVOICE_COUNT =
            "SELECT COUNT(*) FROM invoices WHERE customer_id=?";
    private static final String GET_CUSTOMERS_QUERY =
            "SELECT * FROM customers";
    private static final String SEARCH_CUSTOMERS_BY_ID_QUERY =
            "SELECT * FROM customers WHERE id LIKE ?";
    private static final String SEARCH_CUSTOMERS_BY_NAME_QUERY =
            "SELECT * FROM customers WHERE name LIKE ?";
    private static final String SEARCH_CUSTOMERS_BY_NIF_QUERY =
            "SELECT * FROM customers WHERE nif=?";
    private static final String SEARCH_CUSTOMERS_BY_PHONE_QUERY =
            "SELECT * FROM customers WHERE phone=?";
    private static final String SEARCH_CUSTOMERS_BY_EMAIL_QUERY =
            "SELECT * FROM customers WHERE email=?";

    // Employee select queries
    private static final String GET_EMPLOYEE_ID_QUERY =
            "SELECT COUNT(*) FROM employees";
    private static final String GET_EMPLOYEE_INVOICE_COUNT =
            "SELECT COUNT(*) FROM invoices WHERE employee_id=?";
    private static final String GET_EMPLOYEES_QUERY =
            "SELECT * FROM employees";
    private static final String SEARCH_EMPLOYEES_BY_ID_QUERY =
            "SELECT * FROM employees WHERE id LIKE ?";
    private static final String SEARCH_EMPLOYEES_BY_NAME_QUERY =
            "SELECT * FROM employees WHERE name LIKE ?";
    private static final String SEARCH_EMPLOYEES_BY_NIF_QUERY =
            "SELECT * FROM employees WHERE nif=?";
    private static final String SEARCH_EMPLOYEES_BY_PHONE_QUERY =
            "SELECT * FROM employees WHERE phone=?";
    private static final String SEARCH_EMPLOYEES_BY_EMAIL_QUERY =
            "SELECT * FROM employees WHERE email=?";

    // Invoice select queries
    private static final String GET_INVOICE_ID_QUERY =
            "SELECT COUNT(*) FROM invoices";
    private static final String GET_INVOICES_QUERY =
            "SELECT management.invoices.*" +
                    ", customers.name AS customer_name" +
                    ", employees.name AS employee_name " +
                    "FROM invoices " +
                    "INNER JOIN customers ON customers.id=invoices.customer_id " +
                    "INNER JOIN employees ON employees.id=invoices.employee_id";
    private static final String SEARCH_INVOICES_BY_ID_QUERY =
            "SELECT * FROM invoices WHERE id LIKE ?";
    private static final String SEARCH_INVOICES_BY_CUSTOMER_QUERY =
            "SELECT * FROM invoices WHERE customer_id=?";
    private static final String SEARCH_INVOICES_BY_EMPLOYEE_QUERY =
            "SELECT * FROM invoices WHERE employee_id=?";
    private static final String SEARCH_INVOICES_BY_DATE_QUERY =
            "SELECT * FROM invoices WHERE date=?";

    // Note select queries
    private static final String GET_CUSTOMER_NOTE_ID_QUERY =
            "SELECT COUNT(*) FROM notes_customers";
    private static final String GET_CUSTOMER_NOTES_QUERY =
            "SELECT * FROM notes_customers WHERE customer_id=?";
    private static final String GET_EMPLOYEE_NOTES_QUERY =
            "SELECT * FROM notes_employees WHERE employee_id=?";
    private static final String GET_EMPLOYEE_NOTE_ID_QUERY =
            "SELECT COUNT(*) FROM notes_employees";

    // Product select queries
    private static final String GET_PRODUCT_ID_QUERY =
            "SELECT COUNT(*) FROM products";
    private static final String GET_PRODUCTS_QUERY =
            "SELECT management.products.*" +
                    ", suppliers.name AS supplier_name " +
                    "FROM products " +
                    "INNER JOIN suppliers ON suppliers.id=products.supplier_id";
    private static final String SEARCH_PRODUCTS_BY_ID_QUERY =
            "SELECT * FROM products WHERE id LIKE ?";
    private static final String SEARCH_PRODUCTS_BY_NAME_QUERY =
            "SELECT * FROM products WHERE name LIKE ?";
    private static final String SEARCH_PRODUCTS_BY_PRICE_QUERY =
            "SELECT * FROM products WHERE price=?";
    private static final String SEARCH_PRODUCTS_BY_QUANTITY_QUERY =
            "SELECT * FROM products WHERE quantity=?";

    // Delete queries
    private static final String DELETE_CUSTOMER_QUERY =
            "DELETE FROM customers WHERE id = ?";
    private static final String DELETE_CUSTOMER_NOTE_QUERY =
            "DELETE FROM notes_customers WHERE id = ?";
    private static final String DELETE_EMPLOYEE_QUERY =
            "DELETE FROM employees WHERE id = ?";
    private static final String DELETE_EMPLOYEE_NOTE_QUERY =
            "DELETE FROM notes_employees WHERE id = ?";
    private static final String DELETE_INVOICE_QUERY =
            "DELETE FROM invoices WHERE id = ?";
    private static final String DELETE_PRODUCT_QUERY =
            "DELETE FROM products WHERE id = ?";

    // Insert queries
    private final static String INSERT_CUSTOMER_QUERY =
            "INSERT INTO customers (name, address, phone, email, nif) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_CUSTOMER_NOTE_QUERY =
            "INSERT INTO notes_customers (customer_id, message) VALUES (?, ?)";
    private static final String INSERT_EMPLOYEE_NOTE_QUERY =
            "INSERT INTO notes_employees (employee_id, message) VALUES (?, ?)";
    private final static String INSERT_EMPLOYEE_QUERY =
            "INSERT INTO employees (name, address, phone, email, nif) VALUES (?, ?, ?, ?, ?)";
    // TODO change invoice table structure (Remove products)
    private final static String INSERT_INVOICE_QUERY =
            "INSERT INTO invoices (customer_id, employee_id, date, pdf) VALUES (?, ?, ?, ?)";
    private final static String INSERT_PRODUCT_QUERY =
            "INSERT INTO products (name, price, supplier_id, quantity, image) VALUES (?, ?, ?, ?, ?)";

    // Update queries
    private static final String UPDATE_CUSTOMER_QUERY =
            "UPDATE customers SET name=?, address=?, phone=?, email=?, nif=? WHERE id=?";
    private static final String UPDATE_CUSTOMER_NOTE_QUERY =
            "UPDATE notes_customers SET message=? WHERE id=?";
    private static final String UPDATE_EMPLOYEE_QUERY =
            "UPDATE employees SET name=?, address=?, phone=?, email=?, nif=? WHERE id=?";
    private static final String UPDATE_EMPLOYEE_NOTE_QUERY =
            "UPDATE notes_employees SET message=? WHERE id=?";
    private static final String UPDATE_INVOICE_QUERY =
            "UPDATE invoices SET customer_id=?, employee_id=?, date=? WHERE id=?";
    private static final String UPDATE_PRODUCT_QUERY =
            "UPDATE products SET name=?, price=?, supplier_id=?, quantity=? WHERE id=?";

    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;

    /**
     * Log SQL Exception
     *
     * @param ex - SQLException object
     */
    public static void logSQLException(SQLException ex) {
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
     * Check login credentials
     *
     * @param username - username
     * @param password - password
     * @return - true if correct, false otherwise
     * @throws SQLException - SQL exception
     */
    public static boolean login(String username, String password) throws SQLException {
        try {
            DataSource ds = DatabasePool.getConnection();
            // Get connection
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(LOGIN_QUERY);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get new customer id
     *
     * @return new customer's id
     */
    public static int getCustomerId() throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_ID_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert new customer
     *
     * @param customer - customer object
     * @return - true if success, false otherwise
     */
    public static boolean insertCustomer(Customer customer) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getNif());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Delete customer from database
     *
     * @param customer - customer object
     * @return - true if success, false otherwise
     */
    public static boolean deleteCustomer(Customer customer) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Update customer data at database
     *
     * @param customer - customer object
     * @return - true if success, false otherwise
     */
    public static boolean updateCustomer(Customer customer) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getNif());
            preparedStatement.setString(6, customer.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get customer list from database
     *
     * @return - customer list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Customer> getCustomerList() throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String nif = resultSet.getString("nif");

                list.add(new Customer(id, name, address, phone, email, nif));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Search customer in database
     *
     * @param combo  - user selected input
     * @param search - user input
     * @return - customer search list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Customer> searchCustomer(String combo, String search) throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        String query = null;
        switch (combo) {
            case "ID ou Nome":
                if (search.matches("[0-9]+")) {
                    query = SEARCH_CUSTOMERS_BY_ID_QUERY;
                } else {
                    query = SEARCH_CUSTOMERS_BY_NAME_QUERY;
                }
                search = "%" + search + "%";
                break;
            case "NIF":
                query = SEARCH_CUSTOMERS_BY_NIF_QUERY;
                break;
            case "Contacto":
                query = SEARCH_CUSTOMERS_BY_PHONE_QUERY;
                break;
            case "E-mail":
                query = SEARCH_CUSTOMERS_BY_EMAIL_QUERY;
                break;
        }
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, search);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String nif = resultSet.getString("nif");

                list.add(new Customer(id, name, address, phone, email, nif));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Get new employee id
     *
     * @return new employee's id
     */
    public static int getEmployeeId() throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEE_ID_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert new employee
     *
     * @param employee - employee object
     * @return - true if success, false otherwise
     */
    public static boolean insertEmployee(Employee employee) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_QUERY);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getAddress());
            preparedStatement.setString(3, employee.getPhone());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getNif());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Delete employee from database
     *
     * @param employee - employee object
     * @return - true if success, false otherwise
     */
    public static boolean deleteEmployee(Employee employee) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_QUERY);
            preparedStatement.setString(1, employee.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Update employee data at database
     *
     * @param employee - employee object
     * @return - true if success, false otherwise
     */
    public static boolean updateEmployee(Employee employee) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE_QUERY);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getAddress());
            preparedStatement.setString(3, employee.getPhone());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getNif());
            preparedStatement.setString(6, employee.getId());
            return (preparedStatement.executeUpdate() > 0);
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get employee list from database
     *
     * @return - employee list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Employee> getEmployeeList() throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEES_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String nif = resultSet.getString("nif");

                list.add(new Employee(id, name, address, phone, email, nif));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Search employee in database
     *
     * @param combo  - user selected input
     * @param search - user input
     * @return - employee search list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Employee> searchEmployee(String combo, String search) throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String query = null;
        switch (combo) {
            case "ID ou Nome":
                if (search.matches("[0-9]+")) {
                    query = SEARCH_EMPLOYEES_BY_ID_QUERY;
                } else {
                    query = SEARCH_EMPLOYEES_BY_NAME_QUERY;
                }
                search = "%" + search + "%";
                break;
            case "NIF":
                query = SEARCH_EMPLOYEES_BY_NIF_QUERY;
                break;
            case "Contacto":
                query = SEARCH_EMPLOYEES_BY_PHONE_QUERY;
                break;
            case "E-mail":
                query = SEARCH_EMPLOYEES_BY_EMAIL_QUERY;
                break;
        }
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, search);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String nif = resultSet.getString("nif");

                list.add(new Employee(id, name, address, phone, email, nif));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Get new invoice id
     *
     * @return new invoice's id
     */
    public static int getInvoiceId() throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_INVOICE_ID_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert new invoice
     *
     * @param invoice - invoice object
     * @return - true if success, false otherwise
     */
    public static boolean insertInvoice(Invoice invoice) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_INVOICE_QUERY);
            preparedStatement.setString(1, invoice.getCustomerId());
            preparedStatement.setString(2, invoice.getEmployeeId());
            preparedStatement.setString(3, invoice.getDate());
            preparedStatement.setString(4, invoice.getPdf());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Delete invoice from database
     *
     * @param invoice - invoice object
     * @return - true if success, false otherwise
     */
    public static boolean deleteInvoice(Invoice invoice) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_INVOICE_QUERY);
            preparedStatement.setString(1, invoice.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get invoice list from database
     *
     * @return - invoice list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Invoice> getInvoiceList() throws SQLException {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_INVOICES_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String customerName = resultSet.getString("customer_name");
                String employeeName = resultSet.getString("employee_name");
                String date = resultSet.getString("date");
                String products = resultSet.getString("products");
                String pdf = resultSet.getString("pdf");

                Invoice invoice = new Invoice(id, customerName, employeeName, date, products, pdf);
                invoice.setCustomerName(customerName);
                invoice.setEmployeeName(employeeName);
                list.add(invoice);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Search invoice in database
     *
     * @param combo  - user selected input
     * @param search - user input
     * @return - invoice search list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Invoice> searchInvoice(String combo, String search) throws SQLException {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        String query = null;
        switch (combo) {
            case "ID":
                query = SEARCH_INVOICES_BY_ID_QUERY;
                search = "%" + search + "%";
                break;
            case "ID Cliente":
                query = SEARCH_INVOICES_BY_CUSTOMER_QUERY;
                break;
            case "ID Empregado":
                query = SEARCH_INVOICES_BY_EMPLOYEE_QUERY;
                break;
            case "Data":
                query = SEARCH_INVOICES_BY_DATE_QUERY;
                break;
        }
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, search);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id_fatura");
                String customerId = resultSet.getString("id_cliente");
                String employeeId = resultSet.getString("id_empregado");
                String date = resultSet.getString("data_fatura");
                String products = resultSet.getString("produtos");
                String pdf = resultSet.getString("pdf");

                list.add(new Invoice(id, customerId, employeeId, date, products, pdf));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Get customer note new id
     *
     * @return new customer note's id
     */
    public static int getCustomerNotesId() throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_NOTE_ID_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert new customer note
     *
     * @param note - note object
     * @return - true if success, false otherwise
     */
    public static boolean insertCustomerNote(Note note) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_NOTE_QUERY);
            preparedStatement.setString(1, note.getPersonId());
            preparedStatement.setString(2, note.getMessage());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Delete customer note from database
     *
     * @param note - note object
     * @return - true if success, false otherwise
     */
    public static boolean deleteCustomerNote(Note note) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_NOTE_QUERY);
            preparedStatement.setString(1, note.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Update customer note at database
     *
     * @param note - customer note object
     * @return - true if success, false otherwise
     */
    public static boolean updateCustomerNote(Note note) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_NOTE_QUERY);
            preparedStatement.setString(1, note.getMessage());
            preparedStatement.setString(2, note.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get customer notes list from database
     *
     * @param customer - customer object
     * @return - customer notes list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Note> getCustomerNotesList(Customer customer) throws SQLException {
        ObservableList<Note> list = FXCollections.observableArrayList();
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_NOTES_QUERY);
            preparedStatement.setString(1, customer.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String message = resultSet.getString("message");

                list.add(new Note(id, message));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Get employee note new id
     */
    public static int getEmployeeNotesId() throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEE_NOTE_ID_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert new employee note
     *
     * @param note - note object
     * @return - true if success, false otherwise
     */
    public static boolean insertEmployeeNote(Note note) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_NOTE_QUERY);
            preparedStatement.setString(1, note.getPersonId());
            preparedStatement.setString(2, note.getMessage());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Delete employee note from database
     *
     * @param note - note object
     * @return - true if success, false otherwise
     */
    public static boolean deleteEmployeeNote(Note note) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_NOTE_QUERY);
            preparedStatement.setString(1, note.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Update employee note at database
     *
     * @param note - employee note object
     * @return - true if success, false otherwise
     */
    public static boolean updateEmployeeNote(Note note) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE_NOTE_QUERY);
            preparedStatement.setString(1, note.getMessage());
            preparedStatement.setString(2, note.getId());
            return (preparedStatement.executeUpdate() > 0);
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get employee notes list from database
     *
     * @param employee - employee object
     * @return - employee notes list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Note> getEmployeeNotesList(Employee employee) throws SQLException {
        ObservableList<Note> list = FXCollections.observableArrayList();
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEE_NOTES_QUERY);
            preparedStatement.setString(1, employee.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String message = resultSet.getString("message");

                list.add(new Note(id, message));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Get new product id
     *
     * @return new product's id
     */
    public static int getProductId() throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_PRODUCT_ID_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Insert new product
     *
     * @param product - product object
     * @return - true if success, false otherwise
     */
    public static boolean insertProduct(Product product) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_PRODUCT_QUERY);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getPrice());
            preparedStatement.setString(3, product.getQuantity());
            preparedStatement.setString(4, product.getImage());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get product list from database
     *
     * @return - product notes list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Product> getProductList() throws SQLException {
        ObservableList<Product> list = FXCollections.observableArrayList();
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_PRODUCTS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String price = resultSet.getString("price");
                String supplierId = resultSet.getString("supplier_id");
                String supplierName = resultSet.getString("supplier_name");
                String quantity = resultSet.getString("quantity");
                String image = resultSet.getString("image");

                Product product = new Product(id, name, price, supplierId, quantity, image);
                product.setSupplierName(supplierName);
                list.add(product);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Search product in database
     *
     * @param combo  - user selected input
     * @param search - user input
     * @return - product search list
     * @throws SQLException - SQL exception
     */
    public static ObservableList<Product> searchProduct(String combo, String search) throws SQLException {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String query = null;
        switch (combo) {
            case "ID ou Nome":
                if (search.matches("[0-9]+")) {
                    query = SEARCH_PRODUCTS_BY_ID_QUERY;
                } else {
                    query = SEARCH_PRODUCTS_BY_NAME_QUERY;
                }
                search = "%" + search + "%";
                break;
            case "Preço":
                query = SEARCH_PRODUCTS_BY_PRICE_QUERY;
                break;
            case "Quantidade":
                query = SEARCH_PRODUCTS_BY_QUANTITY_QUERY;
                break;
        }
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, search);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String price = resultSet.getString("price");
                String supplierId = resultSet.getString("supplier_id");
                String quantity = resultSet.getString("quantity");
                String image = resultSet.getString("image");

                list.add(new Product(id, name, price, supplierId, quantity, image));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }

    /**
     * Delete product from database
     *
     * @param product - product object
     * @return - true if success, false otherwise
     */
    public static boolean deleteProduct(Product product) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_PRODUCT_QUERY);
            preparedStatement.setString(1, product.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Update product data at database
     *
     * @param product - product object
     * @return - true if success, false otherwise
     */
    public static boolean updateProduct(Product product) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_QUERY);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getPrice());
            preparedStatement.setString(3, product.getSupplierId());
            preparedStatement.setString(4, product.getQuantity());
            preparedStatement.setString(5, product.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Update invoice data at database
     *
     * @param invoice - invoice object
     * @return - true if success, false otherwise
     */
    public static boolean updateInvoice(Invoice invoice) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_INVOICE_QUERY);
            preparedStatement.setString(1, invoice.getCustomerId());
            preparedStatement.setString(2, invoice.getEmployeeId());
            preparedStatement.setString(3, invoice.getDate());
            preparedStatement.setString(4, invoice.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    /**
     * Get customer invoice count at database
     *
     * @param customer - customer object
     * @return - invoice count
     */
    public int getCustomerInvoiceCount(Customer customer) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_INVOICE_COUNT);
            preparedStatement.setString(1, customer.getId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return 0;
    }

    /**
     * Get employee invoice count at database
     *
     * @param employee - employee object
     * @return - invoice count
     */
    public int getEmployeeInvoiceCount(Employee employee) throws SQLException {
        try {
            DataSource dataSource = DatabasePool.getConnection();
            // Get connection
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEE_INVOICE_COUNT);
            preparedStatement.setString(1, employee.getId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return 0;
    }
}