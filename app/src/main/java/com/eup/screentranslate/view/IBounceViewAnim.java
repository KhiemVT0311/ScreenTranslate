package com.eup.screentranslate.view;

import android.view.animation.AccelerateDecelerateInterpolator;

public interface IBounceViewAnim {
    IBounceViewAnim setScaleForPushInAnim(float scaleX, float scaleY);
    IBounceViewAnim setScaleForPopOutAnim(float scaleX, float scaleY);

    IBounceViewAnim setPushInAnimDuration(int timeInMillis);
    IBounceViewAnim setPopOutAnimDuration(int timeInMillis);
    IBounceViewAnim setInterpolatorPushIn(AccelerateDecelerateInterpolator interpolatorPushIn);
    IBounceViewAnim setInterpolatorPopOut(AccelerateDecelerateInterpolator interpolatorPopOut);
}
