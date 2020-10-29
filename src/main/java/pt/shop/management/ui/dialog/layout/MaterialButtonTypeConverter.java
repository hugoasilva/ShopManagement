package pt.shop.management.ui.dialog.layout;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.shop.management.ui.dialog.layout.MaterialButton.ButtonType;

/**
 * Material Button Type Converter Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class MaterialButtonTypeConverter extends StyleConverter<String, ButtonType> {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(MaterialButtonTypeConverter.class.getName());

    private MaterialButtonTypeConverter() {
        super();
    }

    public static StyleConverter<String, ButtonType> getInstance() {
        return MaterialButtonTypeConverter.Holder.INSTANCE;
    }

    @Override
    public ButtonType convert(ParsedValue<String, ButtonType> value, Font notUsedFont) {
        String string = value.getValue();
        try {
            return ButtonType.valueOf(string);
        } catch (IllegalArgumentException | NullPointerException exception) {
            LOGGER.log(Level.ERROR, "{}", "Material Button Exception: " + String.format("Invalid button type value '%s'", string));
            return ButtonType.FLAT;
        }
    }

    @Override
    public String toString() {
        return "ButtonTypeConverter";
    }

    // lazy, thread-safe instantiation
    private static class Holder {
        static final MaterialButtonTypeConverter INSTANCE = new MaterialButtonTypeConverter();

        private Holder() {
            throw new IllegalAccessError("Holder class");
        }
    }
}
