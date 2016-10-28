package by.bsu.dektiarev.util;

import java.util.ResourceBundle;

/**
 * Created by USER on 28.10.2016.
 */
public final class OptionsManager {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("options");

    public static String getProperty(String key) {
        return BUNDLE.getString(key);
    }

    private OptionsManager() {
    }
}
