package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by PhiSc on 04.10.2016.
 */

public class AntiTheftService extends Service implements AlarmCallback {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
       //handleCommand(intent);
       // We want this service to continue running until it is explicitly
       // stopped, so return sticky.
       return START_STICKY;
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public void onDelayStarted() {
    }
}
