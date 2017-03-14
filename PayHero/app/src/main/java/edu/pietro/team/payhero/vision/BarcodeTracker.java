package edu.pietro.team.payhero.vision;

import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by maxim on 14.03.17.
 */

public class BarcodeTracker extends Tracker<Barcode> {
    private final static String TAG = "BarcodeTracker";

    public void onNewItem(int id, Barcode face) {
        Log.i(TAG, "Barcode entered.");
    }

    public void onUpdate(Detector.Detections<Barcode> detections, Barcode face) {
    }

    public void onDone() {
        Log.i(TAG, "Barcode left.");
    }
}
