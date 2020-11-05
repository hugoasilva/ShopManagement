package pt.hugoasilva.shopmanagement.data.model;

/**
 * Product Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class Product {
    private final String id;
    private final String image;
    private String name;
    private String price;
    private String supplierId;
    private String supplierName;
    private String quantity;

    /**
     * Product object constructor
     *
     * @param id          product's id
     * @param name        product's message
     * @param price       product's price
     * @param supplier_id product's supplier id
     * @param quantity    product's quantity
     * @param image       product's image file path
     */
    public Product(String id, String name, String price, String supplier_id, String quantity, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.supplierId = supplier_id;
        this.quantity = quantity;
        this.image = image;
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
     * Get product name
     *
     * @return product name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set product name
     *
     * @param name product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get product price
     *
     * @return product price
     */
    public String getPrice() {
        return this.price;
    }

    /**
     * Set product price
     *
     * @param price product price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Get product supplier id
     *
     * @return product supplier id
     */
    public String getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set product supplier id
     *
     * @param id supplier id
     */
    public void setSupplierId(String id) {
        this.supplierId = id;
    }

    /**
     * Get product supplier name
     *
     * @return product supplier name
     */
    public String getSupplierName() {
        return this.supplierName;
    }

    /**
     * Set product supplier name
     *
     * @param name supplier name
     */
    public void setSupplierName(String name) {
        this.supplierName = name;
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

    /**
     * Get product image
     *
     * @return product image
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Set product image
     *
     * @param image product image
     */
    public void setImage(String image) {
        this.quantity = image;
    }
}
