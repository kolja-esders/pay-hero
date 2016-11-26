package edu.pietro.team.payhero.services;

import android.app.IntentService;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;

import edu.pietro.team.payhero.helper.PostHelper;

/**
 * Created by maxim on 26.11.16.
 */

public class OcrResolveService extends IntentService {

    private Gson gson = new Gson();

    public OcrResolveService() {
        super("OcrResolveService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            OCR ocr = null;
            if (intent.hasExtra("url")) {
                String json = PostHelper.sendOcrText("application/json", intent.getStringExtra("url"));
                ocr = this.gson.fromJson(json, OCR.class);
            } else if (intent.hasExtra("data")) {
                String json = PostHelper.sendOcrText("application/octet-stream", intent.getStringExtra("data"));
                ocr = this.gson.fromJson(json, OCR.class);
            }

            String str = "";
            if (ocr != null) {
                for (Region r : ocr.regions) {
                    for (Line line : r.lines) {
                        for (Word w : line.words) {
                            str += w.text + " ";
                        }
                    }
                }
                Log.d("RESOLVE", str);
                Intent i = new Intent("IMAGE_OCR_RESOLVED");
                i.putExtra("text", str);
                LocalBroadcastManager.getInstance(this).sendBroadcast(i);
            }
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

    }
}
