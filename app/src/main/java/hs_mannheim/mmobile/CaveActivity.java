package hs_mannheim.mmobile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class CaveActivity extends AppCompatActivity implements ProximityDetector.ProximityListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 117;

    private ProximityDetector mProximityDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cave);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProximityDetector = new ProximityDetector(this, 1.5);
        mProximityDetector.registerObserver(this);

        checkPermissions();

        PersonalProfile profile = new PersonalProfile("Horst", "Schneider", 'm', 32);
        save(profile);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
    public void onApproach() {
        Toast.makeText(this, "BEACON DETECTED", Toast.LENGTH_LONG).show();

        notifyCaveServer();
        sendProfileToStringStore();
    }

    private void notifyCaveServer() {

    }

    private void sendProfileToStringStore() {
        String profile = load();
        new StringStore().write("personal_profile", profile);

    }

    public void save(PersonalProfile profile) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.personal_profile), profile.toJSON());
        editor.commit();
    }

    public String load() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.personal_profile), "");
    }
}
