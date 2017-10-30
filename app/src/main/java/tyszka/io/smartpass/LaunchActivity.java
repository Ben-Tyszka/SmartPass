package tyszka.io.smartpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.View.GONE;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE);
        if(sharedPref.contains("lastName") && sharedPref.contains("firstName") && sharedPref.contains("mascot") && sharedPref.contains("color")&& sharedPref.contains("activity")){
            setContentView(R.layout.activity_launch_return);
            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(2500);
            getWindow().getDecorView().findViewById(R.id.activity_launch_return).setAnimation(in);
            final Intent i = new Intent(this, MainActivity.class);
            in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    }, 1000);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{
            setContentView(R.layout.activity_launch);

            final TextView text_header = (TextView) findViewById(R.id.launch_screen_head);
            final TextView text_sub = (TextView) findViewById(R.id.launch_screen_sub);
            Typeface face = Typeface.createFromAsset(getAssets(),"OpenSans-Light.ttf");

            text_header.setTypeface(face);
            text_sub.setTypeface(face);

            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(1200);

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(1000);


            text_sub.startAnimation(in);
            final Intent i = new Intent(this, SetupActivity.class);
            in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    text_header.startAnimation(out);
                    text_sub.startAnimation(out);
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
                    text_header.setVisibility(GONE);
                    text_sub.setVisibility(GONE);

                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
}
