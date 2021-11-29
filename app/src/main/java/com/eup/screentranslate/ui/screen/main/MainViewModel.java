package com.eup.screentranslate.ui.screen.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.eup.screentranslate.ui.base.BaseViewModel;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.preference.PreferenceHelper;

public class MainViewModel extends BaseViewModel {
    public PreferenceHelper preferenceHelper;


    public MainViewModel(@NonNull Application application) {
        super(application);
        preferenceHelper = new PreferenceHelper(application.getApplicationContext(),Constants.PREFERENCE_SCREEN_TRANS);
    }
}
