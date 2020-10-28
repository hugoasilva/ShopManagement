package pt.shop.management.ui.dialog.dialog;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/**
 * Converts the CSS for -fx-dialog-transition items into DialogTransition.
 * it's used in JFXDialog.
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class MaterialDialogTransitionConverter extends StyleConverter<String, MaterialDialog.MaterialDialogTransition> {
    // lazy, thread-safe instatiation
    private static class Holder {
        static final MaterialDialogTransitionConverter INSTANCE = new MaterialDialogTransitionConverter();
    }

    public static StyleConverter<String, MaterialDialog.MaterialDialogTransition> getInstance() {
        return MaterialDialogTransitionConverter.Holder.INSTANCE;
    }

    private MaterialDialogTransitionConverter() {
    }

    @Override
    public MaterialDialog.MaterialDialogTransition convert(ParsedValue<String, MaterialDialog.MaterialDialogTransition> value, Font not_used) {
        String string = value.getValue();
        try {
            return MaterialDialog.MaterialDialogTransition.valueOf(string);
        } catch (IllegalArgumentException | NullPointerException exception) {
            return MaterialDialog.MaterialDialogTransition.CENTER;
        }
    }

    @Override
    public String toString() {
        return "DialogTransitionConverter";
    }
}
