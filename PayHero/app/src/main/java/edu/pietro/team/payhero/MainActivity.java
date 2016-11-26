package edu.pietro.team.payhero;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import edu.pietro.team.payhero.services.OcrResolveService;

public class MainActivity extends AppCompatActivity {

    private Intent mOcrServiceIntent;
    private IntentFilter mOcrIntentFilter;
    private BroadcastReceiver mOcrResolveReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        startActivity(intent);

        mOcrIntentFilter = new IntentFilter("IMAGE_OCR_RESOLVED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*mOcrServiceIntent = new Intent(this, OcrResolveService.class);
        mOcrServiceIntent.putExtra("url", "https://images.gutefrage.net/media/fragen/bilder/ueberweisung--iban-sparkasse-wie-geht-das/0_original.jpg?v=1330352238000");
        this.startService(mOcrServiceIntent);*/
        Intent i = new Intent(this, ValidationActivity.class);
        i.putExtra("name", "Max");
        i.putExtra("iban", "DE23 5767 4875 432784");
        i.putExtra("amount", 23.46);
        i.putExtra("reason", "VWZ");
        startActivity(i);

        mOcrResolveReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("MAINACT", intent.getStringExtra("text"));
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mOcrResolveReceiver, mOcrIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOcrResolveReceiver);
        mOcrResolveReceiver = null;
    }
}
