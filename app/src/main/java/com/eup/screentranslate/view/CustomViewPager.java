package com.eup.screentranslate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private boolean canScrollHorizontal = true;

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager (@NonNull Context context, @NonNull AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (canScrollHorizontal)
            return super.onTouchEvent(ev);

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canScrollHorizontal)
            return super.onInterceptTouchEvent(ev);
        return false;
    }

    public void setCanScrollHorizontal(boolean canScrollHorizontal) {
        this.canScrollHorizontal = canScrollHorizontal;
    }
}
