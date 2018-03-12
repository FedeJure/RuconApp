package com.ruccon.clases.rucconapp;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Created by Fede on 4/2/2018.
 */

public class DeslizarTouchListener implements View.OnTouchListener {

    float initialTouchX;
    float initialTouchY;
    boolean puedeCambiar = true;
    ViewFlipper flipper;
    TextView tipoDeFiltro;

    public DeslizarTouchListener(ViewFlipper fliper,TextView textView) {
        this.flipper = fliper;
        this.tipoDeFiltro = textView;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                puedeCambiar = true;
                return true;
            case MotionEvent.ACTION_MOVE:

                if ((event.getRawX() - initialTouchX) >100 && puedeCambiar){
                    flipper.showNext();
                    puedeCambiar = false;
                    tipoDeFiltro.setText((String)flipper.getCurrentView().getTag());
                }
                if ((event.getRawX() - initialTouchX) <-100 && puedeCambiar){
                    flipper.showPrevious();
                    puedeCambiar = false;
                    tipoDeFiltro.setText((String)flipper.getCurrentView().getTag());
                }

                return true;
        }
        return false;
    }

}
