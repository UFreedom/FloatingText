package com.ufreedom.uikit.effect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.ufreedom.uikit.BaseFloatingPathAnimator;
import com.ufreedom.uikit.FloatingTextView;

/**
 * Author UFreedom
 * 
 */
public class CurvePathFloatingAnimator extends BaseFloatingPathAnimator {

    
    @Override
    public void applyFloatingPathAnimation(final FloatingTextView view, float start, float end) {
        ValueAnimator translateAnimator;
        ValueAnimator alphaAnimator;
        
        Spring scaleAnim = createSpringByBouncinessAndSpeed(11, 15)
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float transition = transition((float) spring.getCurrentValue(), 0.0f, 1.0f);
                        view.setScaleX(transition);
                        view.setScaleY(transition);
                    }
                });


        translateAnimator = ObjectAnimator.ofFloat(start, end);
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                float pos[] = getFloatingPosition(value);
                float x = pos[0];
                float y = pos[1];
                view.setTranslationX(x);
                view.setTranslationY(y);

            }
        });
        translateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setTranslationX(0);
                view.setTranslationY(0);
                view.setAlpha(0f);
            }
        });


        alphaAnimator = ObjectAnimator.ofFloat(1.0f, 0f);
        alphaAnimator.setDuration(3000);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });


        scaleAnim.setEndValue(1f);
        translateAnimator.setDuration(3000);
        translateAnimator.setStartDelay(50);
        translateAnimator.start();
        alphaAnimator.start();

    }
}
