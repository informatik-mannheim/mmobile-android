package hs_mannheim.mmobile;

import android.content.Context;
import android.database.Observable;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Utils;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.ArrayList;
import java.util.List;

/**
 * Scans for an Eddystone with a certain Namespace and Instance ID and notifies listeners when
 * it was found below the provided threshold.
 */
public class ProximityDetector extends Observable<ProximityDetector.ProximityListener> {
    private static final String TAG = "EddystoneScan";
    private static final String EDDYSTONE_NS = "edd1ebeac04e5defa017";
    private static final String EDDYSTONE_INS = "c80743ea119d";
    private static final int NUM_SAMPLES = 5;
    private static final int SCAN_PERIOD_MILLIS = 500;

    private String mScanId;
    private BeaconManager mBeaconManager;

    private List<Double> mValues = new ArrayList<>();
    private double mThresholdDistance;

    public ProximityDetector(Context context, double thresholdDistance) {
        mThresholdDistance = thresholdDistance;

        mBeaconManager = new BeaconManager(context);
        mBeaconManager.setForegroundScanPeriod(SCAN_PERIOD_MILLIS, 0);
        mBeaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> list) {
                for (Eddystone e : list) {
                    if (e.instance != null
                            && e.namespace != null
                            && e.instance.equals(EDDYSTONE_INS)
                            && e.namespace.equals(EDDYSTONE_NS)) {
                        handle(e);
                    }
                }
            }
        });
    }

    /**
     * Calculate distance to Eddystone and calculate mean if enough values have been stored.
     *
     * @param eddystone The Eddystone to which to determine the distance.
     */
    private void handle(Eddystone eddystone) {
        Log.d(TAG, "Found Eddystone "
                + eddystone.instance +
                " at distance "
                + Double.toString(Utils.computeAccuracy(eddystone)));

        mValues.add(Utils.computeAccuracy(eddystone));

        if (mValues.size() == NUM_SAMPLES) {
            Double mean = calculateArithmeticMean(mValues);
            mValues.clear();

            Log.d(TAG, "Mean calculated: " + Double.toString(mean));

            if (mean < mThresholdDistance) {
                notifyListeners();
            }
        }
    }

    /**
     * Notify all listeners that an Eddystone has been found in range of the threshold.
     */
    private void notifyListeners() {
        for (ProximityListener listener : mObservers) {
            listener.onApproach();
        }
    }


    /**
     * Calculates the arithmetic mean for an array of doubles.
     *
     * @param numbers The numbers to calculate the mean for.
     * @return The arithmetic mean.
     */
    private static Double calculateArithmeticMean(List<Double> numbers) {
        Double sum = 0.0d;

        for (Double d : numbers) {
            sum += d;
        }

        return sum / numbers.size();
    }

    /**
     * Starts scanning for Eddystones matching Namespace and Instance ID.
     */
    public void startScanning() {
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                mScanId = mBeaconManager.startEddystoneScanning();
            }
        });
    }

    /**
     * Stops scanning for Eddystones.
     */
    public void stopScanning() {
        mBeaconManager.stopEddystoneScanning(mScanId);
    }

    public interface ProximityListener {
        void onApproach();
    }
}
