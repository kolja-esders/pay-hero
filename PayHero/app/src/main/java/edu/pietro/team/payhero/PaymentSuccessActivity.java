package edu.pietro.team.payhero;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String amount = i.getStringExtra("amount");

        ((TextView)findViewById(R.id.msg_success)).setText("Transferred " + amount + " to " + name);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(PaymentSuccessActivity.this, ScanActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
