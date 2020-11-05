package pt.hugoasilva.shopmanagement.data.model;

/**
 * Quote Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class Quote {
    private final String id;
    private String customerId;
    private String customerName;
    private String date;
    private String pdf;
    private String products;

    /**
     * Quote object constructor
     *
     * @param id         quote's id
     * @param customerId customer's id
     * @param date       quote date
     * @param pdf        quote pdf file path
     */
    public Quote(String id, String customerId, String date, String pdf) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        this.pdf = pdf;
    }

    /**
     * Get quote id
     *
     * @return quote id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get quote id
     *
     * @return quote id
     */
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * Set quote id
     *
     * @param customerId quote id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get quote date
     *
     * @return quote date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Set quote date
     *
     * @param date quote date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get quote pdf
     *
     * @return quote pdf
     */
    public String getPdf() {
        return this.pdf;
    }

    /**
     * Set quote pdf
     *
     * @param pdf quote pdf
     */
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    /**
     * Get quote customer name
     *
     * @return - quote customer name
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * Set quote customer name
     *
     * @param name quote customer name
     */
    public void setCustomerName(String name) {
        this.customerName = name;
    }

    /**
     * Get quote products
     *
     * @return quote products
     */
    public String getProducts() {
        return this.products;
    }

    /**
     * Set quote products
     *
     * @param products quote products
     */
    public void setProducts(String products) {
        this.products = products;
    }
}
