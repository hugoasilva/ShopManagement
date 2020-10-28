package pt.shop.management.ui.dialog.button;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import pt.shop.management.ui.dialog.button.MaterialButton.ButtonType;

import java.util.logging.Logger;

/**
 * Material Button Type Converter Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class MaterialButtonTypeConverter extends StyleConverter<String, ButtonType> {

    private MaterialButtonTypeConverter() {
        super();
    }

    // lazy, thread-safe instantiation
    private static class Holder {
        static final MaterialButtonTypeConverter INSTANCE = new MaterialButtonTypeConverter();

        private Holder() {
            throw new IllegalAccessError("Holder class");
        }
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
            Logger.getLogger(MaterialButtonTypeConverter.class.getName()).info(String.format("Invalid button type value '%s'", string));
            return ButtonType.FLAT;
        }
    }

    @Override
    public String toString() {
        return "ButtonTypeConverter";
    }
}
