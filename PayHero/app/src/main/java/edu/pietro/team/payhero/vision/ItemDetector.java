package edu.pietro.team.payhero.vision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import edu.pietro.team.payhero.social.Item;


public class ItemDetector {

    private Context mCtx;

    private Item mDetectedItem;

    public ItemDetector(Context ctx) {
        mCtx = ctx;
    }

    public void detectItem(final byte[] data) {
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        Frame frame = new Frame.Builder().setBitmap(bmp).build();
        BarcodeDetector bd = new BarcodeDetector.Builder(mCtx).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        SparseArray<Barcode> barcodeDetections = bd.detect(frame);
        for (int i = 0; i < barcodeDetections.size(); ++i) {
            Barcode b = barcodeDetections.valueAt(i);
            Log.d("BARCODE", b.displayValue);
        }
    }

    public void getDetectedItem() {

    }

}
