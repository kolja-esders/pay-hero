package edu.pietro.team.payhero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.pietro.team.payhero.helper.ImageOcrResolver;
import edu.pietro.team.payhero.services.OcrResolveService;

public class MainActivity extends AppCompatActivity {

    private Intent mOcrServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ImageOcrResolver res = new ImageOcrResolver();
        //res.resolveImage("", null);
        mOcrServiceIntent = new Intent(this, OcrResolveService.class);
        this.startService(mOcrServiceIntent);
    }
}
