package pt.shop.management.ui.add.supplier;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.data.model.Supplier;
import pt.shop.management.ui.alert.AlertMaker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Supplier Add Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class SupplierAddController implements Initializable {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(SupplierAddController.class.getName());
    // Supplier data
    private String id;
    private String imagePath;
    private Boolean isInEditMode = Boolean.FALSE;
    ;
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
     * Add supplier to table
     *
     * @param event - add supplier event
     */
    @FXML
    private void addSupplier(ActionEvent event) throws IOException, SQLException {

        String supplierId = String.valueOf(DatabaseHandler.getSupplierId());
        this.id = supplierId;
        String supplierName = name.getText();
        String supplierAddress = address.getText();
        String supplierPhone = phone.getText();
        String supplierEmail = email.getText();
        String supplierNif = nif.getText();

        if (supplierName.isEmpty() || supplierAddress.isEmpty() || supplierPhone.isEmpty()
                || supplierEmail.isEmpty() || supplierNif.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer,
                    new ArrayList<>(), "Dados insuficientes",
                    "Por favor insira dados em todos os campos.", false);
            return;
        }

        if (isInEditMode) {
            handleUpdateSupplier();
            return;
        }

        Supplier supplier = new Supplier(supplierId, supplierName,
                supplierAddress, supplierPhone, supplierEmail, supplierNif);
//        if (DatabaseHandler.insertSupplier(supplier)) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer,
//                    new ArrayList<>(), "Fornecedor adicionado",
//                    supplierName + " adicionado com sucesso!", true);
//            clearEntries();
//        } else {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer,
//                    new ArrayList<>(), "Ocorreu um erro",
//                    "Verifique os dados e tente novamente.", false);
//        }
    }

    /**
     * Populate table
     *
     * @param supplier - supplier object
     */
    public void inflateUI(Supplier supplier) {
        name.setText(supplier.getName());
        address.setText(supplier.getAddress());
        phone.setText(supplier.getPhone());
        email.setText(supplier.getEmail());
        nif.setText(supplier.getNif());

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
     * Handle supplier update
     */
    private void handleUpdateSupplier() throws SQLException {
        Supplier supplier =
                new Supplier(id, name.getText(), address.getText(),
                        phone.getText(), email.getText(), nif.getText());
//        if (DatabaseHandler.updateSupplier(supplier)) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer,
//                    new ArrayList<>(), "Successo!",
//                    "Dados de fornecedor atualizados.", false);
//        } else {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer,
//                    new ArrayList<>(), "Erro",
//                    new String("Não foi possível atualizar os dados.".getBytes(),
//                            StandardCharsets.UTF_8), false);
//        }
    }

    public void chooseImage(ActionEvent actionEvent) {
        // Open File Chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Imagem");
        fileChooser.getExtensionFilters().addAll(new
                FileChooser.ExtensionFilter("Imagem do produto", "*.jpg", "*.jpeg", "*.png"));
        Stage stage = new Stage(StageStyle.DECORATED);

        // Store File Path
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            LOGGER.log(Level.INFO, "No image file was selected.");
        } else {
            this.imagePath = file.getAbsolutePath();
        }
    }
}
