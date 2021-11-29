package com.eup.screentranslate.google.ads;

import android.content.Context;

import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdsInterstitial {
    private InterstitialAd interstitialAd;
    private Context context;
    private PreferenceHelper preferenceHelper;
    public AdsInterstitial(Context context, PreferenceHelper preferenceHelper){
        this.context = context;
        this.preferenceHelper = preferenceHelper;
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(preferenceHelper.getIdInterstitial());
        loadNewAd();
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                loadNewAd();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                preferenceHelper.setLastTimeShowInter(System.currentTimeMillis());
            }
        });
    }

    public InterstitialAd loadNewAd(){
        if (interstitialAd != null){
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }
        return interstitialAd;
    }

    public void show(){
        if (preferenceHelper.isPremium() || (System.currentTimeMillis() < preferenceHelper.getIntervalAdsInter() + preferenceHelper.getLastTimeShowInter()))
            return;
        if (interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
            preferenceHelper.setLastTimeShowInter(System.currentTimeMillis());
        }
    }
}
