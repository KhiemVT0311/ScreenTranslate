package com.eup.screentranslate.ui.screen.dialog.record;

public interface RecordNavigator {
    void showRecorderDialog();
    void dismissRecorderDialog();
    void clearFocus();
    void onRecordComplete();
    void onGetRecordedText(String s);
    void onNoTextRecognizeFromRecord();
}
