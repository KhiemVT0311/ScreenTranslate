package com.eup.screentranslate.ui.screen.dialog.record;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eup.screentranslate.callback.OnRecognitionCallback;
import com.eup.screentranslate.ui.base.BaseViewModel;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.eup.screentranslate.util.speechrecognizer.SpeechRecognizerHelper;

import java.util.ArrayList;

public class RecordViewModel extends BaseViewModel {
    private RecordNavigator navigatorConversation;
    private RecordNavigator navigatorTranslate;
    private PreferenceHelper preferenceHelper;
    private boolean isConversation = false;
    public boolean isLeftConversation = true;

    public RecordViewModel(@NonNull Application application) {
        super(application);
        preferenceHelper = new PreferenceHelper(application.getApplicationContext(),Constants.PREFERENCE_SCREEN_TRANS);
    }

    public SpeechRecognizerHelper speechRecognizerHelper = new SpeechRecognizerHelper(getApplication().getApplicationContext(), new OnRecognitionCallback() {
        @Override
        public void onResults(ArrayList<String> texts) {
            if (texts != null &&texts.size() >0){
                String result = texts.get(0);
                if (isConversation){
                    navigatorConversation.onGetRecordedText(result);
                } else {
                    navigatorTranslate.onGetRecordedText(result);
                }
            } else {
                if (isConversation){
                    navigatorConversation.onNoTextRecognizeFromRecord();
                } else {
                    navigatorTranslate.onNoTextRecognizeFromRecord();
                }
            }
        }

        @Override
        public void onStartSpeech() {

        }

        @Override
        public void onEndOfSpeech() {
            speechRecognizerHelper.setmIsListening(false);
            if (isConversation){
                navigatorConversation.dismissRecorderDialog();
            } else {
                navigatorTranslate.dismissRecorderDialog();
            }
        }

        @Override
        public void toastMessage(String message) {
            Toast.makeText(getApplication().getApplicationContext(), message,Toast.LENGTH_SHORT).show();
        }
    });


    public void setNavigatorConversation(RecordNavigator navigatorConversation,RecordNavigator navigatorTranslate) {
       if (navigatorConversation!= null) this.navigatorConversation = navigatorConversation;
        if (navigatorTranslate != null) this.navigatorTranslate = navigatorTranslate;
    }

    public void onRecordSourceLang(boolean isConversation){
        isLeftConversation = true;
        if (isConversation){
            this.isConversation = true;
            navigatorConversation.showRecorderDialog();
        } else {
            this.isConversation = false;
            navigatorTranslate.showRecorderDialog();
        }
        speechRecognizerHelper.startListen(LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom()));

    }

    public void onRecordDestLang(boolean isConversation){
        isLeftConversation = false;
        if (isConversation){
            this.isConversation = true;
            navigatorConversation.showRecorderDialog();
        } else {
            this.isConversation = false;
            navigatorTranslate.showRecorderDialog();
        }
        speechRecognizerHelper.startListen(LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo()));
    }

    public boolean isListening(){
        if (speechRecognizerHelper != null){
            return speechRecognizerHelper.ismIsListening();
        }
        return false;
    }

    public void stopListening(){
        if (speechRecognizerHelper != null){
            speechRecognizerHelper.stopListening();
        }
    }

    public void destroySpeedchRecognizer(){
        if (speechRecognizerHelper != null){
            speechRecognizerHelper.destroy();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
