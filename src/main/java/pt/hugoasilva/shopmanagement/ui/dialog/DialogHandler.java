package pt.hugoasilva.shopmanagement.ui.dialog;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.hugoasilva.shopmanagement.ui.dialog.layout.MaterialButton;
import pt.hugoasilva.shopmanagement.ui.dialog.layout.MaterialDialog;
import pt.hugoasilva.shopmanagement.ui.dialog.layout.MaterialDialogLayout;

/**
 * Dialog Handler Class
 *
 * @author Hugo Silva
 * @version 2020-11-01
 */

public class DialogHandler {

    /**
     * Show material confirmation dialog
     *
     * @param nodeToBeBlurred parent window to be blurred
     * @param header          dialog header text
     * @param body            dialog body text
     */
    public static boolean showMaterialConfirmationDialog(Node nodeToBeBlurred, String header, String body) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout alertLayout = new MaterialDialogLayout(false);
        MaterialDialog<String> alert = new MaterialDialog<>(nodeToBeBlurred.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        MaterialButton okButton = new MaterialButton("OK");
        okButton.setDefaultButton(true);
        okButton.setDisableVisualFocus(true);
        okButton.setOnAction(addEvent -> {
            alert.setResult("true");
            alert.hideWithAnimation();
        });

        MaterialButton cancelButton = new MaterialButton("Cancelar");
        cancelButton.setCancelButton(true);
        cancelButton.setDisableVisualFocus(true);
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
     * Show material error dialog
     *
     * @param nodeToBeBlurred parent window to be blurred
     * @param header          dialog header text
     * @param body            dialog body text
     */
    public static void showMaterialErrorDialog(Node nodeToBeBlurred, String header, String body) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout dialogLayout = new MaterialDialogLayout(true);
        MaterialDialog<String> alert = new MaterialDialog<>(nodeToBeBlurred.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        MaterialButton okButton = new MaterialButton("OK");
        okButton.setDefaultButton(true);
        okButton.setDisableVisualFocus(true);
        okButton.setOnAction(addEvent -> {
            alert.close();
            alert.hideWithAnimation();
            nodeToBeBlurred.setEffect(null);
        });
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(okButton);
        alert.setContent(dialogLayout);
        nodeToBeBlurred.setEffect(blur);
        alert.showAndWait();
        alert.setOnCloseRequest(event1 -> nodeToBeBlurred.setEffect(null));
    }

    /**
     * Show material information dialog
     *
     * @param nodeToBeBlurred parent window to be blurred
     * @param header          dialog header text
     * @param body            dialog body text
     * @param closeParent     close parent window
     */
    public static void showMaterialInformationDialog(
            Node nodeToBeBlurred, String header, String body, boolean closeParent) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        MaterialDialogLayout dialogLayout = new MaterialDialogLayout(false);
        MaterialDialog<String> alert = new MaterialDialog<>(nodeToBeBlurred.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        MaterialButton okButton = new MaterialButton("OK");
        okButton.setDefaultButton(true);
        okButton.setDisableVisualFocus(true);
        okButton.setOnAction(addEvent -> {
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
}
