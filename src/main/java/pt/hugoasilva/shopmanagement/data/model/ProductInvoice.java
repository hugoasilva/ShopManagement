package pt.hugoasilva.shopmanagement.data.model;

/**
 * Product Class
 *
 * @author Hugo Silva
 * @version 2020-11-06
 */

public class ProductInvoice {
    private final String id;
    private String invoiceId;
    private String productId;
    private String productName;
    private String quantity;

    /**
     * Product object constructor
     *
     * @param id          product_invoice's id
     * @param invoiceId   invoice's id
     * @param productId   product's id
     * @param productName product's name
     * @param quantity    product's quantity
     */
    public ProductInvoice(String id, String invoiceId, String productId, String productName, String quantity) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    /**
     * Get note id
     *
     * @return note id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get invoice id
     *
     * @return invoice id
     */
    public String getInvoiceId() {
        return this.invoiceId;
    }

    /**
     * Set invoice id
     *
     * @param id invoice id
     */
    public void setInvoiceId(String id) {
        this.invoiceId = id;
    }

    /**
     * Get product id
     *
     * @return product id
     */
    public String getProductId() {
        return this.productId;
    }

    /**
     * Set product id
     *
     * @param id product id
     */
    public void setProductId(String id) {
        this.productId = id;
    }

    /**
     * Get product quantity
     *
     * @return product quantity
     */
    public String getQuantity() {
        return this.quantity;
    }

    /**
     * Set product quantity
     *
     * @param quantity product quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }


}
