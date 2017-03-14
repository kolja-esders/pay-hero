package edu.pietro.team.payhero.helper;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by maxim on 14.03.17.
 */

public class SavePhotoTask extends AsyncTask<byte[], String, String> {
    @Override
    protected String doInBackground(byte[]... jpeg) {
        File photo=new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "photo.jpg");

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(photo.getPath());

            fos.write(jpeg[0]);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

        return(null);
    }
}