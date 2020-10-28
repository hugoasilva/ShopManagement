package pt.shop.management.ui.dialog;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.shop.management.ui.dialog.alert.MaterialAlert;
import pt.shop.management.ui.dialog.button.MaterialButton;
import pt.shop.management.ui.dialog.dialog.MaterialDialogLayout;
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

public class DialogHandler {

    /**
     * Show alert
     *
     * @param header   - alert's title
     * @param body - alert's message
     */
    public static void showMaterialAlert(Node nodeToBeBlurred, String header, String body) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout alertLayout = new MaterialDialogLayout();
        MaterialAlert<String> alert = new MaterialAlert<>(nodeToBeBlurred.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        // Buttons get added into the actions section of the layout.
        MaterialButton okButton = new MaterialButton("OK");
        okButton.setDefaultButton(true);
        okButton.setOnAction(addEvent -> {
            // When the button is clicked, we set the result accordingly
            alert.setResult("true");
            alert.hideWithAnimation();
            nodeToBeBlurred.setEffect(null);
        });

        alertLayout.setHeading(new Label(header));
        alertLayout.setBody(new Label(body));
        alertLayout.setActions(okButton);
        alert.setContent(alertLayout);
        nodeToBeBlurred.setEffect(blur);
        alert.showAndWait();
        alert.setOnCloseRequest(event1 -> nodeToBeBlurred.setEffect(null));
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

    public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<MaterialButton> controls,
                                          String header, String body, boolean closeParent) {

        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout dialogLayout = new MaterialDialogLayout();
        MaterialAlert<String> alert = new MaterialAlert<>(nodeToBeBlurred.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        // Buttons get added into the actions section of the layout.
        MaterialButton okButton = new MaterialButton("OK");
        okButton.setDefaultButton(true);
        okButton.setOnAction(addEvent -> {
            // When the button is clicked, we set the result
            if (closeParent) {
                Stage stage = (Stage) nodeToBeBlurred.getScene().getWindow();
                stage.close();
            }
            alert.close();
            alert.hideWithAnimation();
        });

        dialogLayout.setHeading(new Label(header));
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(okButton);
        alert.setContent(dialogLayout);
        nodeToBeBlurred.setEffect(blur);
        alert.showAndWait();
        alert.setOnCloseRequest(event1 -> nodeToBeBlurred.setEffect(null));
    }

    public static boolean showMaterialAlertWithCancel(
            Node nodeToBeBlurred, String header, String body, boolean closeParent) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout alertLayout = new MaterialDialogLayout();
        MaterialAlert<String> alert = new MaterialAlert<>(nodeToBeBlurred.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        // Buttons get added into the actions section of the layout.
        MaterialButton okButton = new MaterialButton("OK");
        okButton.setDefaultButton(true);
        okButton.setOnAction(addEvent -> {
            // When the button is clicked, we set the result accordingly
            alert.setResult("true");
            alert.hideWithAnimation();
            nodeToBeBlurred.setEffect(null);
        });

        MaterialButton cancelButton = new MaterialButton("Cancelar");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(closeEvent -> {
            alert.setResult("false");
            alert.hideWithAnimation();
            nodeToBeBlurred.setEffect(null);
        });

        alertLayout.setHeading(new Label(header));
        alertLayout.setBody(new Label(body));
        alertLayout.setActions(okButton, cancelButton);
        alert.setContent(alertLayout);
        nodeToBeBlurred.setEffect(blur);
        alert.showAndWait();
        alert.setOnCloseRequest(event1 -> nodeToBeBlurred.setEffect(null));

        return Boolean.parseBoolean(alert.getResult());
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
            BufferedImage image = ImageIO.read(DialogHandler.class.getResource(ShopManagementUtil.ICON_IMAGE_LOC));
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
        dialogPane.getStylesheets().add(DialogHandler.class.getResource("/css/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
    }
}
