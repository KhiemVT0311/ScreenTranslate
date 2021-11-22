package com.eup.screentranslate.callback;

public interface OnTranslateCallBack {
    void onSuccess(String query, String mean, String romaji, String from, String to, String text);
    void onFailed(String message);
}
