package edu.pietro.team.payhero.vision;


import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class FaceTracker extends Tracker<Face> {

    private final static String TAG = "FaceTracker";

    public void onNewItem(int id, Face face) {
        Log.i(TAG, "Face detected :o");
    }

    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        if (face.getIsSmilingProbability() > 0.75) {
            Log.i(TAG, "Keep smiling!");
        }
    }

    public void onDone() {
        Log.i(TAG, "Don't leave!");
    }
}