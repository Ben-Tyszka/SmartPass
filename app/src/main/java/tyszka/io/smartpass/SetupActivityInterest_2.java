package tyszka.io.smartpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetupActivityInterest_2 extends AppCompatActivity {

    private String mascot, activity;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_interest_2);

        final EditText edMN = (EditText) findViewById(R.id.editTextMasName);
        final EditText edAT = (EditText) findViewById(R.id.editTextActName);

        next = (Button) findViewById(R.id.buttonNext_3);

        edMN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().matches("^[a-zA-Z-]*$")){
                    edMN.setError("Please use characters A-z only");
                }
                mascot = charSequence.toString();
                enableButtonCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edAT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().matches("^[a-zA-Z-]*$")){
                    edAT.setError("Please use characters A-z only");
                }
                activity = charSequence.toString();
                enableButtonCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final Intent i = new Intent(this, MainActivity.class);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("activity", activity);
                editor.putString("mascot", mascot);
                editor.commit();

                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }
    private void enableButtonCheck(){
        if(activity == null){
            activity = "";
        }
        if(mascot == null){
            mascot = "";
        }
        if(mascot.matches("^[a-zA-Z-]*$") && activity.matches("^[a-zA-Z-]*$") && !mascot.equals("") && !activity.equals("")){
            next.setEnabled(true);
        }else{
            next.setEnabled(false);
        }
    }
}
