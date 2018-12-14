package com.gungoren.magicanimation;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.gungoren.view.MagicAnimationCompleteListener;
import com.gungoren.view.MagicImageView;

/**
 * Created by mehmetgungoren on 13.12.2018.
 */

public class MagicViewTouchListener implements View.OnTouchListener, MagicAnimationCompleteListener {

    private MagicImageView magicImageView;
    private ImageView imageView;
    private MagicAnimationCompleteListener animationCompleteListener;

    public MagicViewTouchListener(MagicImageView magicImageView, ImageView imageView, MagicAnimationCompleteListener magicAnimationCompleteListener) {
        this.magicImageView = magicImageView;
        this.imageView = imageView;
        this.animationCompleteListener = magicAnimationCompleteListener;
        this.magicImageView.setMagicAnimationCompleteListener(this);
    }

    private int CLICK_ACTION_THRESHOLD = 200;
    private float dX, dY;
    private boolean isAnimating = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isAClick(event)) {
                    isAnimating = true;
                    magicImageView.animate(imageView, ((BitmapDrawable) imageView.getDrawable()).getBitmap());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isAnimating)
                    break;
                v.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;
        }
        return true;
    }

    private boolean isAClick(MotionEvent event) {
        long eventTime = event.getEventTime();
        return (eventTime - event.getDownTime() < CLICK_ACTION_THRESHOLD);
    }

    @Override
    public void onComplete(Bitmap bitmap) {
        isAnimating = false;
        if (animationCompleteListener != null) {
            this.animationCompleteListener.onComplete(bitmap);
        }
    }
}
