package com.ufreedom.uikit;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.ufreedom.uikit.effect.TranslateFloatingAnimator;

/**
 * Author UFreedom
 */
public class FloatingText {

    private FloatingTextBuilder mFloatingTextBuilder;
    private FloatingTextView mFloatingTextView;
    private FrameLayout floatingTextWrapper;


    protected FloatingText(FloatingTextBuilder floatingTextBuilder) {
        this.mFloatingTextBuilder = floatingTextBuilder;
    }


    public void startFloating(View view) {
        mFloatingTextView.flyText(view);
    }


    public FloatingTextView attach2Window() {

        ViewGroup rootView = (ViewGroup) mFloatingTextBuilder.getActivity().findViewById(Window.ID_ANDROID_CONTENT);

        // get wrapper from activity
        floatingTextWrapper = (FrameLayout) mFloatingTextBuilder.getActivity().findViewById(R.id.FloatingText_wrapper);

        // if activity does not yet have a floatingText wrapper, we add one
        if(floatingTextWrapper == null){
            floatingTextWrapper = new FrameLayout(mFloatingTextBuilder.getActivity());
            floatingTextWrapper.setId(R.id.FloatingText_wrapper);
            rootView.addView(floatingTextWrapper);
        }


        mFloatingTextView = new FloatingTextView(mFloatingTextBuilder.getActivity());

        // add view to floatingTextWrapper
        floatingTextWrapper.bringToFront();
        floatingTextWrapper.addView(mFloatingTextView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mFloatingTextView.setFloatingTextBuilder(mFloatingTextBuilder);

        return mFloatingTextView;
    }

    public void dettachFromWidow() {
        if (mFloatingTextView == null || mFloatingTextBuilder == null) return;
        ViewGroup rootView = (ViewGroup) mFloatingTextBuilder.getActivity().findViewById(Window.ID_ANDROID_CONTENT);
        rootView.removeView(mFloatingTextView);
    }


    public static class FloatingTextBuilder {

        private Activity activity;
        private int textColor;
        private int textSize;
        private FloatingAnimator floatingAnimator;
        private FloatingPathEffect floatingPathEffect;
        private String textContent = "";
        private int offsetX;
        private int offsetY;

        public FloatingTextBuilder(Activity activity) {
            this.activity = activity;
        }

        public int getOffsetX() {
            return offsetX;
        }

        public FloatingTextBuilder offsetX(int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public int getOffsetY() {
            return offsetY;
        }

        public FloatingTextBuilder offsetY(int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public Activity getActivity() {
            return activity;
        }

        public int getTextColor() {
            return textColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public FloatingAnimator getFloatingAnimator() {
            return floatingAnimator;
        }

        public FloatingPathEffect getFloatingPathEffect() {
            return floatingPathEffect;
        }

        public String getTextContent() {
            return textContent;
        }

        public FloatingTextBuilder textColor(int mTextColor) {
            this.textColor = mTextColor;
            return this;
        }

        public FloatingTextBuilder textSize(int mTextSize) {
            this.textSize = mTextSize;
            return this;
        }

        public FloatingTextBuilder floatingAnimatorEffect(FloatingAnimator floatingAnimator) {
            this.floatingAnimator = floatingAnimator;
            return this;
        }

        public FloatingTextBuilder textContent(String textContent) {
            this.textContent = textContent;
            return this;
        }

        public FloatingTextBuilder floatingPathEffect(FloatingPathEffect floatingPathEffect) {
            this.floatingPathEffect = floatingPathEffect;
            return this;
        }

        public FloatingText build() {

            if (activity == null) {
                throw new NullPointerException("activity should not be null");
            }

            if (textContent == null) {
                throw new NullPointerException("textContent should not be null");
            }

            if (floatingAnimator == null) {
                floatingAnimator = new TranslateFloatingAnimator();
            }

            FloatingText floatingText = new FloatingText(this);
            return floatingText;
        }

    }


}
