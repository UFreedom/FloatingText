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

        final View normalView = findViewById(R.id.normalView);
        final FloatingText   normalFloatingText = new FloatingText.FloatingTextBuilder(MainActivity.this)
                .textColor(Color.RED)
                .textSize(100)
                .textContent("+1000")
                .build();
        normalFloatingText.attach2Window();
        
        assert normalView != null;
        normalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalFloatingText.startFloating(view);
            }
        });

        final FloatingText  cubicFloatingText = new FloatingText.FloatingTextBuilder(MainActivity.this)
                .textColor(Color.RED)
                .textSize(100)
                .floatingAnimatorEffect(new CurvePathFloatingAnimator())
                .floatingPathEffect(new CurveFloatingPathEffect())
                .textContent("Hello! ").build();
        cubicFloatingText.attach2Window();


        View curveView = findViewById(R.id.curveView);

        assert curveView != null;
        curveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cubicFloatingText.startFloating(view);
            }
        });
        
        View scaleView = findViewById(R.id.scaleView);
        
        final FloatingText  scaleFloatingText = new FloatingText.FloatingTextBuilder(MainActivity.this)
                .textColor(Color.RED)
                .textSize(100)
                .offsetY(-100)
                .floatingAnimatorEffect(new ScaleFloatingAnimator())
                .textContent("+188").build();
        scaleFloatingText.attach2Window();

        assert scaleView != null;
        scaleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleFloatingText.startFloating(view);
            }
        });
        
    }


}
