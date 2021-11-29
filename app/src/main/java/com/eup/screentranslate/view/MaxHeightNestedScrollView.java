package com.eup.screentranslate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.eup.screentranslate.R;
import com.eup.screentranslate.util.Extentions;

public class MaxHeightNestedScrollView extends NestedScrollView {
    private Context context;
    private AttributeSet attrs;
    private int desStyleAttr;
    int maxHeight = 0;
    private int defaultHeight = 200;


    public MaxHeightNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MaxHeightNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaxHeightNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, Context context1, AttributeSet attrs1, int desStyleAttr) {
        super(context, attrs);
        this.context = context1;
        this.attrs = attrs1;
        this.desStyleAttr = desStyleAttr;
    }

    public MaxHeightNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Context context1, AttributeSet attrs1, int desStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context1;
        this.attrs = attrs1;
        this.desStyleAttr = desStyleAttr;
    }

    public MaxHeightNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs){
        if (attrs == null){
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
        try{
            maxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight,defaultHeight);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight,MeasureSpec.AT_MOST));
    }

    public int maxHeightDp;

    public int getMaxHeightDp() {
        return (int) Extentions.convertDpToPixel(context,maxHeightDp);
    }

    public void setMaxHeightDp(int maxHeightDp) {
        this.maxHeightDp = (int) Extentions.convertDpToPixel(context,maxHeightDp);
    }
}
