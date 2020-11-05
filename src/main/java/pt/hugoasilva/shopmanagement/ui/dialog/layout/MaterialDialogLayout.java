package pt.hugoasilva.shopmanagement.ui.dialog.layout;

import com.jfoenix.assets.JFoenixResources;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;

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
            ColumnConstraints graphicColumn = new ColumnConstraints();
            graphicColumn.setFillWidth(false);
            graphicColumn.setHgrow(Priority.NEVER);
            ColumnConstraints textColumn = new ColumnConstraints();
            textColumn.setFillWidth(true);
            textColumn.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().setAll(graphicColumn, textColumn);
            // Error icon
            FontIcon icon = new FontIcon("enty-squared-cross");
            icon.setIconSize(60);
            icon.setIconColor(Color.valueOf("#B33A3A"));
            StackPane stackPane = new StackPane(icon);
            stackPane.setAlignment(Pos.CENTER);
            grid.add(stackPane, 2, 0);
            // Error title
            Label headerLabel = new Label("Erro!");
            headerLabel.setFont(new Font(20));
            headerLabel.setWrapText(true);
            headerLabel.setAlignment(Pos.CENTER_RIGHT);
            headerLabel.setMaxWidth(Double.MAX_VALUE);
            headerLabel.setMaxHeight(Double.MAX_VALUE);
            grid.add(headerLabel, 0, 0);

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
