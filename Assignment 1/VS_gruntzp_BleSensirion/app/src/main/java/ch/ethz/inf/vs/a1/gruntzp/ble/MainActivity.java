package ch.ethz.inf.vs.a1.gruntzp.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Handler;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    public static PackageManager mPackageMgr;
    public static BluetoothAdapter mBluetoothAdapter;
    public static LocationManager mLocationMgr;

    //Needed as "request code" that is returned by the callback
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int PERMISSION_REQUEST_FINE_LOC = 2;

    //Int to check wether location services weren't enabled and subsequently quit the app. Must not be static!
    public int locDenied;

    private boolean mScanning;
    private final Handler mHandler = new Handler(); //Needed for delayed cancel of BLE search
    private static final long SCAN_PERIOD = 20000;

    //Will hold results of ble scan
    private ArrayList<BluetoothDevice> listDevices;
    private ArrayList<String> listDeviceNames;



    //Used to dynamically update the listview of ble scan results
    private static ArrayAdapter<String> mArrayAdapter;


    BluetoothLeScanner mBleScanner;

    private AdapterView.OnItemClickListener mClickedHandler = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView parent, View v, int position, long id) {
            if(mScanning) {
                mScanning = false;
                mBleScanner.stopScan(mScanCallback);

            }
            BluetoothDevice dev = listDevices.get(position);

            Intent intent = new Intent(getApplicationContext(), ConnectionActivity.class);
            intent.putExtra("Device", dev);

            startActivity(intent);

        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPackageMgr = getPackageManager();
        locDenied = 0;



        listDeviceNames = new ArrayList<String>();
        listDevices = new ArrayList<BluetoothDevice>();

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listDeviceNames);

        ListView listView = (ListView) findViewById(R.id.listDeviceNames);
        listView.setOnItemClickListener(mClickedHandler);
        listView.setAdapter(mArrayAdapter);





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
        } else if (mBluetoothAdapter.isEnabled() && !mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, R.string.get_loc, Toast.LENGTH_SHORT).show();
            getLocationServices();
        } else {

            scanLeDevice();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Check if bluetooth was indeed enabled
            if (resultCode == RESULT_OK) {
            } else { //Bluetooth not enabled, end app
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_FINE_LOC) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Permission granted, do nothing

            } else {
                finish();
            }

        }
    }

    public void getLocationServices() {
        Intent enableLocIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(enableLocIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();



        if(mScanning) {
            mScanning = false;
            mBleScanner.stopScan(mScanCallback);
        }

        listDevices.clear();
        listDeviceNames.clear();
        mArrayAdapter.notifyDataSetChanged();
    }

    private void scanLeDevice() {
        mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();


        //Implement a scan filter to only look for Sensirion HumiGadget devices
        ScanFilter.Builder mScanFilterBuilder = new ScanFilter.Builder();
        mScanFilterBuilder.setDeviceName(getResources().getString(R.string.dev_name));
        ScanFilter mScanFilter = mScanFilterBuilder.build();
        ArrayList<ScanFilter> mFilters = new ArrayList<ScanFilter>();
        mFilters.add(mScanFilter);
        ScanSettings mSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();





        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                if(mScanning) {
                    mScanning = false;
                    mBleScanner.stopScan(mScanCallback);
                }
            }
        }, SCAN_PERIOD);

        mScanning = true;
        mBleScanner.startScan(mFilters, mSettings, mScanCallback);
    }

    final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            int index;
            boolean deviceAdded;
            if (callbackType == 4) { //Previously matched device lost
                index = listDevices.indexOf(result.getDevice());
                if(!(index == -1)) { //Device has to be removed
                    listDevices.remove(index);
                    listDeviceNames.remove(index);
                    mArrayAdapter.notifyDataSetChanged();
                }
            } else { //New device found

                boolean duplicate = false;
//                for (BluetoothDevice d : listDevices) {
//                    if(d.getAddress().equals(result.getDevice().getAddress()))
//                        duplicate = true;
//                }
                deviceAdded = listDevices.add(result.getDevice());
                if (deviceAdded) {
                    listDeviceNames.add(listDevices.get(listDevices.size()-1).getName());
                    mArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };





}
