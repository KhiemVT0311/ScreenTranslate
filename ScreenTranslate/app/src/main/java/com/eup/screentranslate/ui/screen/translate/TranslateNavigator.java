package com.eup.screentranslate.ui.screen.translate;

import com.eup.screentranslate.model.Translation;

import java.util.List;

public interface TranslateNavigator {

    void showDialogChooseLangSource();
    void showDialogChooseLangDest();
    void showHistory();
    void hideHistory();
    void doCopy();
    void showFailedTranslate(String message);
    void swapLang();
    void showLoading();
    void clearInputText();
    void showViewTrans();
    void hideViewTrans();
    void setTextInputTrans(String text);
    void hideLayoutDict();
    void showLayoutDict(List<Translation.Entry> entries);
    void hideLayoutDefinition();
    void showLayoutDefinition(List<Translation.Entry__> entries);
    void hideLayoutExample();
    void showLayoutExample(List<Translation.Example> examples);
    void hideLayoutSynset();
    void showLayoutSynset(List<Translation.Synset> synsets);
    void onBeginOfSpeak(boolean isSource);
    void onEndOfSpeak(boolean isSource);
}
