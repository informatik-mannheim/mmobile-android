package hs_mannheim.mmobile;

import android.content.Context;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Utils;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.List;

public class ProximityDetector {
    private static final String TAG = "EddystoneScan";
    private static final String EDDYSTONE_NS = "edd1ebeac04e5defa017";
    private static final String EDDYSTONE_INS = "c80743ea119d";

    private String mScanId;

    private BeaconManager mBeaconManager;

    public ProximityDetector(Context context) {
        mBeaconManager = new BeaconManager(context);

        mBeaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> list) {
                for (Eddystone e : list) {
                    if (e.instance != null
                            && e.instance.equals(EDDYSTONE_INS)
                            && e.namespace.equals(EDDYSTONE_NS)) {
                        handle(e);
                    }
                }
            }
        });
    }

    private void handle(Eddystone eddystone) {
        Log.d(TAG, "Found Eddystone "
                + eddystone.instance +
                " at distance "
                + Double.toString(Utils.computeAccuracy(eddystone)));

        //TODO: Do distance approximation over time.
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
}
