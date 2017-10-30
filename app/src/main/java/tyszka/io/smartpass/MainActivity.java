package tyszka.io.smartpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import tyszka.io.smartpass.tyszka.io.smartpass.api.CreatePassword;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;

    private int clickNum = 0;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SmartPass");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE);
        TextView name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nameDrawableDisplay);
        name.setText(sharedPref.getString("firstName", "") + " " + sharedPref.getString("lastName", ""));
        LinearLayout navHeader = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.navHeader);
        navHeader.setBackgroundColor(sharedPref.getInt("color", 0x000000));


        final Button generate = (Button) findViewById(R.id.btnGenerate);
        final Button btnClear = (Button) findViewById(R.id.btnClearPassword);
        final Button btnCopy = (Button) findViewById(R.id.btnCopyPassword);
        final TextView tvPassword = (TextView) findViewById(R.id.passwordDisplay);
        final TextView tvDisplayText = (TextView) findViewById(R.id.cptv);
        final TextView tvEasy = (TextView) findViewById(R.id.tvEasyToRead);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("T", String.valueOf(clickNum));

                clickNum++;

                Handler handler = new Handler();
                generate.setEnabled(false);
                tvPassword.setText("Loading...");
                tvPassword.setVisibility(View.VISIBLE);
                tvDisplayText.setVisibility(View.GONE);
                btnClear.setVisibility(View.GONE);
                btnCopy.setVisibility(view.GONE);
                tvEasy.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        generate.setEnabled(true);
                        tvPassword.setText("");
                        tvPassword.setVisibility(View.GONE);

                        if (!ShouldStopGeneration()) {
                            CreatePassword p = new CreatePassword(getApplicationContext());
                            String password = p.toString();
                            //Log.i("T", password);
                            //Log.i("T", p.getEasyToRead());
                            tvPassword.setText(password);
                            tvEasy.setText(p.getEasyToRead());
                            tvDisplayText.setVisibility(View.VISIBLE);
                            tvPassword.setVisibility(View.VISIBLE);
                            tvEasy.setVisibility(View.VISIBLE);
                            btnClear.setVisibility(View.VISIBLE);
                            btnCopy.setVisibility(View.VISIBLE);
                            SharedPreferences.Editor ed = sharedPref.edit();
                            if(clickNum == 1){
                                //Snackbar.make(getCurrentFocus(), "Touch password to copy", Snackbar.LENGTH_SHORT).show();
                            }else if(clickNum % 5 == 0 && clickNum != 0 && clickNum != 1){
                                showVideoAd();
                            }else if (clickNum >= 26) {
                                ed.putInt("shouldFreeze", 1);
                                ed.commit();
                            }
                            ed.putLong("lastTime", System.currentTimeMillis());
                            ed.commit();

                        } else {
                            TextView tvDisplayText = (TextView) findViewById(R.id.cptv);

                            tvPassword.setText("You are limited to 26 passwords/hour");
                            tvDisplayText.setVisibility(View.GONE);
                            tvPassword.setVisibility(View.VISIBLE);
                            btnClear.setVisibility(View.VISIBLE);
                            btnCopy.setVisibility(View.GONE);
                            generate.setEnabled(false);
                            tvEasy.setVisibility(View.GONE);
                        }
                    }
                }, 2000);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvDisplayText = (TextView) findViewById(R.id.cptv);
                TextView tvPassword = (TextView) findViewById(R.id.passwordDisplay);
                tvDisplayText.setVisibility(View.GONE);
                tvPassword.setVisibility(View.GONE);
                btnClear.setVisibility(View.GONE);
                btnCopy.setVisibility(View.GONE);
                tvEasy.setVisibility(View.GONE);
                Snackbar.make(getCurrentFocus(), "Cleared", Snackbar.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "");
                clipboard.setPrimaryClip(clip);
            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getCurrentFocus(), "Password copied to clipboard", Snackbar.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password", tvPassword.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });
        /*String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();*/
        mAdView = (AdView) findViewById(R.id.bannerOne);
        /*Log.i("T", deviceId);*/
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("5E33D4F942DB9FBC196F846D82524005").build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5835693865573663/6033022869");
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
                mInterstitialAd.loadAd(adRequest);
            }
        });

    }
    private void showVideoAd(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public boolean ShouldStopGeneration() {

        long prev_time = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE).getLong("lastTime", -1);
        if(prev_time != -1){
            //Log.e("T", String.valueOf(System.currentTimeMillis() - prev_time));
            String result = String.valueOf((System.currentTimeMillis() - prev_time) / 1000);
            Log.e("T", result + "s");
        }
        int status = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE).getInt("shouldFreeze", 2);
        if(status == 1){
            //Check if should allow
            if(((System.currentTimeMillis() - prev_time) / 1000) < 3600){
                return true;
            }else if(((System.currentTimeMillis() - prev_time) / 1000) >= 3600){
                SharedPreferences.Editor ed =getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE).edit();
                ed.putInt("shouldFreeze", 2);
                ed.commit();
                clickNum = 0;
                return false;
            }else{
                return false;
            }
            //return true;
        }else if(status == 2 || status == -1){
            //Allow blindly
            return false;
        }else{
            return false;
        }
        /*if (clickNum >= 12) {
            if(((System.currentTimeMillis() - prev_time) / 1000) < 3600){
                return true;
            }else if(((System.currentTimeMillis() - prev_time) / 1000) >= 3600){
                return false;
            }else{
                return false;
            }
        } else {
            return false;
        }
*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        item.setChecked(false);
        int id = item.getItemId();
        if (id == R.id.nav_FAQ) {
            showPopup(R.layout.popup_faq);
        } else if (id == R.id.nav_resetData) {
            final Intent i = new Intent(this, SetupActivity.class);
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Confirm delete?", 5000).setAction("Clear", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("lastName");
                    editor.remove("firstName");
                    editor.remove("mascot");
                    editor.remove("color");
                    editor.remove("activity");
                    editor.commit();

                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });
            snackbar.show();
        } else if (id == R.id.nav_privacy) {
            showPopup(R.layout.popup_privacypolicy);
        } else if (id == R.id.nav_about) {
            showPopup(R.layout.popup_about);
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "I just used the app SmartPass! It creates easy to remember and secure passwords! Download it here: http://bit.ly/2hb8S4w";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_feedback) {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"upload.typhoontechnologies@gmail.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SmartPass Feedback");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showPopup(int id) {
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.content_main);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(id, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        if (id == R.layout.popup_faq) {
            TextView feedback = (TextView) popupView.findViewById(R.id.textView17);
            feedback.setText(Html.fromHtml("Email us <a href=\"mailto:upload.typhoontechnologies@gmail.com\">here</a>"));
            feedback.setMovementMethod(LinkMovementMethod.getInstance());
        }else if(id == R.layout.popup_privacypolicy){
            TextView privacy = (TextView) popupView.findViewById(R.id.textView7);
            privacy.setText(Html.fromHtml("SmartPass stores your information locally on your device. You data is never transmitted to our servers. We may use third party services like Google Admob that collect/use your personal information.\nIf you have any concerns, please read our full privacy policy <a href=\"https://sites.google.com/view/smartpass/home\">here</a>"));
            privacy.setMovementMethod(LinkMovementMethod.getInstance());
        }
        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
    }
}
