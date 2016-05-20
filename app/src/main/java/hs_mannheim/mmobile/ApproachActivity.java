package hs_mannheim.mmobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Utils;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.List;

public class ApproachActivity extends AppCompatActivity implements ProximityDetector.ProximityListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 117;

    private ProximityDetector mProximityDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approach);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProximityDetector = new ProximityDetector(this, 1.5);
        mProximityDetector.registerObserver(this);
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

        checkPermissions();
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
    }
}
