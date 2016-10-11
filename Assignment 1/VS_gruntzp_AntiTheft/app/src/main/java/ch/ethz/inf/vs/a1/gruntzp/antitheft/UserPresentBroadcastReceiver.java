package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.content.BroadcastReceiver;

/**
 * Created by PhiSc on 11.10.2016.
 */

import android.content.Context;
import android.content.Intent;

public class UserPresentBroadcastReceiver extends BroadcastReceiver {

    private static UnlockListener unlockListener;
    public void setListener(UnlockListener o)
    {
        unlockListener = o;
    }

    @Override
    public void onReceive(Context arg0, Intent intent) {
        /*Sent when the user is present after
         * device wakes up (e.g when the keyguard is gone)
         * */
        if (unlockListener != null)
            unlockListener.onUnlock();
    }

}
