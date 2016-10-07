package ch.ethz.inf.vs.a1.gruntzp.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static PackageManager mPackageMgr;
    public static BluetoothAdapter mBluetoothAdapter;
    public static LocationManager mLocationMgr;

    //Needed as "request code" that is returned by the callback
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int PERMISSION_REQUEST_FINE_LOC = 2;
    private final static int REQUEST_ENABLE_LOC = 3;

    //Int to check wether location services weren't enabled and subsequently quit the app. Must not be static!
    public int locDenied = 0;

    private boolean mScanning;
    private Handler mHandler; //Needed for delayed cancel of BLE search
    private static final long SCAN_PERIOD = 10000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPackageMgr = getPackageManager();


        //Used to check whether BLE is available, if not quit.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //Get bluetoothmanager to obtain bluetoothadapter, which is in turn used to request bluetooth services.
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            mLocationMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }


    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Check if we already have ACCESS_FINE_LOCATION permission, if not request it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOC);
        }


        //Ask to enable bluetooth, start activity for it and get result.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        if (mBluetoothAdapter.isEnabled() && !mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocationServices();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Check if bluetooth was indeed enabled
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.bluetooth_enabled, Toast.LENGTH_SHORT).show();
                getLocationServices(); //Only request location servies if bluetooth is enabled
            } else { //Bluetooth not enabled, end app
                Toast.makeText(this, R.string.bluetooth_not_enabled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_FINE_LOC) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Permission granted


            } else {
                Toast.makeText(this, R.string.fine_loc_denied, Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    public void getLocationServices() {

        if (!mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(locDenied == 1) { //Second time we're here, abort
                Toast.makeText(this, R.string.loc_not_enabled, Toast.LENGTH_SHORT).show();
                finish();
                return;
            } else {
                ++locDenied;
            }
            Intent enableLocIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(enableLocIntent);
        } else {//Location services enabled!
            Toast.makeText(this, R.string.loc_enabled, Toast.LENGTH_SHORT).show();
            locDenied = 2;
            //Initiate search for BLE devices

        }

    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        
    }



}
