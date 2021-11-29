package com.eup.screentranslate.ui.screen.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eup.screentranslate.ui.base.BaseViewModel;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.preference.PreferenceHelper;

public class SplashViewModel extends BaseViewModel {

    public PreferenceHelper preferenceHelper;
    public SplashViewModel(@NonNull Application application) {
        super(application);
        preferenceHelper = new PreferenceHelper(application.getApplicationContext(), Constants.PREFERENCE_SCREEN_TRANS);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
