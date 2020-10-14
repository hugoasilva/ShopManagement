package main.java.pt.shop.management.data.model;

/**
 * Product Class
 *
 * @author Hugo Silva
 * @version 2020-10-13
 */

public class Product {
    private final String id;
    private String name;
    private String price;
    private String quantity;

    /**
     * Product object constructor
     *
     * @param id       - product's id
     * @param name     - product's message
     * @param price    - product's price
     * @param quantity - product's quantity
     */
    public Product(String id, String name, String price, String quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Get note id
     *
     * @return - note id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get product name
     *
     * @return - product name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set product name
     *
     * @param name - product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get product price
     *
     * @return - product price
     */
    public String getPrice() {
        return this.price;
    }

    /**
     * Set product price
     *
     * @param price - product price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Get product quantity
     *
     * @return - product quantity
     */
    public String getQuantity() {
        return this.quantity;
    }

    /**
     * Set product quantity
     *
     * @param quantity - product quantity
     */
    public void setMessage(String quantity) {
        this.quantity = quantity;
    }
}
