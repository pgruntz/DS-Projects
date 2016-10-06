package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    public static NotificationManager nManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClickToggle(View v){
        ToggleButton t = (ToggleButton) v;
        if (t.isChecked()){
            AlarmCallback ac = (AlarmCallback) new TheftService();
            ac.onDelayStarted();
            Test();
        }
    }

    private void Test(){
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
                .setContentText("THEFT ALARM!");
        //.setCategory(Notification.CATEGORY_ALARM)
        //.setPriority(Notification.PRIORITY_MAX)
        //.setVisibility(Notification.VISIBILITY_PUBLIC)
        //.setVibrate(new long[] {100, 100, 100, 100});
        notBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, notBuilder.build());
    }
}
