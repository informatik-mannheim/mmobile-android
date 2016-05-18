package hs_mannheim.mmobile;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwipeDetector.SwipeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SwipeDetector swipeDetector = new SwipeDetector();
        swipeDetector.registerObserver(this);
        swipeDetector.start();
    }

    @Override
    public void onSwipeDetected() {
        Toast.makeText(this, "SWIPE!", Toast.LENGTH_LONG).show();
    }
}
