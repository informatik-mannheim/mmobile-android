package hs_mannheim.mmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwipeDetector.SwipeListener {

    private SwipeDetector mSwipeDetector = new SwipeDetector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSwipeDetector.registerObserver(this);
        mSwipeDetector.start();
    }

    @Override
    public void onSwipeDetected() {
        Toast.makeText(this, "SWIPE!", Toast.LENGTH_LONG).show();
    }

    public void goToBeaconActivity(View view) {
        mSwipeDetector.stop();
        startActivity(new Intent(this, ApproachActivity.class));
    }
}
