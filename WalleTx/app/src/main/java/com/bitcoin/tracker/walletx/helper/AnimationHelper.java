package com.bitcoin.tracker.walletx.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bitcoin.tracker.walletx.R;

/**
 * Created by brianhowell on 4/11/15.
 */
public class AnimationHelper {


    public AnimationHelper() {
        super();
        // default constructor
    }

    public static void startRotateSync(MenuItem item, Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.actionbar_sync, null);

        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);

        item.setActionView(iv);
    }

    public static void stopRotateSync(MenuItem item, Context context) {
        item.getActionView().clearAnimation();
    }


}
