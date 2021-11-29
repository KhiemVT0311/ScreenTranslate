package com.eup.screentranslate.ui.screen.conversation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eup.screentranslate.ui.base.BaseViewModel;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.util.preference.PreferenceHelper;

import io.reactivex.disposables.CompositeDisposable;

public class ConversationViewModel extends BaseViewModel {

    private ConversationNavigator navigator;
    private PreferenceHelper preferenceHelper;
    public MutableLiveData<String> langSource = new MutableLiveData<>();
    public MutableLiveData<String> langDest = new MutableLiveData<>();
    public CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ConversationViewModel(@NonNull Application application) {
        super(application);
        preferenceHelper = new PreferenceHelper(application.getApplicationContext(), Constants.PREFERENCE_SCREEN_TRANS);
    }

    public void setNavigator(ConversationNavigator navigator) {
        this.navigator = navigator;
    }

    public void onInitLang(){
        int posSource = preferenceHelper.getPositionTranslateFrom();
        int posDest = preferenceHelper.getPositionTranslateTo();
        langSource.setValue(LanguageHepler.getNameCountryByPosition(posSource) + " " + LanguageHepler.getEmojiFlag(posSource));
        langDest.setValue(LanguageHepler.getNameCountryByPosition(posDest) + " " + LanguageHepler.getEmojiFlag(posDest));
    }

    public void showChooseSourceLange(boolean isSouce){
        if (isSouce){
            navigator.showDialogChooseLangSource();
        } else navigator.showDialogChooseLangDest();
    }

    public void onRecordSourceLang(){

    }

    public void onRecordDestLang(){

    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onCleared();
    }
}
