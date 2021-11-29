package com.eup.screentranslate.google.ads;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdsBanner {
    private Context context;
    private PreferenceHelper preferenceHelper;

    public AdsBanner(Context context, PreferenceHelper preferenceHelper) {
        this.context = context;
        this.preferenceHelper = preferenceHelper;
    }


    public AdView banner(FrameLayout layoutAds, AdSize adSize) {

        if (isHideAdsBanner() || preferenceHelper.isPremium()) {
            if (layoutAds != null) layoutAds.removeAllViews();
            return null;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        AdView adView = new AdView(context);
        adView.setLayoutParams(params);
        adView.setAdSize(adSize);
        adView.setAdUnitId(preferenceHelper.getIdBanner());
        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (isHideAdsBanner() || preferenceHelper.isPremium()) {
                    layoutAds.removeAllViews();
                } else {
                    layoutAds.removeAllViews();
                    layoutAds.addView(adView);
                }
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                layoutAds.removeAllViews();
                preferenceHelper.setLastTimeClickedAds(System.currentTimeMillis());

            }
        });
        return adView;
    }

    public AdView mediumBanner(FrameLayout layout) {
        if (isHideAdsBanner() || preferenceHelper.isPremium()) {
            return null;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        AdView adView = new AdView(context);
        adView.setLayoutParams(params);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(preferenceHelper.getIdBanner());
        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                layout.removeAllViews();
                layout.addView(adView);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                layout.removeAllViews();
                preferenceHelper.setLastTimeClickedAds(System.currentTimeMillis());
            }
        });

        return adView;
    }

    private boolean isHideAdsBanner() {
        long lastTimeClickedAds = preferenceHelper.getLastTimeClickedAds();
        long pressAds = preferenceHelper.getAdPress();
        long currentTime = System.currentTimeMillis();
        if (currentTime < lastTimeClickedAds + pressAds) {
            return true;
        }
        return false;
    }
}
