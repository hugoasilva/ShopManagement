package pt.shop.management.ui.dialog;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.shop.management.ui.dialog.dialog.MaterialDialog;
import pt.shop.management.ui.dialog.button.MaterialButton;
import pt.shop.management.ui.dialog.dialog.MaterialDialogLayout;
import pt.shop.management.util.ShopManagementUtil;

/**
 * Dialog Handler Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class DialogHandler {

    /**
     * Show error message
     *
     * @param title   - error's title
     * @param content - error's message
     */
    public static void showErrorMessage(String title, String content) {
        // TODO Material Design error alert
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro!");
        alert.setHeaderText(title);
        alert.setContentText(content);
        styleAlert(alert);
        alert.showAndWait();
    }

    /**
     * Show material information dialog
     * @param nodeToBeBlurred - parent window to be blurred
     * @param header - dialog header text
     * @param body - dialog body text
     * @param closeParent - close parent window
     */
    public static void showMaterialInformationDialog(Node nodeToBeBlurred,
                                                     String header, String body, boolean closeParent) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout dialogLayout = new MaterialDialogLayout();
        MaterialDialog<String> alert = new MaterialDialog<>(nodeToBeBlurred.getScene().getWindow());
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
            nodeToBeBlurred.setEffect(null);
        });

        dialogLayout.setHeading(new Label(header));
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(okButton);
        alert.setContent(dialogLayout);
        nodeToBeBlurred.setEffect(blur);
        alert.showAndWait();
        alert.setOnCloseRequest(event1 -> nodeToBeBlurred.setEffect(null));
    }

    /**
     * Show material confirmation dialog
     * @param nodeToBeBlurred - parent window to be blurred
     * @param header - dialog header text
     * @param body - dialog body text
     */
    public static boolean showMaterialConfirmationDialog(Node nodeToBeBlurred, String header, String body) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout alertLayout = new MaterialDialogLayout();
        MaterialDialog<String> alert = new MaterialDialog<>(nodeToBeBlurred.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        // Buttons get added into the actions section of the layout.
        MaterialButton okButton = new MaterialButton("OK");
        okButton.setDefaultButton(true);
        okButton.setOnAction(addEvent -> {
            // When the button is clicked, we set the result accordingly
            alert.setResult("true");
            alert.hideWithAnimation();
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
