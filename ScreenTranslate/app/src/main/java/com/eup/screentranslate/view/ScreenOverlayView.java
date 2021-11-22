package com.eup.screentranslate.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.eup.screentranslate.R;
import com.eup.screentranslate.util.Extentions;

public class ScreenOverlayView extends FrameLayout {
    private int rectLeft = 0;
    private int rectTop = 0;
    private int rectRight = 0;
    private int rectBottom = 0;
    public ImageView iconClose;
    private ProgressBar pb;
    private int pbSize = 32;
    private Paint paint;

    public int getRectBottom() {
        return rectBottom;
    }

    public void setRectBottom(int rectBottom) {
        this.rectBottom = rectBottom;
    }

    public ScreenOverlayView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ScreenOverlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScreenOverlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScreenOverlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public int getRectLeft() {
        return rectLeft;
    }

    public void setRectLeft(int rectLeft) {
        this.rectLeft = rectLeft;
    }

    public int getRectTop() {
        return rectTop;
    }

    public void setRectTop(int rectTop) {
        this.rectTop = rectTop;
    }

    public int getRectRight() {
        return rectRight;
    }

    public void setRectRight(int rectRight) {
        this.rectRight = rectRight;
    }

    public ImageView getIconClose() {
        return iconClose;
    }

    public void setIconClose(ImageView iconClose) {
        this.iconClose = iconClose;
    }

    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public int getPbSize() {
        return pbSize;
    }

    public void setPbSize(int pbSize) {
        this.pbSize = pbSize;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    private void initView(Context context) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        pb = new ProgressBar(context);
        pbSize = Math.round(Extentions.convertDpToPixel(context, 32F));
        LayoutParams params = new LayoutParams(pbSize, pbSize);
        addView(pb, params);
        pb.setVisibility(View.GONE);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#8b7355"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rectLeft != rectTop && rectTop != rectRight && rectRight != rectBottom) {

            canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);
        }
    }

    public void setRect(int left, int top, int right, int bottom) {
        this.rectTop = top;
        this.rectLeft = left;
        this.rectRight = right;
        this.rectBottom = bottom;
        invalidate();
    }

   public void addCloseIcon(float x, float y, float size) {
        iconClose = new ImageView(getContext());
        iconClose.setImageResource(R.drawable.ic_close_circle);
        LayoutParams params = new LayoutParams(Math.round(size), Math.round(size));
        addView(iconClose, params);
        iconClose.setX(x);
        iconClose.setY(y);
        iconClose.setVisibility(View.GONE);
    }

    public void clearRect() {
        if (pb.getVisibility() != View.GONE) {
            //
            pb.post(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            });
        }
        this.rectTop = 0;
        this.rectBottom = 0;
        this.rectRight = 0;
        this.rectLeft = 0;
        invalidate();
    }

    public void showPb() {
        pb.setX((float) (rectLeft + (rectRight - rectLeft - pbSize) / 2));
        pb.setY((float) (rectTop + (rectBottom - rectTop - pbSize) / 2));
        pb.setVisibility(View.VISIBLE);
    }
}
