package pt.shop.management.ui.material.dialog;

import com.jfoenix.assets.JFoenixResources;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Material Dialog Layout Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class MaterialDialogLayout extends VBox {
    private static final String DEFAULT_STYLE_CLASS = "material-dialog-layout";
    private StackPane heading = new StackPane();
    private StackPane body = new StackPane();
    private FlowPane actions = new FlowPane();

    /**
     * Create empty dialog layout
     */
    public MaterialDialogLayout(boolean error) {
        this.initialize();
        heading.getStyleClass().addAll("material-layout-heading", "title");
        body.getStyleClass().add("material-layout-body");
        VBox.setVgrow(body, Priority.ALWAYS);
        actions.getStyleClass().add("material-layout-actions");

        if (error) {
            GridPane grid = new GridPane();
            grid.setMaxHeight(20);
            grid.setPrefHeight(20);
            ImageView customImage = new ImageView(new Image("/img/error2.png", 45, 45, true, true));
            grid.add(customImage, 2, 0);

            this.setHeading(grid);
        }

        getChildren().setAll(heading, body, actions);
    }

    /**
     * Get dialog header note
     *
     * @return - heading node list
     */
    public ObservableList<Node> getHeading() {
        return heading.getChildren();
    }

    /**
     * Set dialog header node
     *
     * @param titleContent - header node
     */
    public void setHeading(Node... titleContent) {
        this.heading.getChildren().setAll(titleContent);
    }

    /**
     * Get dialog body node
     *
     * @return - dialog body node list
     */
    public ObservableList<Node> getBody() {
        return body.getChildren();
    }

    /**
     * Set dialog body node
     *
     * @param body - body node
     */
    public void setBody(Node... body) {
        this.body.getChildren().setAll(body);
    }

    /**
     * Get dialog actions
     *
     * @return - dialog actions node list
     */
    public ObservableList<Node> getActions() {
        return actions.getChildren();
    }

    /**
     * Set actions of the dialog (Accept, Cancel,...)
     *
     * @param actions - actions node
     */
    public void setActions(Node... actions) {
        this.actions.getChildren().setAll(actions);
    }

    /**
     * Set actions of the dialog (Accept, Cancel,...)
     *
     * @param actions - actions node list
     */
    public void setActions(List<? extends Node> actions) {
        this.actions.getChildren().setAll(actions);
    }

    /**
     * Get CSS file
     */
    @Override
    public String getUserAgentStylesheet() {
        return JFoenixResources.load("/css/styles.css").toExternalForm();
    }

    /**
     * Initialize style class
     */
    private void initialize() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }
}
