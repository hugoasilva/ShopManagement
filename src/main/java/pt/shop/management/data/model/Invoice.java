package pt.shop.management.data.model;

/**
 * Invoice Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class Invoice {
    private final String id;
    private String customerId;
    private String customerName;
    private String employeeId;
    private String employeeName;
    private String date;
    private String products;
    private String pdf;

    /**
     * Invoice object constructor
     *
     * @param id         - invoice's id
     * @param customerId - customer's id
     * @param employeeId - employee's id
     * @param date       - invoice date
     * @param products   - invoice products
     * @param pdf        - invoice pdf file path
     */
    public Invoice(String id, String customerId, String employeeId, String date,
                   String products, String pdf) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.date = date;
        this.products = products;
        this.pdf = pdf;
    }

    /**
     * Get invoice id
     *
     * @return - invoice id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get customer id
     *
     * @return - customer id
     */
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * Set customer id
     *
     * @param customerId - customer id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get employee id
     *
     * @return - employee id
     */
    public String getEmployeeId() {
        return this.employeeId;
    }

    /**
     * Set employee id
     *
     * @param employeeId - employee id
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Get invoice date
     *
     * @return - invoice date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Set invoice date
     *
     * @param date - invoice date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get invoice products
     *
     * @return - invoice products
     */
    public String getProducts() {
        return this.products;
    }

    /**
     * Set invoice products
     *
     * @param products - invoice products
     */
    public void setProducts(String products) {
        this.products = products;
    }

    /**
     * Get invoice pdf
     *
     * @return - invoice pdf
     */
    public String getPdf() {
        return this.pdf;
    }

    /**
     * Set invoice pdf
     *
     * @param pdf - invoice pdf
     */
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    /**
     * Get invoice customer name
     *
     * @return - invoice customer name
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * Set invoice customer name
     *
     * @param name - invoice customer name
     */
    public void setCustomerName(String name) {
        this.customerName = name;
    }

    /**
     * Get invoice employee name
     *
     * @return - invoice employee name
     */
    public String getEmployeeName() {
        return this.employeeName;
    }

    /**
     * Set invoice employee name
     *
     * @param name - invoice employee name
     */
    public void setEmployeeName(String name) {
        this.employeeName = name;
    }
}
