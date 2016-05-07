package com.ufreedom.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ufreedom.uikit.FloatingText;
import com.ufreedom.uikit.effect.CurveFloatingPathEffect;
import com.ufreedom.uikit.effect.CurvePathFloatingAnimator;
import com.ufreedom.uikit.effect.ScaleFloatingAnimator;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        final View layoutTranslateFloating = findViewById(R.id.layoutTranslateView);
        final View translateFloatingView = findViewById(R.id.translateView);
        final FloatingText   translateFloatingText = new FloatingText.FloatingTextBuilder(MainActivity.this)
                .textColor(Color.RED)
                .textSize(100)
                .textContent("+1000")
                .build();
        translateFloatingText.attach2Window();
        
        assert layoutTranslateFloating != null;
        layoutTranslateFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateFloatingText.startFloating(translateFloatingView);
            }
        });

        
        final FloatingText  cubicFloatingText = new FloatingText.FloatingTextBuilder(MainActivity.this)
                .textColor(Color.RED)
                .textSize(100)
                .floatingAnimatorEffect(new CurvePathFloatingAnimator())
                .floatingPathEffect(new CurveFloatingPathEffect())
                .textContent("Hello! ").build();
        cubicFloatingText.attach2Window();

        
        View layoutCurveView = findViewById(R.id.layoutCurveView);
        final View curveView = findViewById(R.id.curveView);
        assert curveView != null;
        assert layoutCurveView != null;
        layoutCurveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cubicFloatingText.startFloating(curveView);
            }
        });

        
        View layoutScaleView = findViewById(R.id.layoutScaleView);
        final View scaleView = findViewById(R.id.scaleView);
        final FloatingText  scaleFloatingText = new FloatingText.FloatingTextBuilder(MainActivity.this)
                .textColor(Color.parseColor("#7ED321"))
                .textSize(100)
                .offsetY(-100)
                .floatingAnimatorEffect(new ScaleFloatingAnimator())
                .textContent("+188").build();
        scaleFloatingText.attach2Window();

        assert scaleView != null;
        assert layoutScaleView != null;
        layoutScaleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleFloatingText.startFloating(scaleView);
            }
        });
        
    }


}
