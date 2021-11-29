package com.eup.screentranslate.ui.screen.more;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eup.screentranslate.R;
import com.eup.screentranslate.ui.base.BaseViewModel;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.preference.PreferenceHelper;

public class MoreViewModel extends BaseViewModel {
    private Application app;
    public PreferenceHelper preferenceHelper;
    private MoreNavigator navigator;
    public MutableLiveData<String> currentLangName = new MutableLiveData<>();
    public MutableLiveData<Boolean> isAutoTextSpeech = new MutableLiveData<>();

    public MoreViewModel(@NonNull Application application) {
        super(application);
        this.app = application;
        preferenceHelper = new PreferenceHelper(application.getApplicationContext(),Constants.PREFERENCE_SCREEN_TRANS);
    }

    public void setNavigator(MoreNavigator navigator) {
        this.navigator = navigator;
    }

    public void onViewClick(View view){
        navigator.onClick(view);
    }
    public String getCurrentLangAppName(){
        String langApp = preferenceHelper.getLanguageApp();
        Resources resources = app.getResources();
        if (langApp.equals("en")) return resources.getString(R.string.english);
        else if (langApp.equals("vi")) return resources.getString(R.string.vietnam);
        else if (langApp.equals("ja-JP")) return resources.getString(R.string.japanese);
        return resources.getString(R.string.english);
    }

    public void onInitLangAppName(){
        String langAppName = getCurrentLangAppName();
        currentLangName.setValue(langAppName);
    }

    public void onInitTextToSpeech(){
        isAutoTextSpeech.setValue(preferenceHelper.isAutoTextToSpeech());
    }

    public void onSwitchTextAutoSpeech(){
        navigator.onSwitchTextAutoSpeech();
    }

    public void onFastScreenTrans(){
        navigator.onFastScreenTrans();
    }


}
