package com.eup.screentranslate.ui.screen.screentrans;

import android.app.Application;

import androidx.annotation.NonNull;

import com.eup.screentranslate.ui.base.BaseViewModel;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.preference.PreferenceHelper;

public class ScreenTransViewModel extends BaseViewModel {
    private ScreenTransNavigator navigator;
    public PreferenceHelper preferenceHelper;
    public ScreenTransViewModel(@NonNull Application application) {
        super(application);
        preferenceHelper = new PreferenceHelper(application.getApplicationContext(),Constants.PREFERENCE_SCREEN_TRANS);
    }

    public void setNavigator(ScreenTransNavigator navigator) {
        this.navigator = navigator;
    }

    public void onStartScreenTrans(){
        navigator.onStartScreenTrans();
    }

}
