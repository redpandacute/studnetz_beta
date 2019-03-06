package com.unnamed.studnetz.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.unnamed.studnetz.R;

public class ExampleCustomView extends View {

    private String mText;
    private int mColorRef;
    private int mTextColorRef;

    private Paint mCirclePaint;
    private Paint mTextPaint;

    private int mTextHeight;

    public ExampleCustomView(Context context) {
        this(context, null);
    }

    public ExampleCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExampleCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setFocusable(true);

        // Read xml attributes
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExampleCustomView, defStyleAttr,0);
        if(a.hasValue(R.styleable.ExampleCustomView_backgroundColor)){
            setColorRef(a.getInteger(R.styleable.ExampleCustomView_backgroundColor,0));
        }else{
            setColorRef(getResources().getColor(R.color.dark_grey));
        }
        if(a.hasValue(R.styleable.ExampleCustomView_text)){
            setText(a.getString(R.styleable.ExampleCustomView_text));
        }else{
            setText("***");
        }
        if(a.hasValue(R.styleable.ExampleCustomView_textColor)){
            setmTextColorRef(a.getInteger(R.styleable.ExampleCustomView_textColor,0));
        }else{
            setmTextColorRef(getResources().getColor(R.color.colorWhite));
        }

        a.recycle();

        Context c = this.getContext();

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mColorRef);
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColorRef);
        mTextPaint.setTextSize(75);
        mTextPaint.setFakeBoldText(true);

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
        mTextHeight = bounds.height();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int px = measuredWidth / 2;
        int py = measuredHeight /2;

        int radius = Math.min(px,py);

        canvas.drawCircle(px, py ,radius, mCirclePaint);

        int textWidth = (int) mTextPaint.measureText(mText);
        px = px-textWidth/2;
        py = py+mTextHeight/2;
        canvas.drawText(mText,px,py,mTextPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measuredWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);

        int d = Math.min(measuredWidth, measureHeight);

        setMeasuredDimension(d,d);
    }

    private int measure(int measureSpec){
        int result = 0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.UNSPECIFIED){
            // Return a default size if no bounds are specified
            result = 100;
        }else{
            // If you want to fill the available space
            // return the specSize
            result = 150;
        }

        return result;
    }

    public void setText(String text){
        mText = text;
        invalidate(); // If the View is visible onDraw will be called (repaint)
    }

    public void setColorRef(int colorRef){
        mColorRef = colorRef;
        invalidate();
    }

    public void setmTextColorRef(int mTextColorRef) {
        this.mTextColorRef = mTextColorRef;
        invalidate();
    }

    public String getText() {
        return mText;
    }

    public int getColorRef() {
        return mColorRef;
    }

    public int getmTextColorRef() {
        return mTextColorRef;
    }


}
