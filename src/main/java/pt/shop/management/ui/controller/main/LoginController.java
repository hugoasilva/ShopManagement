package pt.shop.management.ui.controller.main;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.data.database.DatabaseHandler;
import pt.shop.management.ui.dialog.DialogHandler;
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

public class
LoginController implements Initializable {

    // Logger
    private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

    // UI Content
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private StackPane mainContainer;

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
        String response = DatabaseHandler.login(username, password);

        switch (response) {
            case "success":
                closeStage();
                loadMain();
                LOGGER.log(Level.INFO, "Sessão iniciada com sucesso com o utilizador: {}", username);
                break;
            case "error":
                DialogHandler.showMaterialErrorDialog(this.mainContainer,
                        "Erro!", "Nome de utilizador ou palavra passe errados.");
                break;
            case "dberror":
                DialogHandler.showMaterialErrorDialog(this.mainContainer,
                        "Erro de Base de Dados!",
                        new String("Não foi possível aceder à base de dados".getBytes(), StandardCharsets.UTF_8));
                System.exit(0);
                break;
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
            Platform.setImplicitExit(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                    boolean close = DialogHandler.showMaterialConfirmationDialog(
                            parent, "Sair", "Tem a certeza que pretende sair?");
                    if (close) {
                        Platform.exit();
                        System.exit(0);
                    }
                }
            });
            stage.show();
            ShopManagementUtil.setStageIcon(stage);
        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }
}