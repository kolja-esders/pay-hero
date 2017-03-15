package edu.pietro.team.payhero.vision;


import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

import org.greenrobot.eventbus.EventBus;

import edu.pietro.team.payhero.MainActivity;
import edu.pietro.team.payhero.event.OnStartDetectionPostProcessing;
import edu.pietro.team.payhero.helper.ProcessingState;

public class FaceTracker extends Tracker<Face> {

    private final static String TAG = "FaceTracker";
    private ImageFetchingDetector mImageFetchingDetector;

    public FaceTracker(ImageFetchingDetector ifd) {
        mImageFetchingDetector = ifd;
    }

    public void onNewItem(int id, Face face) {
        Log.i(TAG, "Face entered.");
        if (MainActivity.getCurrentActivity().onTryStartProcessing(ProcessingState.FACE_LOCK)) {
            EventBus.getDefault().post(new OnStartDetectionPostProcessing("Processing face..."));
            mImageFetchingDetector.tryRecognizeFace();
        }
    }

    public void onUpdate(Detector.Detections<Face> detections, Face face) {

    }

    public void onDone() {
        Log.i(TAG, "Face left.");
    }
}