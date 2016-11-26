package edu.pietro.team.payhero;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent  = getIntent();
        boolean hasIban = intent.hasExtra("iban");
        boolean hasName = intent.hasExtra("name");
        if (hasIban && hasName) {
            findViewById(R.id.ibanTextEdit).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.nameTextView)).setText(intent.getStringExtra("name"));
            ((TextView)findViewById(R.id.ibanTextView)).setText("( " + intent.getStringExtra("iban") + " )");
        } else if (hasName) {
            ((TextView)findViewById(R.id.nameTextView)).setText(intent.getStringExtra("name"));
            findViewById(R.id.ibanTextEdit).setVisibility(View.INVISIBLE);
            findViewById(R.id.ibanTextView).setVisibility(View.INVISIBLE);
        } else /* hasIban == true !! */ {
            ((TextView)findViewById(R.id.ibanTextView)).setText("( " + intent.getStringExtra("iban") + " )");
            findViewById(R.id.ibanTextEdit).setVisibility(View.INVISIBLE);
            findViewById(R.id.nameTextView).setVisibility(View.INVISIBLE);
        }

        String amountStr = String.format("%1$.2f", intent.getDoubleExtra("amount", 0.0)).replace('.',',') + " €";
        ((TextView)findViewById(R.id.amountTextView)).setText(amountStr);

        if (intent.hasExtra("reason")) {
            ((TextView)findViewById(R.id.reasonTextEdit)).setText(intent.getStringExtra("reason"));
        }
    }
}
