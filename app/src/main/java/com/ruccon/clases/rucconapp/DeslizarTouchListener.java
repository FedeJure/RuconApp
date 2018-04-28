package com.ruccon.clases.rucconapp;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
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
    Animation inFromLeft;
    Animation inFromRight;
    Animation outToLeft;
    Animation outToRight;
    ScrollView scroll;
    public DeslizarTouchListener(ViewFlipper fliper, Context context) {
        inicializar(fliper,context);
    }
    public DeslizarTouchListener(ViewFlipper fliper, Context context, ScrollView scroll) {
        inicializar(fliper, context);
        this.scroll = scroll;
    }
    private void inicializar(ViewFlipper fliper, Context context){
        this.flipper = fliper;
        inFromLeft = AnimationUtils.loadAnimation(context, R.anim.in_from_left);
        inFromRight = AnimationUtils.loadAnimation(context, R.anim.in_from_right);
        outToLeft = AnimationUtils.loadAnimation(context, R.anim.out_to_left);
        outToRight = AnimationUtils.loadAnimation(context, R.anim.out_to_right);
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
                float movimientoX = (event.getRawX() - initialTouchX);
                float movimientoY = (event.getRawY() - initialTouchY);
                if (scroll != null && flipper.getCurrentView().getId() == R.id.scroll_view_temas && Math.abs(movimientoY) >30 && Math.abs(movimientoX) < 100) {
                    scroll.scrollBy(0, -((int)movimientoY)/10);
                    return true;
                }
                if ( movimientoX>100 && puedeCambiar && !flipper.isFlipping()){
                    flipper.setInAnimation(inFromLeft);
                    flipper.setOutAnimation(outToRight);
                    flipper.showNext();
                    puedeCambiar = false;
                }
                if (movimientoX <-100 && puedeCambiar && !flipper.isFlipping()){
                    flipper.setInAnimation(inFromRight);
                    flipper.setOutAnimation(outToLeft);
                    flipper.showPrevious();
                    puedeCambiar = false;
                }

                return true;
        }
        return false;
    }

}
