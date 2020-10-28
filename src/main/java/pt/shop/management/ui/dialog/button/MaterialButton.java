package pt.shop.management.ui.dialog.button;

import com.jfoenix.assets.JFoenixResources;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Material Button Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class MaterialButton extends Button {

    private static final String DEFAULT_STYLE_CLASS = "material-button";
    private static final String USER_AGENT_STYLESHEET = JFoenixResources.load("/css/styles.css").toExternalForm();
    private ObjectProperty<Paint> ripplerFill = new SimpleObjectProperty<>(null);
    private StyleableObjectProperty<ButtonType> buttonType = new SimpleStyleableObjectProperty<>(
            StyleableProperties.BUTTON_TYPE,
            this,
            "buttonType",
            ButtonType.FLAT);
    private StyleableBooleanProperty disableVisualFocus = new SimpleStyleableBooleanProperty(StyleableProperties.DISABLE_VISUAL_FOCUS,
            this,
            "disableVisualFocus",
            true);

    /**
     * Material button constructor
     */
    public MaterialButton() {
        initialize();
        // init in scene builder workaround ( TODO : remove when JFoenix is well integrated in scenebuilder by gluon )
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length && i < 15; i++) {
            if (stackTraceElements[i].getClassName().toLowerCase().contains(".scenebuilder.kit.fxom.")) {
                this.setText("Button");
                break;
            }
        }
    }

    /**
     * Material button with text constructor
     */
    public MaterialButton(String text) {
        super(text);
        initialize();
    }

    /**
     * Material button with text and image constructor
     */
    public MaterialButton(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }

    private void initialize() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /**
     * Create new material button skin
     *
     * @return - material button skin
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new MaterialButtonSkin(this);
    }

    /**
     * Get CSS file
     */
    @Override
    public String getUserAgentStylesheet() {
        return USER_AGENT_STYLESHEET;
    }

    /**
     * Get ripple color
     *
     * @return - ripple color
     */
    public final ObjectProperty<Paint> ripplerFillProperty() {
        return this.ripplerFill;
    }

    /**
     * Get ripple color
     *
     * @return - ripple color
     */
    public final Paint getRipplerFill() {
        return this.ripplerFillProperty().get();
    }

    /**
     * Set ripple color
     *
     * @param ripplerFill - color of the ripple effect
     */
    public final void setRipplerFill(final Paint ripplerFill) {
        this.ripplerFillProperty().set(ripplerFill);
    }

    /**
     * Get button type
     *
     * @return - button type
     */
    public ButtonType getButtonType() {
        return buttonType == null ? ButtonType.FLAT : buttonType.get();
    }

    /**
     * Set button type
     *
     * @param type - button type
     */
    public void setButtonType(ButtonType type) {
        this.buttonType.set(type);
    }

    /**
     * Get button type
     *
     * @return - button type
     */
    public StyleableObjectProperty<ButtonType> buttonTypeProperty() {
        return this.buttonType;
    }

    /**
     * Disable button from showing keyboard focus
     *
     * @return - property to disable focus
     */
    public final StyleableBooleanProperty disableVisualFocusProperty() {
        return this.disableVisualFocus;
    }

    /**
     * Indicate if button will show keyboard focus
     *
     * @return true if button has focus disabled, false otherwise
     */
    public final Boolean isDisableVisualFocus() {
        return disableVisualFocus != null && this.disableVisualFocusProperty().get();
    }

    /**
     * Set keyboard focus for button
     *
     * @param disabled - true if disabled, false otherwise
     */
    public final void setDisableVisualFocus(final Boolean disabled) {
        this.disableVisualFocusProperty().set(disabled);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public enum ButtonType {FLAT, RAISED}

    /**
     * Styleable Properties Class
     */
    private static class StyleableProperties {
        private static final CssMetaData<MaterialButton, ButtonType> BUTTON_TYPE =
                new CssMetaData<MaterialButton, ButtonType>("-jfx-button-type",
                        MaterialButtonTypeConverter.getInstance(), ButtonType.FLAT) {
                    @Override
                    public boolean isSettable(MaterialButton control) {
                        return control.buttonType == null || !control.buttonType.isBound();
                    }

                    @Override
                    public StyleableProperty<ButtonType> getStyleableProperty(MaterialButton control) {
                        return control.buttonTypeProperty();
                    }
                };

        private static final CssMetaData<MaterialButton, Boolean> DISABLE_VISUAL_FOCUS =
                new CssMetaData<MaterialButton, Boolean>("-jfx-disable-visual-focus",
                        BooleanConverter.getInstance(), false) {
                    @Override
                    public boolean isSettable(MaterialButton control) {
                        return control.disableVisualFocus == null || !control.disableVisualFocus.isBound();
                    }

                    @Override
                    public StyleableBooleanProperty getStyleableProperty(MaterialButton control) {
                        return control.disableVisualFocusProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(Button.getClassCssMetaData());
            Collections.addAll(styleables, BUTTON_TYPE, DISABLE_VISUAL_FOCUS);
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }


}
