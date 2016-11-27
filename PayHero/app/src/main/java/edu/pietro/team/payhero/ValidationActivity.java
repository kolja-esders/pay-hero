package edu.pietro.team.payhero;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import edu.pietro.team.payhero.helper.PostHelper;

public class ValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button okay = (Button) findViewById(R.id.button);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText)findViewById(R.id.nameEdit)).getText().toString();
                String iban = ((TextView)findViewById(R.id.ibanEdit)).getText().toString();
                String amount = ((TextView)findViewById(R.id.amountEdit)).getText().toString();
                try {
                    PostHelper.transfer(iban, name, amount.replace(",",".").replace("€", "").replace(" ",""));
                } catch (Exception e) {
                    Log.e("PAYMENT", "Payment failed :/", e);
                }

            }
        });
        final EditText ibanEdit = (EditText) findViewById(R.id.ibanEdit);

        ibanEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String iban = s.toString().replace(" ", "");
                if (StringUtils.countMatches(s.toString(), " ") == ((iban.length() - 1) / 4)) {
                    return;
                }
                List<String> tokens = new ArrayList<>();
                int i = 0;
                while (i < iban.length()) {
                    tokens.add(iban.substring(i, Math.min(i + 4, iban.length())));
                    i += 4;
                }
                ibanEdit.setText(StringUtils.join(tokens, " "));
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent  = getIntent();
        boolean hasIban = intent.hasExtra("iban");
        boolean hasName = intent.hasExtra("name");
        if (hasIban && hasName) {
            ((EditText)findViewById(R.id.nameEdit)).setText(intent.getStringExtra("name"));
            ((EditText)findViewById(R.id.ibanEdit)).setText(intent.getStringExtra("iban"));
        } else if (hasName) {
            ((TextView)findViewById(R.id.nameEdit)).setText(intent.getStringExtra("name"));
        } else /* hasIban == true !! */ {
            ((TextView)findViewById(R.id.ibanEdit)).setText(intent.getStringExtra("iban"));
            findViewById(R.id.ibanEdit).setVisibility(View.VISIBLE);
        }

        String amountStr = String.format("%1$.2f", intent.getDoubleExtra("amount", 0.0)).replace('.',',') + " €";
        ((TextView)findViewById(R.id.amountEdit)).setText(amountStr);
    }
}
