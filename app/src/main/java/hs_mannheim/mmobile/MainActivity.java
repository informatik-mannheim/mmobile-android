package hs_mannheim.mmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwipeDetector.SwipeListener {

    private SwipeDetector mSwipeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_main);

        mSwipeDetector = new SwipeDetector();
        mSwipeDetector.registerObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSwipeDetector.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSwipeDetector.stop();
    }

    @Override
    public void onSwipeDetected() {
        Toast.makeText(this, "SWIPE!", Toast.LENGTH_LONG).show();
    }

    public void goToBeaconActivity(View view) {
        mSwipeDetector.stop();

        startActivity(new Intent(this, CaveActivity.class));
    }
}
