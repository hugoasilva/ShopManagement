package pt.shop.management.ui.add.employee;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Employee;
import pt.shop.management.ui.dialog.DialogHandler;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Employee Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class EmployeeAddController implements Initializable {

    // Employee data
    private String id;
    private String editId;
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
    private void addEmployee(ActionEvent event) throws SQLException {
        String employeeId = String.valueOf(DatabaseHandler.getEmployeeId());
        this.id = employeeId;
        String employeeName = name.getText();
        String employeeAddress = address.getText();
        String employeePhone = phone.getText();
        String employeeEmail = email.getText();
        String employeeNif = nif.getText();

        if (employeeName.isEmpty() || employeeAddress.isEmpty() || employeePhone.isEmpty()
                || employeeEmail.isEmpty() || employeeNif.isEmpty()) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (this.isInEditMode) {
            this.handleUpdateEmployee();
            return;
        }

        Employee employee = new Employee(employeeId, employeeName, employeeAddress,
                employeePhone, employeeEmail, employeeNif);
        if (DatabaseHandler.insertEmployee(employee)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Empregado adicionado",
                    employeeName + " adicionado com sucesso!", false);
            clearEntries();
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Ocorreu um erro",
                    "Verifique os dados e tente novamente.", false);
        }
    }

    /**
     * Populate table
     *
     * @param employee - employee object
     */
    public void inflateUI(Employee employee) {
        this.editId = employee.getId();
        this.name.setText(employee.getName());
        this.address.setText(employee.getAddress());
        this.phone.setText(employee.getPhone());
        this.email.setText(employee.getEmail());
        this.nif.setText(employee.getNif());

        this.isInEditMode = Boolean.TRUE;
    }

    /**
     * Clear table entries
     */
    private void clearEntries() {
        this.name.clear();
        this.address.clear();
        this.phone.clear();
        this.email.clear();
        this.nif.clear();
    }

    /**
     * Handle employee update
     */
    private void handleUpdateEmployee() throws SQLException {
        Employee employee = new Employee(this.editId, this.name.getText(), this.address.getText(),
                this.phone.getText(), this.email.getText(), this.nif.getText());
        if (DatabaseHandler.updateEmployee(employee)) {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Successo!",
                    "Dados de empregado atualizados.", true);
        } else {
            DialogHandler.showMaterialInformationDialog(this.mainContainer, "Erro",
                    new String("Não foi possível atualizar os dados.".getBytes(),
                            StandardCharsets.UTF_8), false);
        }
    }
}