package com.ufreedom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * UFreedom
 * 
 * 
 */
public class FloatingTextView extends TextView {

    private static final String TAG = "FloatingTextView";
    private static final int PATH_WIDTH = 2;

    private FloatingText.FloatingTextBuilder floatingTextBuilder;
    private Paint mTextPaint;
 
    private Paint mPathPaint;
    private PathMeasure mPathMeasure;
    private View mAttachedView;
    

    public FloatingTextView(Context context) {
        this(context, null);
    }

    public FloatingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public FloatingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(100);
        mTextPaint.setColor(Color.RED);

        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(PATH_WIDTH);
        mPathPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        
        if (floatingTextBuilder == null ){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthPaddingOffset =   getPaddingLeft() + getPaddingRight();
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            float maxWidth = getDesireWidth(mTextPaint);
            Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
            setMeasuredDimension((int) maxWidth + widthPaddingOffset, fmi.bottom - fmi.top);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            float maxWidth = getDesireWidth(mTextPaint);
            setMeasuredDimension((int) maxWidth + widthPaddingOffset, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
            setMeasuredDimension(widthSize + widthPaddingOffset, fmi.bottom - fmi.top);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    private  float getDesireWidth(Paint mTextPaint) {
       return mTextPaint.measureText(floatingTextBuilder.getTextContent());
    }


    public void setFloatingTextBuilder(FloatingText.FloatingTextBuilder floatingTextBuilder) {
        this.floatingTextBuilder = floatingTextBuilder;
        initTextStyle();
    }


    
    public void flyText(View view) {

        layout(0,0,0,0);

        mAttachedView = view;

        Rect rect = new Rect();
        mAttachedView.getGlobalVisibleRect(rect);
        int[] location = new int[2];
        getLocationOnScreen(location);
        rect.offset(-location[0], -location[1]);
        
        
        int left = rect.left + (rect.width() - getMeasuredWidth()) / 2 + floatingTextBuilder.getOffsetX();
        int top = rect.top + (rect.height() - getMeasuredHeight()) / 2 + + floatingTextBuilder.getOffsetY();
        int bottom = top + getMeasuredHeight();
        int right = left + getMeasuredWidth();

        layout(left,top,right,bottom);
        invalidate();
        
        FloatingPathEffect floatingPathEffect = floatingTextBuilder.getFloatingPathEffect();
        if (floatingPathEffect != null){
            FloatingPath floatingPath = floatingPathEffect.getFloatingPath(this);
            mPathMeasure = floatingPath.getPathMeasure();
        }
        
        FloatingAnimator floatingAnimator = floatingTextBuilder.getFloatingAnimator();
        floatingAnimator.applyFloatingAnimation(this);
        
    }

    private void initTextStyle() {
        mTextPaint.setTextSize(floatingTextBuilder.getTextSize());
        mTextPaint.setColor(floatingTextBuilder.getTextColor());
    }


    
    public View getAttachedView(){
        return mAttachedView;
    }

    public PathMeasure getPathMeasure(){
        return mPathMeasure;
    }
    
    public void dettachFromWindow(){
       ViewGroup viewParent = (ViewGroup) getParent();
       viewParent.removeView(this);
    }
    
    

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (floatingTextBuilder == null || mAttachedView == null) {
            return;
        }
        
        float x = (float) (getWidth() / 2.0);
        float y = (float) (getHeight() / 2.0);
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(floatingTextBuilder.getTextContent(), x, baseline , mTextPaint);
    }
}
