package tyszka.io.smartpass.tyszka.io.smartpass.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tyszka.io.smartpass.R;

import static android.R.drawable.dialog_holo_light_frame;

/**
 * Created by Ben on 9/4/2017.
 */

public class ColorAdapter extends BaseAdapter {
    private Context mContext;

    public ColorAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mColors.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    private static int alpha = 120;
    public static Integer[] mColors = {
            new Color().argb(alpha, 255, 0, 0), new Color().argb(alpha, 0, 0, 255), new Color().argb(alpha, 204, 255, 51), new Color().argb(alpha, 0, 255, 0), new Color().argb(alpha, 255, 0, 255), new Color().argb(alpha, 0, 0, 0)
    };

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(mContext);
        //textView.setText(String.valueOf(position));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER);
        }
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setHeight(200);


        //TODO: WARNING DEPRECATED

        Drawable drawable = mContext.getResources().getDrawable(android.R.drawable.dialog_holo_light_frame);
        drawable.setColorFilter(new PorterDuffColorFilter(mColors[position], PorterDuff.Mode.MULTIPLY));
        textView.setBackgroundDrawable(drawable);


        return textView;
    }
}

