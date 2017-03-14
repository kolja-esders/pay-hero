package edu.pietro.team.payhero.vision;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.FocusingProcessor;
import com.google.android.gms.vision.Tracker;

/**
 * Created by maxim on 14.03.17.
 */

public class FirstFocusingProcessor<T> extends FocusingProcessor<T> {

    public FirstFocusingProcessor(Detector<T> d, Tracker<T> t) {
        super(d,t);
    }

    @Override
    public int selectFocus(Detector.Detections detections) {
        return detections.getDetectedItems().keyAt(0);
    }
}
