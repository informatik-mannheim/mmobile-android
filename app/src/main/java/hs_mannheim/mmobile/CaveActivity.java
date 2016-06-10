package hs_mannheim.mmobile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import hs_mannheim.mmobile.Model.KVEServer;
import hs_mannheim.mmobile.Model.PersonalProfile;
import hs_mannheim.mmobile.Model.ProximityDetector;
import hs_mannheim.mmobile.Model.StringStore;
import hs_mannheim.mmobile.Model.UDPClient;

public class CaveActivity extends AppCompatActivity implements ProximityDetector.ProximityListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 117;
    private static final String TAG = "[CaveActivity]";

    private ProximityDetector mProximityDetector;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private RadioButton mRadioBlue;

    private StringStore mStringStore;
    private KVEServer mKVEServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cave);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ein Besuch im Autohaus");
        setSupportActionBar(toolbar);

        String ip = PreferenceManager.getDefaultSharedPreferences(this).getString("ip", null);
        int port = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("port", null));
        mStringStore = new StringStore(ip, port);
        mKVEServer = new KVEServer();

        float threshold = Float.parseFloat(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString("proximity_threshold", "3.0"));

        Log.d(TAG, String.format(Locale.GERMANY, "Threshold is %.2f", threshold));

        mProximityDetector = new ProximityDetector(this, threshold);
        mProximityDetector.registerObserver(this);

        checkPermissions();
        checkGPSStatus();

        mTextView = (TextView) findViewById(R.id.tv_beacon);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRadioBlue = (RadioButton) findViewById(R.id.rbBlue);
    }

    private void checkGPSStatus() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if ( locationManager == null ) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){}
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex){}
        if ( !gps_enabled && !network_enabled ){
            AlertDialog.Builder dialog = new AlertDialog.Builder(CaveActivity.this);
            dialog.setMessage("GPS not enabled");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //this will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mProximityDetector.startScanning();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mProximityDetector.stopScanning();
    }

    @Override
    public void onEntry() {
        notifyCaveServerEntry();
        sendProfileToStringStore();

        mProgressBar.setVisibility(View.INVISIBLE);
        mTextView.setText("Autohaus betreten, sende Profil.");
    }
    @Override
    public void onExit() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText("Ausserhalb des Autohauses.");

        removeProfileFromStringStore();
        notifyCaveServerExit();
    }

    private String getColor() {
        return mRadioBlue.isChecked() ? "blue" : "green";
    }

    private void notifyCaveServerEntry() {
        mKVEServer.send(true, getColor());
    }

    private void notifyCaveServerExit() {
        mKVEServer.send(false, getColor());
    }

    private void sendProfileToStringStore() {
        PersonalProfile profile = load();

        Toast.makeText(this,
                String.format("Profil gepeichert \n\n(%s).",
                profile.toJSON()),
                Toast.LENGTH_LONG).show();

        mStringStore.write("personal_profile", profile.toJSON());
    }

    private void removeProfileFromStringStore() {
        Toast.makeText(this, "Profil entfernt", Toast.LENGTH_LONG).show();

        mStringStore.write("personal_profile", "[]");
    }


    public PersonalProfile load() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String firstName = sharedPrefs.getString("first_name", null);
        String lastName = sharedPrefs.getString("last_name", null);
        String gender = sharedPrefs.getString("gender", "?");
        int age = Integer.parseInt(sharedPrefs.getString("age", "-1"));

        return new PersonalProfile(firstName, lastName, gender, age);
    }
}
