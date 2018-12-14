package com.gungoren.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class MagicImageView extends AppCompatImageView {

    private Bitmap nextBitmap = null;
    private Bitmap animatedBitmap = null;
    private boolean isAnimating = false;
    private Rect src = new Rect();
    private Rect dest = new Rect();
    private Rect mov = new Rect();
    private Rect viewRect = new Rect();
    private int[] loc = new int[2];
    private ValueAnimator valueAnimator;
    private MagicAnimationCompleteListener magicAnimationCompleteListener;

    public MagicImageView(Context context) {
        super(context);
    }

    public MagicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed){
            getLocationOnScreen(loc);
            src.set(loc[0], loc[1], loc[0] + getWidth(), loc[1] + getHeight());
            viewRect.set(left, top, right, bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (nextBitmap == null) {
            nextBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas originalCanvas = new Canvas(nextBitmap);
            super.onDraw(originalCanvas);
        }
        if (isAnimating) {
            canvas.drawBitmap(nextBitmap, null, viewRect, null);
            canvas.drawBitmap(animatedBitmap, null, mov, null);
        } else {
            canvas.drawBitmap(nextBitmap, null, viewRect, null);
        }
    }

    public void animate(final View dst, final Bitmap bitmap){
        if (valueAnimator != null && valueAnimator.isRunning())
            valueAnimator.cancel();
        int[] pos = new int[2];
        dst.getLocationOnScreen(pos);
        dest.set(pos[0], pos[1], pos[0] + dst.getWidth(), pos[1] + dst.getHeight());
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (float)valueAnimator.getAnimatedValue();
                int left = (int)((dest.left - src.left) * val);
                int top = (int)((dest.top - src.top) * val);

                int r = (dest.left - src.left + dst.getWidth());
                int b = (dest.top - src.top + dst.getHeight());
                int right = viewRect.right - (int)((viewRect.right - r) * val);
                int bottom = viewRect.bottom - (int)((viewRect.bottom - b) * val);

                mov.set(left, top, right, bottom);
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter(){

            private boolean isCancelled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimating = false;
                isCancelled = true;
                nextBitmap = animatedBitmap;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                if (magicAnimationCompleteListener != null && !isCancelled) {
                    magicAnimationCompleteListener.onComplete(animatedBitmap);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                animatedBitmap = nextBitmap;
                nextBitmap = bitmap;
                isAnimating = true;
            }

            @Override
            public void onAnimationPause(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationResume(Animator animation) {
                isAnimating = true;
            }
        });
        valueAnimator.setDuration(2500);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    public void setMagicAnimationCompleteListener(MagicAnimationCompleteListener magicAnimationCompleteListener) {
        this.magicAnimationCompleteListener = magicAnimationCompleteListener;
    }
}
