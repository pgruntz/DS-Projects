package ch.ethz.inf.vs.a3.udpclient;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import ch.ethz.inf.vs.a2.gruntzp.vsgruntzpchat.R;


public class SettingsActivity extends PreferenceActivity {

    public static String IP = "pref_ip";
    public static String PORT = "pref_port";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Method is deprecated
        addPreferencesFromResource(R.xml.preferences);
    }

}