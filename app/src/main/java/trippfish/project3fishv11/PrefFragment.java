package trippfish.project3fishv11;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Tripp on 3/25/2016.
 */
public class PrefFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
