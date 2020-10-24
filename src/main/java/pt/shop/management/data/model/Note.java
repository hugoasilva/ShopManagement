package pt.shop.management.data.model;

/**
 * Note Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class Note {
    private String id;
    private String message;

    /**
     * Note object constructor
     *
     * @param id      - note's id
     * @param message - note's message
     */
    public Note(String id, String message) {
        this.id = id;
        this.message = message;
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
     * Set note id
     *
     * @param id - note message
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get note message
     *
     * @return - note message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set note message
     *
     * @param message - note message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
