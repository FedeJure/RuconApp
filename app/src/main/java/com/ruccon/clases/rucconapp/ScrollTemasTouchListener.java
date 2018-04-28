package com.ruccon.clases.rucconapp;

import android.app.Application;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

/**
 * Created by Fede on 5/4/2018.
 */

public class ScrollTemasTouchListener implements View.OnTouchListener  {
    public ScrollTemasTouchListener(final Context context) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
