package com.eup.screentranslate.google.firebase;

import androidx.annotation.NonNull;

import com.eup.screentranslate.R;
import com.eup.screentranslate.model.AdsInfo;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

public class FirebaseConfig {
    public FirebaseRemoteConfig remoteConfig;
    public FirebaseRemoteConfigSettings configSettings;

    public FirebaseConfig() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(180)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default);
    }

    public void fetchNewConfig(PreferenceHelper preferenceHelper){
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isComplete()){
                            AdsInfo adsInfo = new Gson().fromJson(remoteConfig.getString(Constants.REMOTE_KEY.CONFIG_ANDROID),AdsInfo.class);
                            if (adsInfo != null){
                                if (adsInfo.getAdsProb() != null){
                                    preferenceHelper.setBanner(adsInfo.getAdsProb().getBanner());
                                    preferenceHelper.setNative(adsInfo.getAdsProb().getNative());
                                    preferenceHelper.setCount(adsInfo.getAdsProb().getCount());
                                    preferenceHelper.setAdPress(adsInfo.getAdsProb().getAdpress());
                                    preferenceHelper.setIntervalAdsInter(adsInfo.getAdsProb().getIntervalAdsInter());
                                }

                                if (adsInfo.getAdsid() != null){
                                    AdsInfo.Adsid adsid = adsInfo.getAdsid();
                                    preferenceHelper.setIdBanner(adsid.getAndroid().getBanner());
                                    preferenceHelper.setIdInterstitial(adsid.getAndroid().getInterstitial());
                                    preferenceHelper.setIdReward(adsid.getAndroid().getReward());
                                    preferenceHelper.setIdNative(adsid.getAndroid().getNative());
                                }
                            }
                        }
                    }
                });
    }
}
