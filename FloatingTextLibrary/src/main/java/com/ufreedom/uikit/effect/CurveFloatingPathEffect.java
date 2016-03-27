package com.ufreedom.uikit.effect;

import android.graphics.Path;

import com.ufreedom.uikit.FloatingPath;
import com.ufreedom.uikit.FloatingPathEffect;
import com.ufreedom.uikit.FloatingTextView;

/**
 * Author UFreedom
 */
public class CurveFloatingPathEffect implements FloatingPathEffect {


    @Override
    public FloatingPath getFloatingPath(FloatingTextView floatingTextView) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.quadTo(-100, -200, 0, -300);
        path.quadTo(200, -400, 0, -500);
        return FloatingPath.create(path, false);
    }

}
