package com.eup.screentranslate.ui.screen.translate;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.eup.screentranslate.R;
import com.eup.screentranslate.callback.OnTranslateCallBack;
import com.eup.screentranslate.ui.screen.translate.adapter.HistoryAdapter;
import com.eup.screentranslate.database.history.HistoryDatabase;
import com.eup.screentranslate.database.history.HistoryRepository;
import com.eup.screentranslate.model.History;
import com.eup.screentranslate.model.Translation;
import com.eup.screentranslate.ui.base.BaseViewModel;
import com.eup.screentranslate.callback.OnTranslateTokenCallback;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.util.network.NetworkHelper;
import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.eup.screentranslate.util.speaktext.OnSpeakCallback;
import com.eup.screentranslate.util.speaktext.SpeakTextHelper;
import com.eup.screentranslate.util.translate.GetTranslateHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class TranslateViewModel extends BaseViewModel {
    public PreferenceHelper preferenceHelper;
    public Application application;
    private TranslateNavigator navigator;
    public String textInputTrans = "";
    public MutableLiveData<String> translationViveData;
    public MutableLiveData<String> langSource = new MutableLiveData<>();
    public MutableLiveData<String> langDest = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<String> resultTranslate = new MutableLiveData<>();
    private HistoryRepository historyRepository;
    public LiveData<List<History>> allHistoryTranslate = new MutableLiveData<>();
    public SpeakTextHelper speakTextHelper;


    private OnTranslateTokenCallback onTranslateTokenCallback = new OnTranslateTokenCallback() {
        @Override
        public void onSuccess(Translation translation) {
            resultTranslate.setValue(translation.getTranslateContent());
            new Thread() {
                @Override
                public void run() {
                    String langSourceCode = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom());
                    String langDestCode = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo());
                    historyRepository.insertOrUpdate(textInputTrans, translation.getTranslateContent(), langSourceCode, langDestCode);

                    LiveData<List<History>> data = historyRepository.listTransHistory;
                }
            }.start();

            processDefinition(translation);
            processAlternateTranslation(translation);
            processExamples(translation);
            processSynset(translation);

        }

        @Override
        public void onFailed(String message) {
            navigator.showFailedTranslate(message);
        }
    };

    public TranslateViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        preferenceHelper = new PreferenceHelper(application.getApplicationContext(), Constants.PREFERENCE_SCREEN_TRANS);
        historyRepository = new HistoryRepository(HistoryDatabase.getInstance(application.getApplicationContext()).historyDao());
        allHistoryTranslate = historyRepository.listTransHistory;
        speakTextHelper = new SpeakTextHelper(application);
    }

    private void processAlternateTranslation(Translation translation) {
        List<Translation.Dict> dicts = translation.getDict();
        if (dicts == null || dicts.isEmpty()) {
            navigator.hideLayoutDict();
            return;
        }

        List<Translation.Entry> entriesDict = new ArrayList<>();
        for (Translation.Dict dict : dicts) {
            Translation.Entry entry = new Translation.Entry();
            if (dict.getPos() != null && !dict.getPos().isEmpty())
                entry.setWord(dict.getPos());
            else entry.setWord("");

            entriesDict.add(entry);
            entriesDict.addAll(dict.getEntry());
        }

        if (!entriesDict.isEmpty()) {
            navigator.showLayoutDict(entriesDict);
        } else {
            navigator.hideLayoutDict();
        }
    }

    private void processDefinition(Translation translation) {
        List<Translation.Definition> definitions = translation.getDefinitions();
        if (definitions == null || definitions.isEmpty()) {
            navigator.hideLayoutDefinition();
            return;
        }

        ArrayList<Translation.Entry__> entriesDefinition = new ArrayList<>();
        for (Translation.Definition definition : definitions) {
            Translation.Entry__ entry__ = new Translation.Entry__();
            if (definition.getPos() != null && !definition.getPos().isEmpty()) {
                entry__.setGloss(definition.getPos());
            } else entry__.setGloss("");

            entriesDefinition.add(entry__);
            entriesDefinition.addAll(definition.getEntry());
        }

        if (entriesDefinition != null && !entriesDefinition.isEmpty()) {
            navigator.showLayoutDefinition(entriesDefinition);
        } else {
            navigator.hideLayoutDefinition();
        }
    }

    private void processExamples(Translation translation) {
        List<Translation.Example> examples = null;
        if (translation.getExamples() != null)
            examples = translation.getExamples().getExample();
        else {
            navigator.hideLayoutExample();
            return;
        }

        if (examples != null && !examples.isEmpty()) {
            navigator.showLayoutExample(examples);
        } else {
            navigator.hideLayoutExample();
        }
    }

    private void processSynset(Translation translation) {
        List<Translation.Synset> synsets = translation.getSynsets();
        if (synsets == null) {
            navigator.hideLayoutSynset();
        } else {
            navigator.showLayoutSynset(synsets);
        }
    }

    public void onInitLang() {
        int posSource = preferenceHelper.getPositionTranslateFrom();
        int posDest = preferenceHelper.getPositionTranslateTo();
        langSource.setValue(LanguageHepler.getNameCountryByPosition(posSource) + " " + LanguageHepler.getEmojiFlag(posSource));
        langDest.setValue(LanguageHepler.getNameCountryByPosition(posDest) + " " + LanguageHepler.getEmojiFlag(posDest));
    }

    public void translate() {
        if (textInputTrans == null || textInputTrans.isEmpty()) return;
        navigator.showLoading();
        navigator.showViewTrans();
        navigator.hideHistory();
        navigator.hideLayoutDict();
        navigator.hideLayoutDefinition();
        navigator.hideLayoutExample();
        navigator.hideLayoutSynset();

        compositeDisposable.clear();
        String sl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom());
        String tl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo());
        String query = textInputTrans;
        if (!NetworkHelper.isNetworkConnected(getApplication().getApplicationContext()) && GetTranslateHelper.setLanguage(sl,tl)){
            GetTranslateHelper.isExistModel(new GetTranslateHelper.onTranslateOfflineCallback() {
                @Override
                public void onFinished(boolean b) {
                    if (b){
                        GetTranslateHelper.translate(query, new GetTranslateHelper.onTranslateOffline() {
                            @Override
                            public void onResult(String s) {
                                if (null == s || s.isEmpty()){
                                    resultTranslate.setValue(getApplication().getApplicationContext().getString(R.string.something_went_wrong));
                                } else {
                                    resultTranslate.setValue(s);
                                }
                            }
                        });
                    } else {
                        transOnline(query);
                    }
                }
            });
        } else {
            transOnline(query);
        }
    }

    public void transOnline(String query){
        String sl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom());
        String tl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo());
        compositeDisposable.add(GetTranslateHelper.tranlateWithToken(sl, tl, query, "en", onTranslateTokenCallback )) ;

    }

    public void setTextInptTrans(String text) {
        navigator.setTextInputTrans(text);
    }

    private OnSpeakCallback onSpeakSourceCb = new OnSpeakCallback() {
        @Override
        public void success() {
            navigator.onEndOfSpeak(true);
        }

        @Override
        public void failure() {
            navigator.onEndOfSpeak(true);
        }
    };
    //
    private OnSpeakCallback onSpeakDestCb = new OnSpeakCallback() {
        @Override
        public void success() {
            navigator.onEndOfSpeak(false);
        }

        @Override
        public void failure() {
            navigator.onEndOfSpeak(false);
        }
    };

    public void speakSource() {
        if (speakTextHelper != null && textInputTrans != null && textInputTrans.length() > 0){
            navigator.onBeginOfSpeak(true);
            speakTextHelper.speakText(textInputTrans, LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom()), onSpeakSourceCb);
        }
    }

    public void speakDest() {
        if (speakTextHelper != null && resultTranslate.getValue() != null && !resultTranslate.getValue().isEmpty()){
            navigator.onBeginOfSpeak(false);
            speakTextHelper.speakText(resultTranslate.getValue(), LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo()), onSpeakDestCb);
        }
    }

    public void chooseSourceLange(boolean isSouce) {
        if (isSouce){
            navigator.showDialogChooseLangSource();
        } else navigator.showDialogChooseLangDest();

    }

    public void setNavigator(TranslateNavigator navigator) {
        this.navigator = navigator;
    }

    public void swapLang() {
        navigator.swapLang();
        navigator.hideHistory();
        int posSource = preferenceHelper.getPositionTranslateFrom();
        int posDest = preferenceHelper.getPositionTranslateTo();
        preferenceHelper.setPositionTranslateFrom(posDest);
        preferenceHelper.setPositionTranslateTo(posSource);
        resultTranslate.setValue("");
        translate();
        onInitLang();

    }

    public void clearInputText() {
        navigator.hideViewTrans();
        navigator.clearInputText();

    }

    public void doCopy() {
        navigator.doCopy();
    }

    public void showHistory() {
        navigator.showHistory();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public void onItemHistoryClick(History history) {
        navigator.setTextInputTrans(history.text);
        navigator.hideHistory();
    }

    public void onDeleteHistoryClick(History history) {
        new Thread(){
            @Override
            public void run() {
                historyRepository.deleteSingle(history);
            }
        }.start();
    }

    @BindingAdapter("app:listHistoryData")
    public static void setHistoryData(RecyclerView rv, LiveData<List<History>> data){
        if (rv == null) return;
        RecyclerView.Adapter adapter = rv.getAdapter();
        if (adapter instanceof HistoryAdapter){
            ((HistoryAdapter) adapter).setHistories(data.getValue());
        }
    }
}
