package com.unnamed.studnetz.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.unnamed.studnetz.R;

public class SubjectView extends View {

    //Todo: revise

    Paint mTextPaint;

    int mBackgroundId = R.drawable.subject;
    Drawable mBackground;
    Drawable mImage;

    // Attributes

    int mTextColor;

    float mTextHeight;

    String mSubject;

    int mDrawableId;

    int mBackgroundColor;

    float mTextPaddingBottom = 20;


    public SubjectView(Context context) {
        this(context, null);
    }

    public SubjectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setFocusable(true);

        final TypedArray a  = getContext().obtainStyledAttributes(attrs, R.styleable.SubjectView, defStyleAttr, 0);
        if(a.hasValue(R.styleable.SubjectView_textHeight)){
            TypedValue textHeightValue = new TypedValue();
            a.getValue(R.styleable.SubjectView_textHeight, textHeightValue);

            if (textHeightValue.type == TypedValue.TYPE_DIMENSION) {
                setTextHeight(a.getDimension(R.styleable.SubjectView_textHeight, 0));
            } else {
                setTextHeight(a.getInteger(R.styleable.SubjectView_textHeight,0));
            }
        }else{
            setTextHeight(75);
        }

        if(a.hasValue(R.styleable.SubjectView_subjectText)){



            TypedValue subjectValue = new TypedValue();
            a.getValue(R.styleable.SubjectView_subjectText, subjectValue);

            if (subjectValue.type == TypedValue.TYPE_STRING) {
                setSubject(a.getString(R.styleable.SubjectView_subjectText));
            } else {
                setSubject(getResources().getString(a.getInteger(R.styleable.SubjectView_subjectText,0)));
            }

        }else{
            setSubject("Subject");
        }

        if(a.hasValue(R.styleable.SubjectView_icon)){
            setDrawableId(a.getInteger(R.styleable.SubjectView_icon,0));
        }else{
            setDrawableId(R.drawable.ic_subjects_physics);
        }

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);

        if(mTextHeight == 0){
            mTextHeight = mTextPaint.getTextSize();
        }else{
            mTextPaint.setTextSize(mTextHeight);
        }

        mBackground = ContextCompat.getDrawable(getContext(),mBackgroundId);
        mImage = ContextCompat.getDrawable(getContext(),mDrawableId);

        if(a.hasValue(R.styleable.SubjectView_subjectTextColor)){
            setTextColor(a.getInteger(R.styleable.SubjectView_subjectTextColor,0));
        }else{
            setTextColor(Color.WHITE);
        }

        if(a.hasValue(R.styleable.SubjectView_subjectBackgroundColor)){
            setBackgroundColor(a.getInteger(R.styleable.SubjectView_subjectBackgroundColor,0));
        }else{
            setBackgroundColor(getResources().getColor(R.color.black_overlay));
        }

        a.recycle();



    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        float px = measuredWidth / 2.0f;
        float py = measuredHeight /2.0f;

        mBackground.setBounds(0,0,getMeasuredHeight(),getMeasuredWidth());
        mBackground.draw(canvas);

        System.out.println("dfsdsaffdsadfsa" + mSubject + " " + mTextHeight);

        int textWidth = (int) mTextPaint.measureText(mSubject);
        px = px-textWidth/2.0f;
        py =  measuredHeight - mTextHeight/4.0f - mTextPaddingBottom;
        canvas.drawText(mSubject,px,py,mTextPaint);

        mImage.setBounds(getPaddingStart(), getPaddingTop(),getMeasuredWidth()-getPaddingEnd(),getMeasuredHeight()-getPaddingBottom());
        mImage.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int measuredWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);

        int d = Math.min(measuredWidth, measureHeight);

        setMeasuredDimension(d,d);
    }

    private int measure(int measureSpec){
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.UNSPECIFIED){
            result = 250;
        }else{
            result = specSize;
        }

        return result;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mImage.setColorFilter(mTextColor, PorterDuff.Mode.MULTIPLY);
        mTextPaint.setColor(mTextColor);
        invalidate();
        requestLayout();
    }

    public void setTextHeight(float mTextHeight) {
        this.mTextHeight = mTextHeight;
        invalidate();
        requestLayout();
    }

    public void setSubject(String mSubject) {
        this.mSubject = mSubject;
        invalidate();
        requestLayout();
    }

    public void setDrawableId(int mDrawableId) {
        this.mDrawableId = mDrawableId;
        invalidate();
        requestLayout();
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        mBackground.setColorFilter(mBackgroundColor, PorterDuff.Mode.MULTIPLY);
        invalidate();
        requestLayout();
    }


    public float getTextHeight() {
        return mTextHeight;
    }
    public int getBackgroundColor() {
        return mBackgroundColor;
    }
    public int getDrawableId() {
        return mDrawableId;
    }
    public String getSubject() {
        return mSubject;
    }
    public int getTextColor() {
        return mTextColor;
    }

}
