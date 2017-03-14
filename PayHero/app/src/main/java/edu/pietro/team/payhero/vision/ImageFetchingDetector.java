package edu.pietro.team.payhero.vision;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import edu.pietro.team.payhero.helper.PostHelper;
import edu.pietro.team.payhero.helper.SavePhotoTask;

/**
 * Created by maxim on 14.03.17.
 */

public class ImageFetchingDetector extends Detector {
    Frame lastFrame = null;
    boolean isBusy = false;

    public SparseArray detect (Frame frame) {
        return null;
    }

    @Override
    public void receiveFrame(Frame frame) {
        //super.receiveFrame(frame);
        lastFrame = frame;
        Bitmap b = frame.getBitmap();
        ByteBuffer bb = frame.getGrayscaleImageData();
    }

    @Override
    public boolean isOperational() {
        super.isOperational();
        return true;
    }

    public void tryRecognizeFace() {
        if (!isBusy) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        isBusy = true;
                        //int bytes = lastFrame.getBitmap().getByteCount();
                        //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
                        //int bytes = b.getWidth()*b.getHeight()*4;

                        //ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                        //lastFrame.getBitmap().copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                        int w = lastFrame.getMetadata().getWidth();
                        int h = lastFrame.getMetadata().getHeight();

                        YuvImage yuvimage=new YuvImage(lastFrame.getGrayscaleImageData().array(),
                                ImageFormat.NV21, w, h, null);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        yuvimage.compressToJpeg(new Rect(0, 0, w, h), 100, baos); // Where 100 is the quality of the generated jpeg
                        byte[] jpegArray = baos.toByteArray();

                        new SavePhotoTask().execute(jpegArray);


                        String faceId = PostHelper.detectFace(jpegArray);
                        if (faceId != "") {
                            String personId = PostHelper.identifyFace(faceId);
                            if (personId != "") {
                                Log.d("PERSON", personId);
                                //TODO: initiate payment
                                isBusy = false;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("ERROR", e.toString(), e);
                        isBusy = false;
                    }
                }
            }).start();
        }

    }
}
