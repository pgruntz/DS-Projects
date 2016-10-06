package ch.ethz.inf.vs.a1.gruntzp.antitheft;

import android.app.NotificationManager;
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
        }
    }
}
