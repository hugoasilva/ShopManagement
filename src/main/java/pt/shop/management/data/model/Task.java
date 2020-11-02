package pt.shop.management.data.model;

/**
 * Task Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class Task {
    private final String id;
    private String date;
    private String employeeId;
    private String description;
    private String priority;
    private String limitDate;
    private String resolutionDate;
    private String notes;
    private String employeeName;

    /**
     * Task object constructor
     *
     * @param id             task's id
     * @param date           task's date
     * @param employeeId     task's employee id
     * @param description    task's description
     * @param priority       task's priority
     * @param limitDate      task's limit date
     * @param resolutionDate task's resolution date
     * @param notes          task's notes
     */
    public Task(String id, String date, String employeeId, String description,
                String priority, String limitDate, String resolutionDate, String notes) {
        this.id = id;
        this.date = date;
        this.employeeId = employeeId;
        this.description = description;
        this.priority = priority;
        this.limitDate = limitDate;
        this.resolutionDate = resolutionDate;
        this.notes = notes;
    }

    /**
     * Get task id
     *
     * @return task id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get task date
     *
     * @return task date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Set task date
     *
     * @param date task date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get employee id
     *
     * @return employee id
     */
    public String getEmployeeId() {
        return this.employeeId;
    }

    /**
     * Set employee id
     *
     * @param employeeId employee id
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Get task description
     *
     * @return task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set task description
     *
     * @param description task description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get task priority
     *
     * @return task priority
     */
    public String getPriority() {
        return this.priority;
    }

    /**
     * Set task priority
     *
     * @param priority task priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Get task limit date
     *
     * @return task limit date
     */
    public String getLimitDate() {
        return this.limitDate;
    }

    /**
     * Set task limit date
     *
     * @param limitDate task limit date
     */
    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    /**
     * Get task resolution date
     *
     * @return task resolution date
     */
    public String getResolutionDate() {
        return this.resolutionDate;
    }

    /**
     * Set task resolution date
     *
     * @param resolutionDate task resolution date
     */
    public void setResolutionDate(String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    /**
     * Get task notes
     *
     * @return task notes
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Set task notes
     *
     * @param notes task notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Get task employee name
     *
     * @return task employee name
     */
    public String getEmployeeName() {
        return this.employeeName;
    }

    /**
     * Set task employee name
     *
     * @param employeeName task employee name
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
