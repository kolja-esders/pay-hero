package edu.pietro.team.payhero;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.pietro.team.payhero.helper.AddressBook;
import edu.pietro.team.payhero.helper.PostHelper;

public class ValidationActivity extends AppCompatActivity {

    private String mName;
    private String mIban;
    private String mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_validation);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //((Button) findViewById(R.id.button)).setVisibility(View.VISIBLE);
        //((ProgressBar) findViewById(R.id.progressTransaction)).setVisibility(View.INVISIBLE);

        //((RelativeLayout) findViewById(R.id.content_validation)).requestFocus();

        //setSupportActionBar(toolbar);
        //Button okay = (Button) findViewById(R.id.button);
        /*okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hide button and show loading
                //((Button) findViewById(R.id.button)).setVisibility(View.INVISIBLE);
                //((ProgressBar) findViewById(R.id.progressTransaction)).setVisibility(View.VISIBLE);
                //((RelativeLayout) findViewById(R.id.content_validation)).requestFocus();


                //mName = ((EditText)findViewById(R.id.nameEdit)).getText().toString();
                mIban = ((TextView)findViewById(R.id.ibanEdit)).getText().toString();
                mAmount = ((TextView)findViewById(R.id.amountEdit)).getText().toString();
                new AsyncTask<String[],Void,Boolean>() {
                        @Override
                        protected Boolean doInBackground(String[]... strings) {
                            Log.d("HELP", strings.toString());
                            try {
                                PostHelper.transfer(strings[0][0].replace(" ", ""), strings[0][1], strings[0][2].replace(",",".").replace("€", "").replace(" ",""));
                                return true;
                            } catch (Exception e) {
                                Log.e("PAYMENT", "Payment failed :/", e);
                            }
                            return false;
                        }

                        @Override
                        protected void onPostExecute(Boolean transerSuccessful) {
                            super.onPostExecute(transerSuccessful);
                            if (transerSuccessful) {
                                Intent intent = new Intent(ValidationActivity.this, PaymentSuccessActivity.class);
                                intent.putExtra("name", ValidationActivity.this.mName);
                                intent.putExtra("amount", ValidationActivity.this.mAmount);
                                startActivity(intent);
                            } else {
                                ((Button) findViewById(R.id.button)).setVisibility(View.VISIBLE);
                                //((ProgressBar) findViewById(R.id.progressTransaction)).setVisibility(View.INVISIBLE);
                            }
                        }
                    }.execute(new String[][]{{mIban,mName,mAmount}});
            }
        });*/
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
            //((EditText)findViewById(R.id.nameEdit)).setText(intent.getStringExtra("name"));
            ((EditText)findViewById(R.id.ibanEdit)).setText(intent.getStringExtra("iban"));
        } else if (hasName) {
            ((TextView)findViewById(R.id.nameEdit)).setText(intent.getStringExtra("name"));
        } else /* hasIban == true !! */ {
            ((TextView)findViewById(R.id.ibanEdit)).setText(intent.getStringExtra("iban"));
            //findViewById(R.id.ibanEdit).setVisibility(View.VISIBLE);
        }

        String amountStr = String.format("%1$.2f", intent.getDoubleExtra("amount", 0.0)).replace('.',',') + " €";
        ((TextView)findViewById(R.id.amountEdit)).setText(amountStr);

        int img = AddressBook.getImgForName(intent.getStringExtra("name"));
        if (img >= 0) {
            //((CircleImageView)findViewById(R.id.profileImage)).setImageResource(img);
        }
    }
}
