package edu.pietro.team.payhero.helper;

import android.util.Log;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.*;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;

import java.util.concurrent.ExecutionException;

/**
 * Created by maxim on 26.11.16.
 */

public class ImageOcrResolver implements IImageOcrResolver {

    private Gson gson = new Gson();

    public ImageOcrResolver() {

    }

    @Override
    public void resolveImage(String imgBinary, OcrListener l) {
        /*VisionServiceClient client = new VisionServiceRestClient("8c7bb9fea8c14560a4d3c000a5f775e9");
        try {
            OCR ocr = client.recognizeText("http://www.unix.org/images/unix_plate.jpg", "unk", true);
            Log.d("RESOLVE", ocr.regions.iterator().next().lines.iterator().next().words.iterator().next().text);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }*/
        try {
            String json = PostHelper.sendOcrText("https://images.gutefrage.net/media/fragen/bilder/ueberweisung--iban-sparkasse-wie-geht-das/0_original.jpg?v=1330352238000");
            OCR ocr = this.gson.fromJson(json, OCR.class);
            String str = "";
            for (Region r : ocr.regions) {
                for (Line line : r.lines) {
                    for (Word w : line.words) {
                        str += w.text + " ";
                    }
                }
            }

            Log.d("RESOLVE", str);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }
}