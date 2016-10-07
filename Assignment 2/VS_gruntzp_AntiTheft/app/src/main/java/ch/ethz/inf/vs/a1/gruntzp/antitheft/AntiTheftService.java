package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Debug;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by PhiSc on 04.10.2016.
 */

public class AntiTheftService extends Service implements AlarmCallback {
    private SensorManager sensorManager;
    private Sensor accel;
    private MovementDetector movementDetector;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //handleCommand(intent);
        Intent resultIntent = new Intent(this, MainActivity.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.fav57)
                .setContentTitle("Theft Alarm!")
                .setContentText("THEFT ALARM!")
                .setCategory(Notification.CATEGORY_ALARM)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setAutoCancel(false)
                .setVibrate(new long[] {100, 100, 100, 100});
        notBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;


        // Builds the notification and issues it.
        //MainActivity.mNotifyMgr.notify(mNotificationId, notBuilder.build());
        this.startForeground(mNotificationId, notBuilder.build());


       return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        movementDetector = new MovementDetector(this, 10);

        sensorManager.registerListener(movementDetector, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy()
    {
        sensorManager.unregisterListener(movementDetector, accel);
    }

    @Override
    public void onDelayStarted() {

    }
}
