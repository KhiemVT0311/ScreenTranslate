package com.eup.screentranslate.callback;

import java.util.ArrayList;

public interface OnRecognitionCallback {
    void onResults(ArrayList<String> texts);
    void onStartSpeech();
    void onEndOfSpeech();
    void toastMessage(String message);
}
