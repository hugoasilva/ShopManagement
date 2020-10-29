package pt.shop.management.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.util.ShopManagementUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Login Controller Class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class LoginController implements Initializable {

    // Logger
    private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

    // UI Content
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Handle login event
     *
     * @param event - login event
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {

        String username = this.username.getText();
        String password = this.password.getText();

        // Validate login credentials
        boolean valid = DatabaseHandler.login(username, password);

        if (valid) {
            closeStage();
            loadMain();
            LOGGER.log(Level.INFO, "Sessão iniciada com sucesso com o utilizador: {}", username);
        } else {
            this.username.getStyleClass().add("wrong-credentials");
            this.password.getStyleClass().add("wrong-credentials");
        }
    }

    /**
     * Cancel button handler
     *
     * @param event - cancel event
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Close current window
     */
    private void closeStage() {
        ((Stage) this.username.getScene().getWindow()).close();
    }

    /**
     * Load main screen
     */
    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/fxml/main/Main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(new String("Sistema de Gestão de Loja".getBytes(), StandardCharsets.UTF_8));
            stage.setScene(new Scene(parent));
            stage.setMaximized(true);
            stage.show();
            ShopManagementUtil.setStageIcon(stage);
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }
}
