package edu.pietro.team.payhero.services;

import android.app.IntentService;
import android.content.Intent;

import edu.pietro.team.payhero.helper.ImageOcrResolver;

/**
 * Created by maxim on 26.11.16.
 */

public class OcrResolveService extends IntentService {

    public OcrResolveService() {
        super("OcrResolveService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ImageOcrResolver res = new ImageOcrResolver();
        res.resolveImage("", null);
    }
}
