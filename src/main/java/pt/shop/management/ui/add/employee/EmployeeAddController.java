package pt.shop.management.ui.add.employee;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.files.JSONHandler;
import pt.shop.management.data.files.SFTPHandler;
import pt.shop.management.data.model.Employee;
import pt.shop.management.data.model.Note;
import pt.shop.management.ui.alert.AlertMaker;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Employee Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-16
 */

public class EmployeeAddController implements Initializable {

    // Directory paths
    private final static String LOCAL_DOWNLOAD_PATH = "downloads/";
    private final static String REMOTE_EMPLOYEE_PATH = "/home/pi/gestao/empregados/";
    // Database handler instance
    DatabaseHandler databaseHandler;
    // Employee data
    private String id;
    private String notesPath;
    private Boolean isInEditMode = false;
    // UI Content
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField address;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField nif;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    /**
     * Cancel button handler
     *
     * @param event - cancel event
     */
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    /**
     * Add employee to table
     *
     * @param event - add employee event
     */
    @FXML
    private void addEmployee(ActionEvent event) {

        String employeeId = String.valueOf(DatabaseHandler.getEmployeeId());
        this.id = employeeId;
        String employeeName = name.getText();
        String employeeAddress = address.getText();
        String employeePhone = phone.getText();
        String employeeEmail = email.getText();
        String employeeNif = nif.getText();
        String employeeNotes = REMOTE_EMPLOYEE_PATH + this.id + ".json";
        this.notesPath = employeeNotes;

        if (employeeName.isEmpty() || employeeAddress.isEmpty() || employeePhone.isEmpty()
                || employeeEmail.isEmpty() || employeeNif.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (isInEditMode) {
            handleUpdateEmployee();
            return;
        }

        Employee employee = new Employee(employeeId, employeeName, employeeAddress,
                employeePhone, employeeEmail, employeeNif);
        if (DatabaseHandler.insertEmployee(employee)) {
            this.createNotesJSON();
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Empregado adicionado",
                    employeeName + " adicionado com sucesso!", false);
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
        }
    }

    /**
     * Populate table
     *
     * @param employee - employee object
     */
    public void inflateUI(Employee employee) {
        name.setText(employee.getName());
        address.setText(employee.getAddress());
        phone.setText(employee.getPhone());
        email.setText(employee.getEmail());
        nif.setText(employee.getNif());

        isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear table entries
     */
    private void clearEntries() {
        name.clear();
        address.clear();
        phone.clear();
        email.clear();
        nif.clear();
    }

    /**
     * Handle employee update
     */
    private void handleUpdateEmployee() {
        Employee employee = new Employee(id, name.getText(), address.getText(),
                phone.getText(), email.getText(), nif.getText());
        if (DatabaseHandler.getInstance().updateEmployee(employee)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Successo!",
                    "Dados de empregado atualizados.", false);
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(),
                            StandardCharsets.UTF_8), false);
        }
    }

    /**
     * Create empty notes JSON file
     */
    private void createNotesJSON() {
        // Create notes list
        List<Note> notes = new ArrayList<>();
        // Add null message to notes list
        notes.add(new Note("0", "Message zero"));
        String path = LOCAL_DOWNLOAD_PATH + this.id + ".json";
        // Convert notes list to JSON
        JSONHandler.notesToJSON(notes, path);
        // Upload file to server
        File file = new File(path);
        SFTPHandler.uploadFile(file.getPath(), this.notesPath);
    }
}