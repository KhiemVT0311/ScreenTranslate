package com.eup.screentranslate.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TableLayout;

import com.eup.screentranslate.R;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;

public class BounceView implements IBounceViewAnim{

    private WeakReference<View> view ;
    private WeakReference<Dialog> dialog;
    private WeakReference<PopupWindow> popup;
    private WeakReference<TabLayout> tabLayout;
    private boolean isTouchInsideView = true;
    private float pushInScaleX = PUSH_IN_SCALE_X;
    private float pushInScaleY = PUSH_IN_SCALE_Y;
    private float popOutScaleX = POP_OUT_SCALE_X;
    private float popOutScaleY = POP_OUT_SCALE_Y;
    private int pushInAnimDuration = PUSH_IN_ANIM_DURATION;
    private int popOutAnimDuration = POP_OUT_ANIM_DURATION;
    private AccelerateDecelerateInterpolator pushInInterpolator =
            DEFAULT_INTERPOLATOR;
    private AccelerateDecelerateInterpolator popOutInterpolator =
            DEFAULT_INTERPOLATOR;

    public BounceView(View view) {
        this.view = new WeakReference<>(view);
        if (this.view.get() != null){
            if (!this.view.get().hasOnClickListeners()){
                this.view.get().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }
    }

    private BounceView(Dialog dialog) {
        this.dialog = new WeakReference<>(dialog);
    }

    private BounceView(PopupWindow popupWindow){
        this.popup = new WeakReference<>(popupWindow);
    }

    private BounceView(TabLayout tabLayout) {
        this.tabLayout = new WeakReference<>(tabLayout);
    }


    @Override
    public IBounceViewAnim setScaleForPushInAnim(float scaleX, float scaleY) {
        pushInScaleX = scaleX;
        pushInScaleY = scaleY;
        return this;
    }

    @Override
    public IBounceViewAnim setScaleForPopOutAnim(float scaleX, float scaleY) {
        popOutScaleX = scaleX;
        popOutScaleY = scaleY;
        return this;
    }

    @Override
    public IBounceViewAnim setPushInAnimDuration(int timeInMillis) {
        pushInAnimDuration = timeInMillis;
        return this;
    }

    @Override
    public IBounceViewAnim setPopOutAnimDuration(int timeInMillis) {
        popOutAnimDuration = timeInMillis;
        return this;
    }

    @Override
    public IBounceViewAnim setInterpolatorPushIn(AccelerateDecelerateInterpolator interpolatorPushIn) {
        pushInInterpolator = interpolatorPushIn;
        return this;
    }

    @Override
    public IBounceViewAnim setInterpolatorPopOut(AccelerateDecelerateInterpolator interpolatorPopOut) {
        popOutInterpolator = interpolatorPopOut;
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setAnimToView(){
        if (view != null){
            view.get().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == MotionEvent.ACTION_DOWN){
                        isTouchInsideView = true;
                        startAnimScale(view,
                                pushInScaleX,
                                pushInScaleY,
                                pushInAnimDuration,
                                pushInInterpolator,
                                0);
                    } else if (action == MotionEvent.ACTION_UP){
                        if (isTouchInsideView){
                            view.animate().cancel();
                            startAnimScale(view,
                                    popOutScaleX,
                                    popOutScaleY,
                                    popOutAnimDuration,
                                    popOutInterpolator,
                                    0);
                            startAnimScale(view,
                                    1f,
                                    1f,
                                    popOutAnimDuration,
                                    popOutInterpolator,
                                    popOutAnimDuration+1);
                            return false;
                        }
                    } else if (action == MotionEvent.ACTION_CANCEL){
                        if (isTouchInsideView){
                            view.animate().cancel();
                            startAnimScale(view,
                                    1f,
                                    1f,
                                    popOutAnimDuration,
                                    DEFAULT_INTERPOLATOR,
                                    0);
                        }
                        return true;
                    } else if (action == MotionEvent.ACTION_MOVE){
                        if (isTouchInsideView){
                            float currentX = motionEvent.getX();
                            float currentY = motionEvent.getY();
                            float currentPosX = currentX + view.getLeft();
                            float currentPosY = currentY + view.getTop();
                            float viewLeft = view.getLeft();
                            float viewTop = view.getTop();
                            float viewRight = view.getRight();
                            float viewBottom = view.getBottom();
                            if (!(currentPosX > viewLeft && currentPosX < viewRight && currentPosY > viewTop && currentPosY < viewBottom)){
                              isTouchInsideView = false;
                              view.animate().cancel();
                              startAnimScale(view, 1f,
                                      1f,
                                      popOutAnimDuration,DEFAULT_INTERPOLATOR,
                                      0);
                            }
                            return  true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void startAnimScale(View view, float scaleX, float scaleY, int animDuration, AccelerateDecelerateInterpolator interpolator, int startDelay){
        ObjectAnimator animX = ObjectAnimator.ofFloat(view,"scaleX",scaleX);
        ObjectAnimator  animY = ObjectAnimator.ofFloat(view,"scaleY",scaleY);
        AnimatorSet animatorSet = new AnimatorSet();
        animX.setDuration(animDuration);
        animX.setInterpolator(interpolator);
        animY.setDuration(animDuration);
        animY.setInterpolator(interpolator);
        animatorSet.playTogether(animX,animY);
        animatorSet.setStartDelay(startDelay);
        animatorSet.start();
    }

    private void setAnimToPopUp(){
        if (popup.get() != null){
            popup.get().setAnimationStyle(R.style.CustomDialogAnimation);
        }
    }

    private void setAnimToDiaLog(){
        if (dialog.get() != null){
            Window dialogWindow =  dialog.get().getWindow();
            dialogWindow.setWindowAnimations(R.style.CustomDialogAnimation);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setAnimToTabLayout(){
        if (tabLayout.get() != null){
            for (int i=0;i<tabLayout.get().getTabCount();i++) {
                TabLayout.Tab tab = tabLayout.get().getTabAt(i);
                View tabView = ((ViewGroup) tabLayout.get().getChildAt(0)).getChildAt(i);
                tabView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        int action = motionEvent.getAction();
                        if (action == MotionEvent.ACTION_DOWN){
                            isTouchInsideView = true;
                            startAnimScale(
                                    view,
                                    pushInScaleX,
                                    pushInScaleY,
                                    pushInAnimDuration,
                                    pushInInterpolator,
                                    0
                            );
                            return true;
                        } else if (action == MotionEvent.ACTION_UP) {
                            if (isTouchInsideView){
                                view.animate().cancel();
                                startAnimScale(
                                        view,
                                        popOutScaleX,
                                        popOutScaleY,popOutAnimDuration,
                                        popOutInterpolator,
                                        0
                                );
                                startAnimScale(
                                        view,
                                        1f,
                                        1f,
                                        popOutAnimDuration,
                                        popOutInterpolator,
                                        popOutAnimDuration+1
                                );
                                tab.select();
                                return false;
                            }
                        } else if (action == MotionEvent.ACTION_CANCEL){
                            if (isTouchInsideView){
                                view.animate().cancel();
                                startAnimScale(
                                        view,
                                        1f,
                                        1f,
                                        popOutAnimDuration,
                                        DEFAULT_INTERPOLATOR,
                                        0
                                );
                            }
                            return true;
                        } else if (action == MotionEvent.ACTION_MOVE){
                            if (isTouchInsideView){
                                float currentX = motionEvent.getX();
                                float currentY = motionEvent.getY();
                                float currentPosX = currentX + view.getLeft();
                                float currentPosY = currentY + view.getTop();
                                float viewLeft = view.getLeft();
                                float viewTop = view.getTop();
                                float viewRight = view.getRight();
                                float viewBottom = view.getBottom();
                                if (!(currentPosX > viewLeft && currentPosX <viewRight && currentPosY > viewTop && currentPosY <viewBottom )){
                                    isTouchInsideView = false;
                                    view.animate().cancel();
                                    startAnimScale(
                                            view,
                                            1f,
                                            1f,
                                            popOutAnimDuration,
                                            DEFAULT_INTERPOLATOR,
                                            0
                                    );
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    public static final float PUSH_IN_SCALE_X = 0.9f;
    public static final float PUSH_IN_SCALE_Y = 0.9f;
    public static final float POP_OUT_SCALE_X = 1.1f;
    public static final float POP_OUT_SCALE_Y = 1.1f;
    public static final int PUSH_IN_ANIM_DURATION = 100;
    public static final int POP_OUT_ANIM_DURATION = 100;
    public static AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();

    public static BounceView addAnimTo(View view){
        BounceView bounceAnim = new BounceView(view);
        bounceAnim.setAnimToView();
        return bounceAnim;
    }

    public static void addAnimTo(Dialog dialog){
        BounceView bounceAnim = new BounceView(dialog);
        bounceAnim.setAnimToDiaLog();
    }

    public static void addAnimTo(PopupWindow popupWindow){
        BounceView bounceAnim = new BounceView(popupWindow);
        bounceAnim.setAnimToPopUp();
    }

    public static BounceView addAnimTo(TabLayout tabLayout){
        BounceView bounceAnim = new BounceView(tabLayout);
        bounceAnim.setAnimToTabLayout();
        return bounceAnim;
    }
}
