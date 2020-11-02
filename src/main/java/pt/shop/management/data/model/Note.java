package pt.shop.management.data.model;

/**
 * Note Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class Note {
    private String id;
    private String message;
    private String personId;
    private String type;

    /**
     * Note object constructor
     *
     * @param id      note's id
     * @param message note's message
     */
    public Note(String id, String message) {
        this.id = id;
        this.message = message;
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
     * Set note id
     *
     * @param id note message
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get note message
     *
     * @return note message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set note message
     *
     * @param message note message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get note person id
     *
     * @return note person id
     */
    public String getPersonId() {
        return this.personId;
    }

    /**
     * Set note person id
     *
     * @param id note person id
     */
    public void setPersonId(String id) {
        this.personId = id;
    }

    /**
     * Get note person type
     *
     * @return note person type
     */
    public String getPersonType() {
        return this.type;
    }


    // TODO Remove following methods???

    /**
     * Set note person type
     *
     * @param type note person type
     */
    public void setPersonType(String type) {
        this.type = type;
    }

    public boolean isCustomer() {
        return this.type.equals("customer");
    }

    public boolean isEmployee() {
        return this.type.equals("employee");
    }

    public boolean isSupplier() {
        return this.type.equals("supplier");
    }
}
