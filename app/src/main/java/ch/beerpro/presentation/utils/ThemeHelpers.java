package ch.beerpro.presentation.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ch.beerpro.R;

public class ThemeHelpers {
    public static int setTheme(Activity activity) {
        return setTheme(activity, R.style.AppTheme, R.style.DarkAppTheme);
    }

    public static int setTheme(Activity activity, int lightTheme, int darkTheme) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplication());
        boolean useDarkTheme = settings.getBoolean(activity.getString(R.string.preferences_use_dark_theme_key), false);
        int theme = useDarkTheme ? darkTheme : lightTheme;
        activity.setTheme(theme);
        return theme;
    }
}
