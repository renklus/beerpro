package ch.beerpro.presentation.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import ch.beerpro.R;
import ch.beerpro.presentation.utils.ThemeHelpers;

public class PreferencesActivity extends PreferenceActivity {

    private final SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, s) -> {
                    finish();
                    startActivity(getIntent());
                };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PrefsFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        ThemeHelpers.setTheme(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getApplication())
                .registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getApplication())
                .unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}

