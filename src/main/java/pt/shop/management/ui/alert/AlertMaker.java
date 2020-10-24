package pt.shop.management.ui.alert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.shop.management.util.ShopManagementUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Alert Dialog class
 *
 * @author Hugo Silva
 * @version 2020-10-23
 */

public class AlertMaker {

    /**
     * Show simple alert
     *
     * @param title   - alert's title
     * @param content - alert's message
     */
    public static void showSimpleAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    /**
     * Show error message
     *
     * @param title   - error's title
     * @param content - error's message
     */
    public static void showErrorMessage(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro!");
        alert.setHeaderText(title);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls,
                                          String header, String body, boolean closeParent) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        if (controls.isEmpty()) {
            controls.add(new JFXButton("OK"));
        }
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().add("dialog-button");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                if (closeParent) {
                    Stage stage = (Stage) dialog.getParent().getScene().getWindow();
                    stage.close();
                }
                dialog.close();
            });
        });

        dialogLayout.setHeading(new Label(header));
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(controls);
        dialog.show();
        dialog.setOnDialogClosed(event1 -> nodeToBeBlurred.setEffect(null));
        nodeToBeBlurred.setEffect(blur);
    }

    /**
     * Show OS notification
     *
     * @param title   - notification's title
     * @param message - notification's message
     */
    public static void showTrayMessage(String title, String message) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            BufferedImage image = ImageIO.read(AlertMaker.class.getResource(ShopManagementUtil.ICON_IMAGE_LOC));
            TrayIcon trayIcon = new TrayIcon(image,
                    new String("Sistema de Gestão de Loja".getBytes(), StandardCharsets.UTF_8));
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip(new String("Sistema de Gestão de Loja".getBytes(), StandardCharsets.UTF_8));
            tray.add(trayIcon);
            trayIcon.displayMessage(title, message, MessageType.INFO);
            tray.remove(trayIcon);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Set alert's style
     *
     * @param alert - alert dialog
     */
    private static void styleAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        ShopManagementUtil.setStageIcon(stage);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertMaker.class.getResource("/css/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
    }
}
