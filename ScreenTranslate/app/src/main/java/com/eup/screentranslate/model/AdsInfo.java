package com.eup.screentranslate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsInfo {
    @SerializedName("adsProb")
    @Expose
    private AdsProb adsProb;
    @SerializedName("ga")
    @Expose
    private Ga ga;
    @SerializedName("adsid")
    @Expose
    private Adsid adsid;

    public AdsProb getAdsProb() {
        return adsProb;
    }

    public void setAdsProb(AdsProb adsProb) {
        this.adsProb = adsProb;
    }

    public Ga getGa() {
        return ga;
    }

    public void setGa(Ga ga) {
        this.ga = ga;
    }

    public Adsid getAdsid() {
        return adsid;
    }

    public void setAdsid(Adsid adsid) {
        this.adsid = adsid;
    }

    public class AdsProb {

        @SerializedName("banner")
        @Expose
        private Integer banner;
        @SerializedName("native")
        @Expose
        private Integer _native;
        @SerializedName("interstitial")
        @Expose
        private Integer interstitial;
        @SerializedName("adpress")
        @Expose
        private long adpress;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("intervalAdsInter")
        @Expose
        private long intervalAdsInter;

        public Integer getBanner() {
            return banner;
        }

        public void setBanner(Integer banner) {
            this.banner = banner;
        }

        public Integer getNative() {
            return _native;
        }

        public void setNative(Integer _native) {
            this._native = _native;
        }

        public Integer getInterstitial() {
            return interstitial;
        }

        public void setInterstitial(Integer interstitial) {
            this.interstitial = interstitial;
        }

        public long getAdpress() {
            return adpress;
        }

        public void setAdpress(long adpress) {
            this.adpress = adpress;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public long getIntervalAdsInter() {
            return intervalAdsInter;
        }

        public void setIntervalAdsInter(long intervalAdsInter) {
            this.intervalAdsInter = intervalAdsInter;
        }
    }

    public class Adsid {

        @SerializedName("android")
        @Expose
        private Android android;

        public Android getAndroid() {
            return android;
        }

        public void setAndroid(Android android) {
            this.android = android;
        }

    }

    public class Android {

        @SerializedName("banner")
        @Expose
        private String banner;
        @SerializedName("interstitial")
        @Expose
        private String interstitial;
        @SerializedName("reward")
        @Expose
        private String reward;
        @SerializedName("native")
        @Expose
        private String _native;

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getInterstitial() {
            return interstitial;
        }

        public void setInterstitial(String interstitial) {
            this.interstitial = interstitial;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getNative() {
            return _native;
        }

        public void setNative(String _native) {
            this._native = _native;
        }

    }

    public class Ga {

        @SerializedName("android")
        @Expose
        private String android;
        @SerializedName("ios")
        @Expose
        private String ios;
        @SerializedName("wp")
        @Expose
        private String wp;

        public String getAndroid() {
            return android;
        }

        public void setAndroid(String android) {
            this.android = android;
        }

        public String getIos() {
            return ios;
        }

        public void setIos(String ios) {
            this.ios = ios;
        }

        public String getWp() {
            return wp;
        }

        public void setWp(String wp) {
            this.wp = wp;
        }

    }

}
