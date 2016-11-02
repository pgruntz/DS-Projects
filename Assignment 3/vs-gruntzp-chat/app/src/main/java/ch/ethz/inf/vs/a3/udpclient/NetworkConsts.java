package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ch.ethz.inf.vs.a2.gruntzp.vsgruntzpchat.R;

public final class NetworkConsts {

    public static SharedPreferences sharedPref;
    public static String Server_Adress()
    {
        String preference = sharedPref.getString(SettingsActivity.IP, null);
        return preference;
    }

    public static int UDP_Port()
    {
        String preference = sharedPref.getString(SettingsActivity.PORT, "");
        return Integer.parseInt(preference);
    }

    /**
     * UDP Port
     */
    //public static int UDP_PORT = 4446;

    /**
     * Address of the chat server
     *
     * This address is for the emulator.
     */
    //public static String SERVER_ADDRESS = "10.0.2.2";

    /**
     * Size of UDP payload in bytes
     */
    public static int PAYLOAD_SIZE = 1024;

    /**
     * Time to wait for a message in ms
     */
    public static int SOCKET_TIMEOUT = 10000;
}
