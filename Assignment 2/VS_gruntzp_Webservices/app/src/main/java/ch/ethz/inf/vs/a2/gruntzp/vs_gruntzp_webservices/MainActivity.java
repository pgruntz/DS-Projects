package ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.ethz.inf.vs.a2.gruntzp.vs_gruntzp_webservices.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRawHttpRequest(View v){
        Intent intent = new Intent(this, RESTactivity.class);
        this.startActivity(intent);
    }

    public void onRestServer(View v){
        Intent intent = new Intent(this, ServerAcitivity.class);
        this.startActivity(intent);
    }

    public void onSoapClient(View v){
        Intent intent = new Intent(this, SoapActivity.class);
        this.startActivity(intent);
    }
}
