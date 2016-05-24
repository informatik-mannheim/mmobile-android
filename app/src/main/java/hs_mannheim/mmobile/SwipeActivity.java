package hs_mannheim.mmobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hs_mannheim.mmobile.Model.SwipeDetector;

public class SwipeActivity extends AppCompatActivity implements SwipeDetector.SwipeListener {

    private SwipeDetector mSwipeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

}
