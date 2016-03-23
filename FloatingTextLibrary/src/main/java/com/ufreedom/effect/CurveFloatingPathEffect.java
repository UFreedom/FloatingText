package com.ufreedom.effect;

import android.graphics.Path;
import android.graphics.PathMeasure;

import com.ufreedom.FloatingPathEffect;
import com.ufreedom.FloatingTextView;

/**
 * Author UFreedom
 *
 */
public class CurveFloatingPathEffect implements FloatingPathEffect {


    @Override
    public Path getFloatingPath(FloatingTextView floatingTextView) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.quadTo(-100, -200, 0, -300);
        path.quadTo(200, -400, 0, -500);
        return path;
    }

    @Override
    public PathMeasure getFloatPathMeasure(Path path, FloatingTextView floatingTextView) {
        return new PathMeasure(path, false);
    }
}
