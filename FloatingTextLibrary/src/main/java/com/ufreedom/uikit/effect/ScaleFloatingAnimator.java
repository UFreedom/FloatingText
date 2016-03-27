package com.ufreedom.uikit.effect;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.ufreedom.uikit.FloatingTextView;

/**
 * Author UFreedom
 * 
 */
public class ScaleFloatingAnimator extends ReboundFloatingAnimator {

    private long duration;

    public ScaleFloatingAnimator() {
        duration = 1000;
    }

    public ScaleFloatingAnimator(long duration) {
        this.duration = duration;
    }


    @Override
    public void applyFloatingAnimation(final FloatingTextView view) {

        Spring scaleAnim = createSpringByBouncinessAndSpeed(10, 15)
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float transition = transition((float) spring.getCurrentValue(), 0.0f, 1.0f);
                        view.setScaleX(transition);
                        view.setScaleY(transition);
                    }
                });
        
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(1.0f, 0.0f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleAnim.setEndValue(1f);
        alphaAnimator.start();
    }


}
