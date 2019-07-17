package com.gd70.android.expcal;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

public class CustomTextView extends AppCompatTextView {

    private float amt,initialAmt,finalAmt;
    private String value;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAmt(int amt) {
        this.amt = amt;
        invalidate();
    }

    public void setInitialAmt(float initialAmt) {
        this.initialAmt = initialAmt;
    }

    public void setFinalAmt(float finalAmt) {
        this.finalAmt = finalAmt;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        value=this.getText().toString();
        initialize();
    }

    private void initialize()
    {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this,"amt",0,Integer.parseInt(value));
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator);
        animatorSet.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setText(Float.toString(amt));
    }
}
