package com.eup.screentranslate.callback;

import com.eup.screentranslate.model.Translation;

public interface OnTranslateTokenCallback {
    void onSuccess(Translation translation);
    void onFailed(String message);
}
