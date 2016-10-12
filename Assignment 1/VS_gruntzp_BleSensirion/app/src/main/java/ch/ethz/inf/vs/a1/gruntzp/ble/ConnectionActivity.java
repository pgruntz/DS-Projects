package ch.ethz.inf.vs.a1.gruntzp.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;


public class ConnectionActivity extends AppCompatActivity {

    private GraphView graph;


    private static LineGraphSeries<DataPoint> temp;
    private static LineGraphSeries<DataPoint> hum;

    static long timestamp = 0;

    private static boolean[] notification_enabled = {false, false};


    BluetoothDevice mDevice;
    BluetoothGatt mGatt;

    private static UUID lastUuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        graph = (GraphView) findViewById(R.id.graph);

        temp =  new LineGraphSeries<>();
        hum  = new LineGraphSeries<>();
        temp.setTitle("Temperature [°C]");
        hum.setTitle("Humidity [%Rh]");

        graph.addSeries(temp);
        graph.addSeries(hum);

        temp.setColor(BLUE);
        temp.setDataPointsRadius(50);
        temp.setBackgroundColor(BLUE);


        hum.setColor(GREEN);
        hum.setDataPointsRadius(50);
        hum.setBackgroundColor(GREEN);

        Viewport vp = graph.getViewport();
        vp.setScrollable(true);
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(20);


        Bundle extras = getIntent().getExtras();
        mDevice = extras.getParcelable("Device");



        //graphContainer = new GraphContainerImpl(graph);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("[s]");
        graph.getLegendRenderer().setVisible(true);


        timestamp = System.nanoTime();





    }

    @Override
    protected void onResume() {
        super.onResume();



        mGatt = mDevice.connectGatt(this, false, mGattCallback);


        if(mGatt == null) {
            //Connection failed
            finish();
        }




    }

    @Override
    protected void onPause() {
        super.onPause();
        notification_enabled[1] = false;
        notification_enabled[0] = false;
        mGatt.close();
        finish();
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(newState == BluetoothGatt.STATE_DISCONNECTED) {
                //Device was disconnected
                finish();
            } else if (newState == BluetoothGatt.STATE_CONNECTED) {
                if(!gatt.discoverServices()) {
                    //Discovery failed
                    finish();
                }
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if(status != BluetoothGatt.GATT_SUCCESS) {
                //Discovery failed
                finish();
            } else {


                BluetoothGattService tempService = gatt.getService(SensirionSHT31UUIDS.UUID_TEMPERATURE_SERVICE);

                if(tempService == null) {
                    //Couldn't get services
                    finish();
                }

                boolean tc = tempService.addCharacteristic(new BluetoothGattCharacteristic(SensirionSHT31UUIDS.UUID_TEMPERATURE_CHARACTERISTIC,BluetoothGattCharacteristic.PROPERTY_READ,BluetoothGattCharacteristic.PERMISSION_READ));

                if(tc) {
                    tc = gatt.setCharacteristicNotification(tempService.getCharacteristic(SensirionSHT31UUIDS.UUID_TEMPERATURE_CHARACTERISTIC), true);

                    if(tc) {
                        tc = w_desc(gatt, tempService, SensirionSHT31UUIDS.UUID_TEMPERATURE_CHARACTERISTIC, 0);
                        //Call on humidity characteristic from descriptor write callback
                    } else {
                        //Couldn't add notification
                        finish();
                    }
                } else {
                    //Couldn't add characteristics
                    finish();
                }

            }

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if (!characteristic.getUuid().equals(lastUuid)) {
                lastUuid = characteristic.getUuid();

                float newVal = convertRawValue(characteristic.getValue());
                final float currTime = Long.valueOf(System.nanoTime() - timestamp).floatValue()/1000000000f;

                DataPoint nd = new DataPoint(currTime,newVal);

                        if (characteristic.getUuid().equals(SensirionSHT31UUIDS.UUID_TEMPERATURE_CHARACTERISTIC)) {
                            temp.appendData(nd,true,100);
                        } else {
                            hum.appendData(nd,true,100);
                        }


                   // }
                //});
            }


        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

            //First initialization has happened, attempt for humidity
            if(notification_enabled[1] == false) {
                BluetoothGattService humService = gatt.getService(SensirionSHT31UUIDS.UUID_HUMIDITY_SERVICE);
                if (humService == null) {
                    //Couldn't get services
                    finish();
                }
                boolean hc = humService.addCharacteristic(new BluetoothGattCharacteristic(SensirionSHT31UUIDS.UUID_HUMIDITY_CHARACTERISTIC, BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ));

                if (hc) {
                    hc = gatt.setCharacteristicNotification(humService.getCharacteristic(SensirionSHT31UUIDS.UUID_HUMIDITY_CHARACTERISTIC), true);

                    if (hc) {
                        hc = w_desc(gatt, humService, SensirionSHT31UUIDS.UUID_HUMIDITY_CHARACTERISTIC, 1);
                    } else {
                        //Couldn't add notification
                        finish();
                    }
                } else {
                    //Couldn't add characteristics
                    finish();
                }
            }

        }
    };

    private boolean w_desc(BluetoothGatt gatt, BluetoothGattService service, UUID uuid, int number) {
        BluetoothGattDescriptor mDesc = service.getCharacteristic(uuid).getDescriptor(SensirionSHT31UUIDS.NOTIFICATION_DESCRIPTOR_UUID);
        mDesc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        notification_enabled[number] = gatt.writeDescriptor(mDesc);
        return notification_enabled[number];

    }

    private float convertRawValue(byte[] raw) {

        ByteBuffer wrapper = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);

        return wrapper.getFloat();

    }
}
