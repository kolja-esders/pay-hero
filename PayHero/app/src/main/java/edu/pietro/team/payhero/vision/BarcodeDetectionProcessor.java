package edu.pietro.team.payhero.vision;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeDetectionProcessor implements Detector.Processor<Barcode> {

    private final static String TAG = "BarcodeDetectionProc.";

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        SparseArray<Barcode> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            Barcode item = items.valueAt(i);
            Log.d(TAG, item.displayValue);
        }
    }

    @Override
    public void release() {
    }
}
