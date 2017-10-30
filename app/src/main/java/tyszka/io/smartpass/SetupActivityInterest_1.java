package tyszka.io.smartpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import tyszka.io.smartpass.tyszka.io.smartpass.adapters.ColorAdapter;

import static tyszka.io.smartpass.tyszka.io.smartpass.adapters.ColorAdapter.mColors;

public class SetupActivityInterest_1 extends AppCompatActivity {

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_interest_1);

        TextView welcome = (TextView) findViewById(R.id.tvSetupWelcome1);

        welcome.setText("Welcome " + getIntent().getExtras().getString("first"));

        final GridView gridview = (GridView) findViewById(R.id.gv1);
        gridview.setAdapter(new ColorAdapter(this));
        final Button next = (Button) findViewById(R.id.buttonNext_2);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                /*Toast.makeText(SetupActivityInterest_1.this, "" + position,
                        Toast.LENGTH_SHORT).show();*/
                //TODO: WARNING DEPRECATED

                for(int i = 0; i < mColors.length; i++){
                    if(i != position){
                        Drawable drawable_d = getResources().getDrawable(android.R.drawable.dialog_holo_light_frame);
                        drawable_d.setColorFilter(new PorterDuffColorFilter(mColors[i], PorterDuff.Mode.MULTIPLY));
                        gridview.getChildAt(i).setBackgroundDrawable(drawable_d);
                    }else{
                        gridview.getChildAt(i).setBackgroundDrawable(null);
                        gridview.getChildAt(i).setBackgroundColor(mColors[i]);
                    }
                }
                color = mColors[position];
                next.setEnabled(true);
            }
        });


        final Intent i = new Intent(this, SetupActivityInterest_2.class);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("tyszka.io.smartpass", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("color", color);
                editor.commit();

                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }
}
