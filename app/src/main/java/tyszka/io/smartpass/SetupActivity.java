package tyszka.io.smartpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {


    private String first, last;
    Button next;
    CheckBox box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);



        final TextView intro = (TextView) findViewById(R.id.textView2);
        final TextView promt_1 = (TextView) findViewById(R.id.textView3);

        final EditText firstName = (EditText) findViewById(R.id.editText_firstName);
        final EditText lastName = (EditText) findViewById(R.id.editText_lastName);

        next = (Button) findViewById(R.id.buttonNext_1);

        box = (CheckBox) findViewById(R.id.checkBox_TosPS);


        box.setText(Html.fromHtml("I have read and agree to our <a href=\"https://sites.google.com/view/smartpass/home\">Privacy Policy</a>"));
        box.setLinksClickable(true);
        box.setClickable(true);
        box.setMovementMethod(LinkMovementMethod.getInstance());
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(2000);

        final Animation in_1 = new AlphaAnimation(0.0f, 1.0f);
        in_1.setDuration(2000);

        final Animation in_2 = new AlphaAnimation(0.0f, 1.0f);
        in_2.setDuration(1200);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(2000);

        final Intent i = new Intent(this, SetupActivityInterest_1.class);

        promt_1.setAlpha(0.0f);
        intro.setAnimation(in);

        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                promt_1.setAlpha(1.0f);
                promt_1.setAnimation(in_1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        in_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                promt_1.setAnimation(out);
                intro.setAnimation(out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                promt_1.setVisibility(View.GONE);
                intro.setVisibility(View.GONE);

                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                box.setVisibility(View.VISIBLE);

                firstName.setAnimation(in_2);
                lastName.setAnimation(in_2);
                next.setAnimation(in_2);
                box.setAnimation(in_2);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().matches("^[a-zA-Z-]*$")){
                    firstName.setError("Please use characters A-z only");
                }
                first = charSequence.toString();
                enableButtonCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().matches("^[a-zA-Z-]*$")){
                    lastName.setError("Please use characters A-z only");
                }
                last = charSequence.toString();
                enableButtonCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableButtonCheck();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i.putExtra("first", first);
                i.putExtra("last", last);


                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("firstName", first);
                editor.putString("lastName", last);
                editor.commit();


                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });


        enableButtonCheck();
    }

    private void enableButtonCheck(){
        if(first == null){
            first = "";
        }
        if(last == null){
            last = "";
        }
        if(first.matches("^[a-zA-Z-]*$") && last.matches("^[a-zA-Z-]*$") && box.isChecked() && !first.equals("") && !last.equals("")){
            next.setEnabled(true);
        }else{
            next.setEnabled(false);
        }
    }
}
