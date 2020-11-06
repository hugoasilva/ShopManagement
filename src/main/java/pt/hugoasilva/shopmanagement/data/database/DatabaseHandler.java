package pt.hugoasilva.shopmanagement.data.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.hugoasilva.shopmanagement.data.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Handler Class
 *
 * @author Hugo Silva
 * @version 2020-11-05
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
            "SELECT * FROM customers ";

    // Employee select queries
    private static final String GET_EMPLOYEE_ID_QUERY =
            "SELECT COUNT(*) FROM employees";
    private static final String GET_EMPLOYEE_INVOICE_COUNT =
            "SELECT COUNT(*) FROM invoices WHERE employee_id=?";
    private static final String GET_EMPLOYEES_QUERY =
            "SELECT * FROM employees ";

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

    private static final String SEARCH_INVOICES_QUERY =
            "SELECT management.invoices.*" +
                    ", customers.name AS customer_name" +
                    ", employees.name AS employee_name " +
                    "FROM invoices ";

    // Note select queries
    private static final String GET_CUSTOMER_NOTE_ID_QUERY =
            "SELECT COUNT(*) FROM notes_customers";
    private static final String GET_CUSTOMER_NOTES_QUERY =
            "SELECT * FROM notes_customers WHERE customer_id=?";
    private static final String GET_EMPLOYEE_NOTE_ID_QUERY =
            "SELECT COUNT(*) FROM notes_employees";
    private static final String GET_EMPLOYEE_NOTES_QUERY =
            "SELECT * FROM notes_employees WHERE employee_id=?";
    private static final String GET_SUPPLIER_NOTE_ID_QUERY =
            "SELECT COUNT(*) FROM notes_suppliers";
    private static final String GET_SUPPLIER_NOTES_QUERY =
            "SELECT * FROM notes_suppliers WHERE supplier_id=?";

    // Product select queries
    private static final String GET_PRODUCT_INVOICE_ID_QUERY =
            "SELECT COUNT(*) FROM products_invoices";
    private static final String GET_PRODUCT_INVOICE_QUERY =
            "SELECT management.products_invoices.* " +
                    ", products.name " +
                    ", products.supplier_id " +
                    ", products.price " +
                    ", products.image " +
                    "FROM products_invoices " +
                    "INNER JOIN products ON products.id = products_invoices.product_id " +
                    "WHERE invoice_id=?";
    private static final String GET_PRODUCT_ID_QUERY =
            "SELECT COUNT(*) FROM products";
    private static final String GET_PRODUCTS_QUERY =
            "SELECT management.products.*" +
                    ", suppliers.name AS supplier_name " +
                    "FROM products " +
                    "INNER JOIN suppliers ON suppliers.id=products.supplier_id";
    private static final String SEARCH_PRODUCTS_QUERY =
            "SELECT management.products.*" +
                    ", suppliers.name AS supplier_name " +
                    "FROM products ";

    // Supplier select queries
    private static final String GET_SUPPLIER_ID_QUERY =
            "SELECT COUNT(*) FROM suppliers";
    private static final String GET_SUPPLIERS_QUERY =
            "SELECT * FROM suppliers ";

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
    // TODO Fix product delete
    private static final String DELETE_INVOICE_PRODUCT_QUERY =
            "DELETE FROM products_invoices WHERE id = ?";
    private static final String DELETE_PRODUCT_QUERY =
            "DELETE FROM products WHERE id = ?";
    private static final String DELETE_SUPPLIER_QUERY =
            "DELETE FROM suppliers WHERE id = ?";
    private static final String DELETE_SUPPLIER_NOTE_QUERY =
            "DELETE FROM notes_customers WHERE id = ?";

    // Insert queries
    private final static String INSERT_CUSTOMER_QUERY =
            "INSERT INTO customers (name, address, phone, email, nif) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_CUSTOMER_NOTE_QUERY =
            "INSERT INTO notes_customers (customer_id, message) VALUES (?, ?)";
    private final static String INSERT_EMPLOYEE_QUERY =
            "INSERT INTO employees (name, address, phone, email, nif) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_EMPLOYEE_NOTE_QUERY =
            "INSERT INTO notes_employees (employee_id, message) VALUES (?, ?)";
    private final static String INSERT_INVOICE_QUERY =
            "INSERT INTO invoices (customer_id, employee_id, date, pdf) VALUES (?, ?, ?, ?)";
    private final static String INSERT_PRODUCT_QUERY =
            "INSERT INTO products (name, price, supplier_id, quantity, image) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_PRODUCT_INVOICE_QUERY =
            "INSERT INTO products_invoices (invoice_id, product_id, quantity) VALUES (?, ?, ?)";
    private final static String INSERT_SUPPLIER_QUERY =
            "INSERT INTO suppliers (name, address, phone, email, nif) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_SUPPLIER_NOTE_QUERY =
            "INSERT INTO notes_suppliers (supplier_id, message) VALUES (?, ?)";

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
    private static final String UPDATE_PRODUCT_INVOICE_QUERY =
            "UPDATE products_invoices SET product_id=?, quantity=? WHERE id=?";
    private static final String UPDATE_SUPPLIER_QUERY =
            "UPDATE suppliers SET name=?, address=?, phone=?, email=?, nif=? WHERE id=?";
    private static final String UPDATE_SUPPLIER_NOTE_QUERY =
            "UPDATE notes_suppliers SET message=? WHERE id=?";

    /**
     * Log SQL Exception
     *
     * @param ex SQLException object
     */
    public static void logSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
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
     * @param username username
     * @param password password
     * @return true if correct, false otherwise
     */
    public static String login(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(LOGIN_QUERY);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return "success";
            } else {
                return "error";
            }
        } catch (SQLException ex) {
            logSQLException(ex);
            return "dberror";
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    /**
     * Get new customer id
     *
     * @return new customer's id
     */
    public static int getCustomerId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    /**
     * Insert new customer
     *
     * @param customer customer object
     * @return true if success, false otherwise
     */
    public static boolean insertCustomer(Customer customer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getNif());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Delete customer from database
     *
     * @param customer customer object
     * @return true if success, false otherwise
     */
    public static boolean deleteCustomer(Customer customer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Update customer data at database
     *
     * @param customer customer object
     * @return true if success, false otherwise
     */
    public static boolean updateCustomer(Customer customer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getNif());
            preparedStatement.setString(6, customer.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Get customer list from database
     *
     * @return customer list
     */
    public static ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMERS_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
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
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Search customer in database
     *
     * @param id      search by id
     * @param name    search by name
     * @param address search by address
     * @param phone   search by phone
     * @param email   search by email
     * @param nif     search by nif
     * @return customer search list
     */
    public static ObservableList<Customer> searchCustomer(String id, String name,
                                                          String address, String phone,
                                                          String email, String nif) {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        String query = GET_CUSTOMERS_QUERY + "WHERE ";
        boolean and = false;
        if (id != null) {
            query += "id LIKE '%" + id + "%'";
            and = true;
        }
        if (name != null) {
            if (and) {
                query += "AND name LIKE '%" + name + "%'";
            } else {
                query += "name LIKE '%" + name + "%'";
                and = true;
            }
        }
        if (address != null) {
            if (and) {
                query += "AND address LIKE '%" + address + "%'";
            } else {
                query += "address LIKE '%" + address + "%'";
                and = true;
            }
        }
        if (phone != null) {
            if (and) {
                query += "AND phone LIKE '%" + phone + "%'";
            } else {
                query += "phone LIKE '%" + phone + "%'";
                and = true;
            }
        }
        if (email != null) {
            if (and) {
                query += "AND email LIKE '%" + email + "%'";
            } else {
                query += "email LIKE '%" + email + "%'";
                and = true;
            }
        }
        if (nif != null) {
            if (and) {
                query += "AND nif LIKE '%" + nif + "%'";
            } else {
                query += "nif LIKE '%" + nif + "%'";
            }
        }

        System.out.println(query);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String customerId = resultSet.getString("id");
                String customerName = resultSet.getString("name");
                String customerAddress = resultSet.getString("address");
                String customerPhone = resultSet.getString("phone");
                String customerEmail = resultSet.getString("email");
                String customerNif = resultSet.getString("nif");

                Customer customer = new Customer(customerId, customerName,
                        customerAddress, customerPhone, customerEmail, customerNif);
                list.add(customer);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Get new employee id
     *
     * @return new employee's id
     */
    public static int getEmployeeId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEE_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    /**
     * Insert new employee
     *
     * @param employee employee object
     * @return true if success, false otherwise
     */
    public static boolean insertEmployee(Employee employee) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_QUERY);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getAddress());
            preparedStatement.setString(3, employee.getPhone());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getNif());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Delete employee from database
     *
     * @param employee employee object
     * @return true if success, false otherwise
     */
    public static boolean deleteEmployee(Employee employee) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_QUERY);
            preparedStatement.setString(1, employee.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Update employee data at database
     *
     * @param employee employee object
     * @return true if success, false otherwise
     */
    public static boolean updateEmployee(Employee employee) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE_QUERY);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getAddress());
            preparedStatement.setString(3, employee.getPhone());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getNif());
            preparedStatement.setString(6, employee.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Get employee list from database
     *
     * @return employee list
     */
    public static ObservableList<Employee> getEmployeeList() {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEES_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
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
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Search employee in database
     *
     * @param id      search by id
     * @param name    search by name
     * @param address search by address
     * @param phone   search by phone
     * @param email   search by email
     * @param nif     search by nif
     * @return employee search list
     */
    public static ObservableList<Employee> searchEmployee(String id, String name,
                                                          String address, String phone,
                                                          String email, String nif) {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String query = GET_EMPLOYEES_QUERY + "WHERE ";
        boolean and = false;
        if (id != null) {
            query += "id LIKE '%" + id + "%'";
            and = true;
        }
        if (name != null) {
            if (and) {
                query += "AND name LIKE '%" + name + "%'";
            } else {
                query += "name LIKE '%" + name + "%'";
                and = true;
            }
        }
        if (address != null) {
            if (and) {
                query += "AND address LIKE '%" + address + "%'";
            } else {
                query += "address LIKE '%" + address + "%'";
                and = true;
            }
        }
        if (phone != null) {
            if (and) {
                query += "AND phone LIKE '%" + phone + "%'";
            } else {
                query += "phone LIKE '%" + phone + "%'";
                and = true;
            }
        }
        if (email != null) {
            if (and) {
                query += "AND email LIKE '%" + email + "%'";
            } else {
                query += "email LIKE '%" + email + "%'";
                and = true;
            }
        }
        if (nif != null) {
            if (and) {
                query += "AND nif LIKE '%" + nif + "%'";
            } else {
                query += "nif LIKE '%" + nif + "%'";
            }
        }

        System.out.println(query);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String employeeId = resultSet.getString("id");
                String employeeName = resultSet.getString("name");
                String employeeAddress = resultSet.getString("address");
                String employeePhone = resultSet.getString("phone");
                String employeeEmail = resultSet.getString("email");
                String employeeNif = resultSet.getString("nif");

                Employee employee = new Employee(employeeId, employeeName,
                        employeeAddress, employeePhone, employeeEmail, employeeNif);
                list.add(employee);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Get new invoice id
     *
     * @return new invoice's id
     */
    public static int getInvoiceId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_INVOICE_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    /**
     * Insert new invoice
     *
     * @param invoice invoice object
     * @return true if success, false otherwise
     */
    public static boolean insertInvoice(Invoice invoice) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_INVOICE_QUERY);
            preparedStatement.setString(1, invoice.getCustomerId());
            preparedStatement.setString(2, invoice.getEmployeeId());
            preparedStatement.setString(3, invoice.getDate());
            preparedStatement.setString(4, invoice.getPdf());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Delete invoice from database
     *
     * @param invoice invoice object
     * @return true if success, false otherwise
     */
    public static boolean deleteInvoice(Invoice invoice) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_INVOICE_QUERY);
            preparedStatement.setString(1, invoice.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Get invoice list from database
     *
     * @return invoice list
     */
    public static ObservableList<Invoice> getInvoiceList() {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_INVOICES_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String customerName = resultSet.getString("customer_name");
                String employeeName = resultSet.getString("employee_name");
                String date = resultSet.getString("date");
                String pdf = resultSet.getString("pdf");

                Invoice invoice = new Invoice(id, customerName, employeeName, date, pdf);
                invoice.setCustomerName(customerName);
                invoice.setEmployeeName(employeeName);
                list.add(invoice);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Search invoice in database
     *
     * @param id        search by id
     * @param customer  search by customer
     * @param employee  search by employee
     * @param product   search by product
     * @param initDate  search by init date
     * @param finalDate search by final date
     * @return invoice search list
     */
    public static ObservableList<Invoice> searchInvoice(String id, String customer,
                                                        String employee, String product,
                                                        String initDate, String finalDate) {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        String query = SEARCH_INVOICES_QUERY;
        boolean and = false;
        boolean customers = false;
        boolean employees = false;
        if (customer != null) {
            if (customer.matches("[0-9]+")) {
                query += "INNER JOIN customers ON invoices.customer_id = customers.id " +
                        "AND customers.id LIKE '%" + customer + "%' ";
            } else {
                query += "INNER JOIN customers ON invoices.customer_id = customers.id " +
                        "AND customers.name LIKE '%" + customer + "%' ";
            }
            customers = true;
        }
        if (employee != null) {
            if (employee.matches("[0-9]+")) {
                query += "INNER JOIN employees ON invoices.employee_id = employees.id " +
                        "AND employees.id LIKE '%" + employee + "%' ";
            } else {
                query += "INNER JOIN employees ON invoices.employee_id = employees.id " +
                        "AND employees.name LIKE '%" + employee + "%' ";
            }
            employees = true;
        }
        if (product != null) {
            if (product.matches("[0-9]+")) {
                // TODO Invoice product id search
            } else {
                // TODO Invoice product name search
            }
        }
        if (!customers) {
            query += "INNER JOIN customers ON invoices.customer_id = customers.id ";
        }
        if (!employees) {
            query += "INNER JOIN employees ON invoices.employee_id = employees.id ";
        }
        if (id != null || initDate != null || finalDate != null) {
            query += "WHERE ";
        }
        if (id != null) {
            query += "invoices.id LIKE '%" + id + "%'";
            and = true;
        }
        if (initDate != null) {
            if (and) {
                query += "AND invoices.date>='" + initDate + "'";
            } else {
                query += "invoices.date>='" + initDate + "'";
                and = true;
            }
        }
        if (finalDate != null) {
            if (and) {
                query += "AND invoices.date<='" + finalDate + "'";
            } else {
                query += "invoices.date<='" + finalDate + "'";
            }
        }

        System.out.println(query);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String invoiceId = resultSet.getString("id");
                String customerId = resultSet.getString("customer_id");
                String customerName = resultSet.getString("customer_name");
                String employeeId = resultSet.getString("employee_id");
                String employeeName = resultSet.getString("employee_name");
                String date = resultSet.getString("date");
                String pdf = resultSet.getString("pdf");

                Invoice invoice = new Invoice(invoiceId, customerId, employeeId, date, pdf);
                invoice.setCustomerName(customerName);
                invoice.setEmployeeName(employeeName);
                list.add(invoice);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Get customer note new id
     *
     * @return new customer note's id
     */
    public static int getCustomerNotesId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_NOTE_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    /**
     * Insert new customer note
     *
     * @param note note object
     * @return true if success, false otherwise
     */
    public static boolean insertCustomerNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_NOTE_QUERY);
            preparedStatement.setString(1, note.getPersonId());
            preparedStatement.setString(2, note.getMessage());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Delete customer note from database
     *
     * @param note note object
     * @return true if success, false otherwise
     */
    public static boolean deleteCustomerNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_NOTE_QUERY);
            preparedStatement.setString(1, note.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Update customer note at database
     *
     * @param note customer note object
     * @return true if success, false otherwise
     */
    public static boolean updateCustomerNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_NOTE_QUERY);
            preparedStatement.setString(1, note.getMessage());
            preparedStatement.setString(2, note.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Get customer notes list from database
     *
     * @param customer customer object
     * @return customer notes list
     */
    public static ObservableList<Note> getCustomerNotesList(Customer customer) {
        ObservableList<Note> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_NOTES_QUERY);
            preparedStatement.setString(1, customer.getId());
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String message = resultSet.getString("message");

                list.add(new Note(id, message));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Get employee note new id
     */
    public static int getEmployeeNotesId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEE_NOTE_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    /**
     * Insert new employee note
     *
     * @param note note object
     * @return true if success, false otherwise
     */
    public static boolean insertEmployeeNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_NOTE_QUERY);
            preparedStatement.setString(1, note.getPersonId());
            preparedStatement.setString(2, note.getMessage());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Delete employee note from database
     *
     * @param note note object
     * @return true if success, false otherwise
     */
    public static boolean deleteEmployeeNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_NOTE_QUERY);
            preparedStatement.setString(1, note.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Update employee note at database
     *
     * @param note employee note object
     * @return true if success, false otherwise
     */
    public static boolean updateEmployeeNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE_NOTE_QUERY);
            preparedStatement.setString(1, note.getMessage());
            preparedStatement.setString(2, note.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Get employee notes list from database
     *
     * @param employee employee object
     * @return employee notes list
     */
    public static ObservableList<Note> getEmployeeNotesList(Employee employee) {
        ObservableList<Note> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_EMPLOYEE_NOTES_QUERY);
            preparedStatement.setString(1, employee.getId());
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String message = resultSet.getString("message");

                list.add(new Note(id, message));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Insert new supplier note
     *
     * @param note note object
     * @return true if success, false otherwise
     */
    public static boolean insertSupplierNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_SUPPLIER_NOTE_QUERY);
            preparedStatement.setString(1, note.getPersonId());
            preparedStatement.setString(2, note.getMessage());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Delete supplier note from database
     *
     * @param note note object
     * @return true if success, false otherwise
     */
    public static boolean deleteSupplierNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_SUPPLIER_NOTE_QUERY);
            preparedStatement.setString(1, note.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Update supplier note at database
     *
     * @param note note object
     * @return true if success, false otherwise
     */
    public static boolean updateSupplierNote(Note note) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_SUPPLIER_NOTE_QUERY);
            preparedStatement.setString(1, note.getMessage());
            preparedStatement.setString(2, note.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    public static ObservableList<Note> getSupplierNotesList(Supplier supplier) {
        ObservableList<Note> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_SUPPLIER_NOTES_QUERY);
            preparedStatement.setString(1, supplier.getId());
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String message = resultSet.getString("message");

                list.add(new Note(id, message));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }


    /**
     * Get new product id
     *
     * @return new product's id
     */
    public static int getProductId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_PRODUCT_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    /**
     * Insert new product
     *
     * @param product product object
     * @return true if success, false otherwise
     */
    public static boolean insertProduct(Product product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_PRODUCT_QUERY);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getPrice());
            preparedStatement.setString(3, product.getSupplierId());
            preparedStatement.setString(4, product.getQuantity());
            preparedStatement.setString(5, product.getImage());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Get product list from database
     *
     * @return product notes list
     */
    public static ObservableList<Product> getProductList() {
        ObservableList<Product> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_PRODUCTS_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
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
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Search product in database
     *
     * @param id       search by id
     * @param name     search by name
     * @param price    search by price
     * @param supplier search by supplier
     * @param quantity search by quantity
     * @return product search list
     */
    public static ObservableList<Product> searchProduct(String id, String name,
                                                        String price, String supplier,
                                                        String quantity) {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String query = SEARCH_PRODUCTS_QUERY;
        boolean and = false;
        boolean suppliers = false;
        if (supplier != null) {
            if (supplier.matches("[0-9]+")) {
                query += "INNER JOIN suppliers ON products.supplier_id = suppliers.id " +
                        "AND suppliers.id LIKE '%" + supplier + "%' ";
            } else {
                query += "INNER JOIN suppliers ON products.supplier_id = suppliers.id " +
                        "AND suppliers.name LIKE '%" + supplier + "%' ";
            }
            suppliers = true;
        }
        if (!suppliers) {
            query += "INNER JOIN suppliers ON products.supplier_id = suppliers.id ";
        }
        if (id != null || name != null || price != null || quantity != null) {
            query += "WHERE ";
        }
        if (id != null) {
            query += "products.id LIKE '%" + id + "%'";
            and = true;
        }
        if (name != null) {
            if (and) {
                query += "AND products.name LIKE '%" + name + "%'";
            } else {
                query += "products.name LIKE '%" + name + "%'";
                and = true;
            }
        }
        if (price != null) {
            if (and) {
                query += "AND products.price LIKE '%" + price + "%'";
            } else {
                query += "products.price LIKE '%" + price + "%'";
                and = true;
            }
        }
        if (quantity != null) {
            if (and) {
                query += "AND products.quantity LIKE '%" + quantity + "%'";
            } else {
                query += "products.quantity LIKE '%" + quantity + "%'";
            }
        }

        System.out.println(query);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productId = resultSet.getString("id");
                String productName = resultSet.getString("name");
                String productPrice = resultSet.getString("price");
                String productSupplierId = resultSet.getString("supplier_id");
                String productSupplierName = resultSet.getString("supplier_name");
                String productQuantity = resultSet.getString("quantity");
                String image = resultSet.getString("image");

                Product product = new Product(productId, productName,
                        productPrice, productSupplierId, productQuantity, image);
                product.setSupplierName(productSupplierName);
                list.add(product);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Delete product from database
     *
     * @param product product object
     * @return true if success, false otherwise
     */
    public static boolean deleteProduct(Product product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_PRODUCT_QUERY);
            preparedStatement.setString(1, product.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Update product data at database
     *
     * @param product product object
     * @return true if success, false otherwise
     */
    public static boolean updateProduct(Product product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_QUERY);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getPrice());
            preparedStatement.setString(3, product.getSupplierId());
            preparedStatement.setString(4, product.getQuantity());
            preparedStatement.setString(5, product.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    /**
     * Update invoice data at database
     *
     * @param invoice invoice object
     * @return true if success, false otherwise
     */
    public static boolean updateInvoice(Invoice invoice) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_INVOICE_QUERY);
            preparedStatement.setString(1, invoice.getCustomerId());
            preparedStatement.setString(2, invoice.getEmployeeId());
            preparedStatement.setString(3, invoice.getDate());
            preparedStatement.setString(4, invoice.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    // TODO Comment following methods and organize class
    public static ObservableList<Supplier> getSupplierList() {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_SUPPLIERS_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String nif = resultSet.getString("nif");

                list.add(new Supplier(id, name, address, phone, email, nif));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    /**
     * Search supplier in database
     *
     * @param id      search by id
     * @param name    search by name
     * @param address search by address
     * @param phone   search by phone
     * @param email   search by email
     * @param nif     search by nif
     * @return supplier search list
     */
    public static ObservableList<Supplier> searchSupplier(String id, String name,
                                                          String address, String phone,
                                                          String email, String nif) {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        String query = GET_SUPPLIERS_QUERY + "WHERE ";
        boolean and = false;
        if (id != null) {
            query += "id LIKE '" + id + "'";
            and = true;
        }
        if (name != null) {
            if (and) {
                query += "AND name LIKE '%" + name + "%'";
            } else {
                query += "name LIKE '%" + name + "%'";
                and = true;
            }
        }
        if (address != null) {
            if (and) {
                query += "AND address LIKE '%" + address + "%'";
            } else {
                query += "address LIKE '%" + address + "%'";
                and = true;
            }
        }
        if (phone != null) {
            if (and) {
                query += "AND phone LIKE '%" + phone + "%'";
            } else {
                query += "phone LIKE '%" + phone + "%'";
                and = true;
            }
        }
        if (email != null) {
            if (and) {
                query += "AND email LIKE '%" + email + "%'";
            } else {
                query += "email LIKE '%" + email + "%'";
                and = true;
            }
        }
        if (nif != null) {
            if (and) {
                query += "AND nif LIKE '%" + nif + "%'";
            } else {
                query += "nif LIKE '%" + nif + "%'";
            }
        }

        System.out.println(query);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String employeeId = resultSet.getString("id");
                String employeeName = resultSet.getString("name");
                String employeeAddress = resultSet.getString("address");
                String employeePhone = resultSet.getString("phone");
                String employeeEmail = resultSet.getString("email");
                String employeeNif = resultSet.getString("nif");

                Supplier supplier = new Supplier(employeeId, employeeName,
                        employeeAddress, employeePhone, employeeEmail, employeeNif);
                list.add(supplier);
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    public static boolean deleteSupplier(Supplier supplier) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_SUPPLIER_QUERY);
            preparedStatement.setString(1, supplier.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    public static boolean insertSupplier(Supplier supplier) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_SUPPLIER_QUERY);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getAddress());
            preparedStatement.setString(3, supplier.getPhone());
            preparedStatement.setString(4, supplier.getEmail());
            preparedStatement.setString(5, supplier.getNif());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    public static boolean updateSupplier(Supplier supplier) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_SUPPLIER_QUERY);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getAddress());
            preparedStatement.setString(3, supplier.getPhone());
            preparedStatement.setString(4, supplier.getEmail());
            preparedStatement.setString(5, supplier.getNif());
            preparedStatement.setString(6, supplier.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    public static int getSupplierId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_SUPPLIER_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    public static int getSupplierNotesId() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_SUPPLIER_NOTE_ID_QUERY);
            // Execute query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) + 1;
        } catch (SQLException ex) {
            logSQLException(ex);
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
    }

    public static ObservableList<Product> getInvoiceProductList(Invoice invoice) {
        ObservableList<Product> list = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(GET_PRODUCT_INVOICE_QUERY);
            preparedStatement.setString(1, invoice.getId());
            // Execute query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("product_id");
                String name = resultSet.getString("name");
                String price = resultSet.getString("price");
                String supplier = resultSet.getString("supplier_id");
                String quantity = resultSet.getString("quantity");
                String image = resultSet.getString("image");

                list.add(new Product(id, name, price, supplier, quantity, image));
            }
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return list;
    }

    public static boolean deleteInvoiceProduct(Product product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_INVOICE_PRODUCT_QUERY);
            preparedStatement.setString(1, product.getId());
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    public static boolean insertProductInvoice(String invoiceId, String productId, String quantity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(INSERT_PRODUCT_INVOICE_QUERY);
            preparedStatement.setString(1, invoiceId);
            preparedStatement.setString(2, productId);
            preparedStatement.setString(3, quantity);

            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }

    public static boolean updateProductInvoice(String productInvoiceId, String productId, String quantity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Create connection
            connection = DatabasePool.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_INVOICE_QUERY);
            preparedStatement.setString(1, productId);
            preparedStatement.setString(2, quantity);
            preparedStatement.setString(3, productInvoiceId);
            // Execute query
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            logSQLException(ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
        }
        return false;
    }
}