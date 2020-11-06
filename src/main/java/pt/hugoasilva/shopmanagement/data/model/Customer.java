package pt.hugoasilva.shopmanagement.data.model;

/**
 * Customer Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class Customer {
    private final String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String nif;

    /**
     * Customer object constructor
     *
     * @param id      customer's id
     * @param name    customer's name
     * @param address customer's address
     * @param phone   customer's phone
     * @param email   customer's email
     * @param nif     customer's nif
     */
    public Customer(String id, String name, String address, String phone, String email, String nif) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.nif = nif;
    }

    /**
     * Get customer id
     *
     * @return customer id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get customer name
     *
     * @return customer name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get customer address
     *
     * @return customer address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Set customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get customer phone
     *
     * @return customer phone
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Set customer phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get customer email
     *
     * @return customer email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set customer email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get customer nif
     *
     * @return customer nif
     */
    public String getNif() {
        return this.nif;
    }

    /**
     * Set customer nif
     */
    public void setNif(String nif) {
        this.nif = nif;
    }
}