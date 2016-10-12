package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PhiSc on 04.10.2016.
 */

public class AntiTheftService extends Service implements AlarmCallback, UnlockListener {

    private static boolean running = false;

    private SensorManager sensorManager;
    private Sensor accel;
    private AbstractMovementDetector movementDetector;

    private UserPresentBroadcastReceiver unlockReceiver = new UserPresentBroadcastReceiver();

    Uri notification;
    MediaPlayer player;

    private int delay = 5;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
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
                .setContentTitle("Running. Watching!")
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

        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        player = MediaPlayer.create(this, notification);

        unlockReceiver.setListener(this);

        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        movementDetector = new SpikeMovementDetector(this, 2);

        sensorManager.registerListener(movementDetector, accel, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onDestroy()
    {
        Log.d("AntiTheft", "destroyed");
        player.stop();
        sensorManager.unregisterListener(movementDetector, accel);
        running = false;
        unlockReceiver.setListener(null);
    }


    @Override
    public void onDelayStarted() {
        sensorManager.unregisterListener(movementDetector, accel);

        Toast.makeText(this, "Alarm in " + Integer.toString(delay) + " seconds", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.setLooping(true);
                player.start();
            }
        }, 1000 * delay);
    }

    public static boolean isRunning() { return running; }

    @Override
    public void onUnlock() {
        this.stopSelf();
        Log.d("AntiTheft", "unlocked");
    }
}
