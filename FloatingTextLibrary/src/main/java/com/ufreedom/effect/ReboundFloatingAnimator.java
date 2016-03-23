package com.ufreedom.effect;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.ufreedom.FloatingAnimator;

/**
 * Author SunMeng
 * Date : 2016 三月 16
 */
public abstract class ReboundFloatingAnimator implements FloatingAnimator {

    protected SpringSystem springSystem;


    public ReboundFloatingAnimator() {
        springSystem = SpringSystem.create();
    }

    public Spring createSpringByBouncinessAndSpeed(double bounciness, double speed) {
        return springSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(bounciness, speed));
    }

    public Spring createSpringByTensionAndFriction(double bounciness, double speed) {
        return springSystem.createSpring()
                .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(bounciness, speed));
    }

    protected float transition(float progress, float startValue, float endValue) {
        return (float) SpringUtil.mapValueFromRangeToRange(progress, 0, 1, startValue, endValue);
    }
    
}
