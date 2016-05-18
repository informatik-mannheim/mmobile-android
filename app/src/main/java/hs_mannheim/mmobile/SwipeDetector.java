package hs_mannheim.mmobile;

import android.database.Observable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Polls the SysPlace server for incoming swipes from the CAS MMobile app.
 */
public class SwipeDetector extends Observable<SwipeDetector.SwipeListener> {
    private static final String KEY = "cas_mmobile_swipe_data";
    private static final int FREQUENCY = 1000;

    private final StringStore mStringStore;
    private Thread mThread;

    public SwipeDetector() {
        mStringStore = new StringStore();
    }

    /**
     * Starts polling for incoming swipes at a given frequency.
     */
    public void start() {
        mThread = new Thread(new LooperRunnable());
        mThread.start();
    }

    public void stop() {
        mThread.interrupt();
    }

    /**
     * Notifies all registered SwipeListeners when a swipe occurred.
     */
    private void notifyListeners() {
        for (final SwipeListener listener : mObservers) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onSwipeDetected();
                }
            });
        }
    }

    /**
     * Runnable that implements the polling mechanism.
     */
    private class LooperRunnable implements Runnable {
        private final String TAG = "SwipeDetector";

        @Override
        public void run() {
            Looper.prepare();

            boolean abort = false;

            while (!abort) {
                try {
                    Thread.sleep(FREQUENCY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    abort = true;
                }

                String result = mStringStore.read(KEY);
                Log.d(TAG, "Fetched result: " + result);

                if (!result.isEmpty()) {
                    notifyListeners();

                    // reset swipe data
                    mStringStore.write(KEY, "");
                }
            }
        }
    }

    /**
     * Implemented by listeners that want to be notified of CAS MMobile Swipe events.
     */
    public interface SwipeListener {
        void onSwipeDetected();
    }
}
