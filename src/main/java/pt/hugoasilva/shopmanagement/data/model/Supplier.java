package pt.hugoasilva.shopmanagement.data.model;

/**
 * Supplier Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class Supplier {
    private final String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String nif;

    /**
     * Supplier object constructor
     *
     * @param id      supplier's id
     * @param name    supplier's message
     * @param address supplier's address
     * @param phone   supplier's phone
     * @param email   supplier's email
     * @param nif     supplier's nif
     */
    public Supplier(String id, String name, String address, String phone, String email, String nif) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.nif = nif;
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
     * Get supplier address
     *
     * @return supplier address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set supplier address
     *
     * @param address supplier address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get supplier phone
     *
     * @return supplier phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set supplier phone
     *
     * @param phone supplier phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get supplier email
     *
     * @return supplier email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set supplier email
     *
     * @param email supplier email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get supplier nif
     *
     * @return supplier nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * Set supplier nif
     *
     * @param nif supplier nif
     */
    public void setNif(String nif) {
        this.nif = nif;
    }
}
