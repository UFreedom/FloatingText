package com.ufreedom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * UFreedom
 */
public class FloatingTextView extends TextView {

    public static final String TAG = "FloatingTextView";
    private static final int PATH_WIDTH = 2;

    private FloatingText.FloatingTextBuilder floatingTextBuilder;
    private Paint mTextPaint;
 
    private Path mEffectPath;
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

            Log.e(TAG," -----onMeasure-----floatingTextBuilder == null || mAttachedView == null -");
            return;
        }


        Log.e(TAG," -----onMeasure------");

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


        
    /*@Override
    public void layout(int l, int t, int r, int b) {

       Log.e(TAG," -----layout 原始的----: left: "+l+" top: "+t+" right:"+r+" bottom:"+b);
       if (floatingTextBuilder == null || mAttachedView == null){
            Log.e(TAG," -----layout- floatingTextBuilder is null-----");
            super.layout(l, t, r, b);
        }else {
            Rect rect = new Rect();

           Log.e(TAG," mAttachedView: "+mAttachedView.hashCode());


            mAttachedView.getGlobalVisibleRect(rect);
            int[] location = new int[2];
            getLocationOnScreen(location);
            rect.offset(-location[0], -location[1]);

            Log.e(TAG," mes: "+getMeasuredWidth()+" w: "+getWidth());

            Log.e(TAG," -----Rect----: left: "+rect.left+" top: "+rect.top+" right:"+rect.right+" bottom:"+rect.bottom);


           int left = rect.left + (rect.width() - getMeasuredWidth()) / 2;
            int top = rect.top + (rect.height() - getMeasuredHeight()) / 2;
           
           
           
            int bottom = top + getMeasuredHeight();
            int right = left + getMeasuredWidth();
            Log.e(TAG," -----layout----: left: "+left+" top: "+top+" right:"+right+" bottom:"+bottom);
            super.layout(left,top,right,bottom);
        }
       
    }*/

    public void flyText(View view) {

        layout(0,0,0,0);

        mAttachedView = view;

        Rect rect = new Rect();
        mAttachedView.getGlobalVisibleRect(rect);
        int[] location = new int[2];
        getLocationOnScreen(location);
        rect.offset(-location[0], -location[1]);
        

        Log.e(TAG," mes: "+getMeasuredWidth()+" w: "+getWidth());

        int left = rect.left + (rect.width() - getMeasuredWidth()) / 2 + floatingTextBuilder.getOffsetX();
        int top = rect.top + (rect.height() - getMeasuredHeight()) / 2 + + floatingTextBuilder.getOffsetY();
        int bottom = top + getMeasuredHeight();
        int right = left + getMeasuredWidth();
        Log.e(TAG," -----flyText----: left: "+left+" top: "+top+" right:"+right+" bottom:"+bottom);
        layout(left,top,right,bottom);
        invalidate();
        
        FloatingPathEffect floatingPathEffect = floatingTextBuilder.getFloatingPathEffect();
        if (floatingPathEffect != null){
            FloatingPath floatingPath = floatingPathEffect.getFloatingPath(this);
            mEffectPath = floatingPath.getPath();
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
            Log.e(TAG, "----onDraw----->  flyTextBuilder null or mAttachedView null");
            return;
        }
        
        float x = (float) (getWidth() / 2.0);
        float y = (float) (getHeight() / 2.0);
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(floatingTextBuilder.getTextContent(), x, baseline , mTextPaint);
    }
}
