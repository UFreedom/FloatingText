package com.ufreedom;

import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Author UFreedom
 * 
 */
public interface   FloatingPathEffect {
    
    abstract Path getFloatingPath(FloatingTextView floatingTextView);
    
    abstract PathMeasure getFloatPathMeasure(Path path,FloatingTextView floatingTextView); 
    
}
