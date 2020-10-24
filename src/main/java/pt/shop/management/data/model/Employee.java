package pt.shop.management.data.model;

/**
 * Employee Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class Employee {
    String id;
    String name;
    String address;
    String phone;
    String email;
    String nif;

    /**
     * Employee object constructor
     *
     * @param id      - employee's id
     * @param name    - employee's name
     * @param address - employee's address
     * @param phone   - employee's phone
     * @param email   - employee's email
     * @param nif     - employee's nif
     */
    public Employee(String id, String name, String address, String phone, String email, String nif) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.nif = nif;
    }

    /**
     * Get employee id
     *
     * @return - employee id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get employee name
     *
     * @return - employee name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set employee name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get employee address
     *
     * @return - employee address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Set employee address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get employee phone
     *
     * @return - employee phone
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * Set employee phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get employee email
     *
     * @return - employee email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set employee email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get employee nif
     *
     * @return - employee nif
     */
    public String getNif() {
        return this.nif;
    }

    /**
     * Set employee nif
     */
    public void setNif(String nif) {
        this.nif = nif;
    }
}
