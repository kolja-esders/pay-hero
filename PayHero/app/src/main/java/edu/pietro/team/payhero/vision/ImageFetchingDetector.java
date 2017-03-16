package edu.pietro.team.payhero.vision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import edu.pietro.team.payhero.MainActivity;
import edu.pietro.team.payhero.entities.AmountOfMoney;
import edu.pietro.team.payhero.event.OnErrorDuringDetectionPostProcessing;
import edu.pietro.team.payhero.event.OnPaymentInit;
import edu.pietro.team.payhero.helper.AddressBook;
import edu.pietro.team.payhero.helper.PostHelper;
import edu.pietro.team.payhero.helper.ProcessingState;
import edu.pietro.team.payhero.helper.SavePhotoTask;
import edu.pietro.team.payhero.social.MoneyTransfer;
import edu.pietro.team.payhero.social.User;

/**
 * Created by maxim on 14.03.17.
 */

public class ImageFetchingDetector extends Detector {
    Frame lastFrame = null;

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

    private static byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;
        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
                * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        return yuv;
    }

    public static byte[] rotateYUV420Degree270(byte[] data, int imageWidth,
                                               int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvHeight = 0;
        if (imageWidth != nWidth || imageHeight != nHeight) {
            nWidth = imageWidth;
            nHeight = imageHeight;
            wh = imageWidth * imageHeight;
            uvHeight = imageHeight >> 1;// uvHeight = height / 2
        }
        // ??Y
        int k = 0;
        for (int i = 0; i < imageWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < imageHeight; j++) {
                yuv[k] = data[nPos + i];
                k++;
                nPos += imageWidth;
            }
        }
        for (int i = 0; i < imageWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                yuv[k] = data[nPos + i];
                yuv[k + 1] = data[nPos + i + 1];
                k += 2;
                nPos += imageWidth;
            }
        }
        return rotateYUV420Degree180(yuv, imageWidth, imageHeight);
    }

    static private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight)
    {
        byte [] yuv = new byte[imageWidth*imageHeight*3/2];
        // Rotate the Y luma
        int i = 0;
        for(int x = 0;x < imageWidth;x++)
        {
            for(int y = imageHeight-1;y >= 0;y--)
            {
                yuv[i] = data[y*imageWidth+x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth*imageHeight*3/2-1;
        for(int x = imageWidth-1;x > 0;x=x-2)
        {
            for(int y = 0;y < imageHeight/2;y++)
            {
                yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
                i--;
                yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
                i--;
            }
        }
        return yuv;
    }

    public void tryRecognizeFace() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //int bytes = lastFrame.getBitmap().getByteCount();
                    //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
                    //int bytes = b.getWidth()*b.getHeight()*4;

                    //ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                    //lastFrame.getBitmap().copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                    int w = lastFrame.getMetadata().getWidth();
                    int h = lastFrame.getMetadata().getHeight();

                    byte[] rotatedYuvBytes = rotateYUV420Degree90(lastFrame.getGrayscaleImageData().array(),w,h);
                    //YuvImage yuvimage=new YuvImage(lastFrame.getGrayscaleImageData().array(),
                    //        ImageFormat.NV21, w, h, null);
                    YuvImage yuvimage=new YuvImage(rotatedYuvBytes,
                            ImageFormat.NV21, h,w, null);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //yuvimage.compressToJpeg(new Rect(0, 0, w, h), 100, baos); // Where 100 is the quality of the generated jpeg
                    yuvimage.compressToJpeg(new Rect(0, 0, h, w), 60, baos); // Where 100 is the quality of the generated jpeg
                    byte[] jpegArray = baos.toByteArray();

                    new SavePhotoTask().execute(jpegArray);
                    Log.d("ImageFetchingDetector", "saving pic");


                    String faceId = PostHelper.detectFace(jpegArray);
                    Log.d("detectFace", faceId);
                    if (faceId != "") {
                        String personId = PostHelper.identifyFace(faceId);
                        if (personId != "") {
                            Log.d("PERSON", personId);
                            User c = AddressBook.getByFace(personId);
                            if (c==null){
                                Log.e("FACE", "no corresponding user found for face");
                                EventBus.getDefault().post(new OnErrorDuringDetectionPostProcessing("Face not recognized"));
                            } else {
                                EventBus.getDefault().post(new OnPaymentInit(
                                        new MoneyTransfer(c, null, new AmountOfMoney(5.0)),
                                        ProcessingState.FACE_LOCK
                                ));
                            }
                        } else {
                            EventBus.getDefault().post(new OnErrorDuringDetectionPostProcessing("Face not recognized"));
                        }
                    } else {
                        EventBus.getDefault().post(new OnErrorDuringDetectionPostProcessing("Face not recognized"));
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.toString(), e);
                } finally {
                    MainActivity.getCurrentActivity().onStopProcessing(ProcessingState.FACE_LOCK);
                }
            }
        }).start();
    }
}
