package com.eup.screentranslate.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.eup.screentranslate.util.Prekey;
import com.eup.screentranslate.util.language.LanguageHepler;

public class PreferenceHelper {//trình trợ giúp ưu tiên
    private Context context;
    private String name;
    private SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context, String name) {
        this.context = context;
        this.name = name;
        sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }



    public int getPositionTranslateFrom(){
        return sharedPreferences.getInt(Prekey.POSITION_TRANS_FROM, LanguageHepler.POS_JAPANESE);
    }

    public int getPositionTranslateTo(){
        return sharedPreferences.getInt(Prekey.POSTION_TRANS_TO, LanguageHepler.getDefaultPositionLanguage());
    }

    public void setPositionTranslateFrom(int position){
        sharedPreferences.edit().putInt(Prekey.POSITION_TRANS_FROM, position).apply();
    }

    public void setPositionTranslateTo(int position){
        sharedPreferences.edit().putInt(Prekey.POSTION_TRANS_TO, position).apply();
    }

    public void setLanguageApp(String lang){
        if (lang.equals("vi")){
            Log.d("Log","vi language");
        }
        sharedPreferences.edit().putString(Prekey.LANGUAGE_APP, lang).apply();
    }

    public String getLanguageApp(){
        return sharedPreferences.getString(Prekey.LANGUAGE_APP, "vi");
    }

    public void setAutoTextToSpeech(boolean isAuto){
        sharedPreferences.edit().putBoolean(Prekey.IS_AUTO_TEXT_SPEECH, isAuto).apply();
    }

    public boolean isAutoTextToSpeech(){
        return sharedPreferences.getBoolean(Prekey.IS_AUTO_TEXT_SPEECH, false);
    }

    public void setBanner(int banner){
        sharedPreferences.edit().putInt(Prekey.BANNER, banner).apply();
    }

    public int getBanner(){
        return sharedPreferences.getInt(Prekey.BANNER, 1);
    }

    public void setNative(int nativee){
        sharedPreferences.edit().putInt(Prekey.NATIVE, nativee).apply();
    }

    public int getNative(){
        return sharedPreferences.getInt(Prekey.NATIVE, 1);
    }

    public void setCount(long count){
        sharedPreferences.edit().putLong(Prekey.COUNT, count).apply();
    }

    public long getCount(){
        return sharedPreferences.getLong(Prekey.COUNT, 0);
    }

    public void setAdPress(long adpress){
        sharedPreferences.edit().putLong(Prekey.ADPRESS, adpress).apply();
    }

    public long getAdPress(){
        return sharedPreferences.getLong(Prekey.ADPRESS, 0);
    }

    public void setIntervalAdsInter(long time){ // miliseconds
        sharedPreferences.edit().putLong(Prekey.INTERVAL_ADS_INTER, time).apply();
    }

    public long getIntervalAdsInter(){
        return sharedPreferences.getLong(Prekey.INTERVAL_ADS_INTER, 120000);
    }

    public void setIdBanner(String idBanner){
        sharedPreferences.edit().putString(Prekey.ID_BANNER, idBanner).apply();
    }

    public String getIdBanner(){
        return sharedPreferences.getString(Prekey.ID_BANNER, "ca-app-pub-3940256099942544/6300978111");
    }

    public void setIdInterstitial(String idInterstitial){
        sharedPreferences.edit().putString(Prekey.ID_INTERSTITIAL, idInterstitial).apply();
    }

    public String getIdInterstitial(){
        return sharedPreferences.getString(Prekey.ID_INTERSTITIAL, "ca-app-pub-3940256099942544/1033173712");
    }

    public void setIdReward(String idReward){
        sharedPreferences.edit().putString(Prekey.ID_REWARD, idReward).apply();
    }

    public String getIdReward(){
        return sharedPreferences.getString(Prekey.ID_REWARD, "ca-app-pub-3940256099942544/5354046379");
    }

    public void setIdNative(String idReward){
        sharedPreferences.edit().putString(Prekey.ID_NATIVE, idReward).apply();
    }

    public String getIdNative(){
        return sharedPreferences.getString(Prekey.ID_NATIVE, "");
    }

    public void setLastTimeClickedAds(long time){ // (s)
        sharedPreferences.edit().putLong(Prekey.LAST_TIME_CLICKED_ADS, time).apply();
    }

    public long getLastTimeClickedAds(){
        return sharedPreferences.getLong(Prekey.LAST_TIME_CLICKED_ADS, 0);
    }

    public void setPremium(boolean isPremium){
        sharedPreferences.edit().putBoolean(Prekey.PREMIUM, isPremium).apply();
    }

    public boolean isPremium(){
        return sharedPreferences.getBoolean(Prekey.PREMIUM, false);
    }

    public void setLastTimeShowInter(long time){
        sharedPreferences.edit().putLong(Prekey.LAST_TIME_SHOW_INTER, time).apply();
    }

    public long getLastTimeShowInter(){
        return sharedPreferences.getLong(Prekey.LAST_TIME_SHOW_INTER, 0);
    }

    public boolean isShowTipScreenTrans(){
        return sharedPreferences.getBoolean(Prekey.SHOW_TIP_SCREEN_TRANS,true);
    }

    public void setIsBooleanScreenTran(boolean isShowTipScreenTrans){
        sharedPreferences.edit().putBoolean(Prekey.SHOW_TIP_SCREEN_TRANS,isShowTipScreenTrans).apply();
    }

    public boolean isShowFurigana(){
        return sharedPreferences.getBoolean("cb1",true);
    }

    public void setIsShowFurigana(boolean isShowFurigana){
        sharedPreferences.edit().putBoolean("cb1",isShowFurigana()).apply();
    }
}
