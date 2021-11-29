package com.eup.screentranslate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("status")
    @Expose
    int status;

    @SerializedName("result")
    @Expose
    Result result;

    @SerializedName("message")
    @Expose
    String mesage;

    @SerializedName("err")
    @Expose
    String err;

    class Result{
        @SerializedName("userId")
        @Expose
        int userId;

        @SerializedName("email")
        @Expose
        String email;

        @SerializedName("tokenId")
        @Expose
        String tokenID;

        @SerializedName("status")
        @Expose
        int status;

        @SerializedName("agreement")
        @Expose
        int agreement;

        @SerializedName("active")
        @Expose
        int active;

        @SerializedName("soc_armorial_id")
        @Expose
        int socArmorialId;

        @SerializedName("soc_rank")
        @Expose
        int socRank;

        @SerializedName("username")
        @Expose
        String username;

        @SerializedName("created_at")
        @Expose
        String createdAt;

        @SerializedName("updated_at")
        @Expose
        String updatedAt;

        @SerializedName("id")
        @Expose
        int id;

        @SerializedName("lastest_update")
        @Expose
        String lastestUpdate;

        @SerializedName("premium_date")
        @Expose
        String premiumData;

        @SerializedName("premium_expired_date")
        @Expose
        String premiumExpiredData;

        @SerializedName("user_introduce")
        @Expose
        String userIntroduce;

        @SerializedName("provider")
        @Expose
        String provider;

        @SerializedName("provider_id")
        @Expose
        String providerId;

        @SerializedName("access_token")
        @Expose
        String accessToken;

        @SerializedName("dict_register")
        @Expose
        int dictRegister;

        @SerializedName("language_id")
        @Expose
        int languageId;

        @SerializedName("premium")
        @Expose
        boolean premium = false;

        @SerializedName("profile")
        @Expose
        Profile profile;

        @SerializedName("lifetime")
        @Expose
        int lifetime;

        @SerializedName("code")
        @Expose
        int code;

        @SerializedName("app_review")
        @Expose
        boolean appReview = false;

        public Result(int userId, String email, String tokenID, int status, int agreement, int active, int socArmorialId, int socRank, String username, String createdAt, String updatedAt, String lastestUpdate, String premiumData, String premiumExpiredData, String userIntroduce, String provider, String providerId, String accessToken, int dictRegister, int languageId, boolean premium, Profile profile, int lifetime, int code, boolean appReview) {
            this.userId = userId;
            this.email = email;
            this.tokenID = tokenID;
            this.status = status;
            this.agreement = agreement;
            this.active = active;
            this.socArmorialId = socArmorialId;
            this.socRank = socRank;
            this.username = username;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.lastestUpdate = lastestUpdate;
            this.premiumData = premiumData;
            this.premiumExpiredData = premiumExpiredData;
            this.userIntroduce = userIntroduce;
            this.provider = provider;
            this.providerId = providerId;
            this.accessToken = accessToken;
            this.dictRegister = dictRegister;
            this.languageId = languageId;
            this.premium = premium;
            this.profile = profile;
            this.lifetime = lifetime;
            this.code = code;
            this.appReview = appReview;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTokenID() {
            return tokenID;
        }

        public void setTokenID(String tokenID) {
            this.tokenID = tokenID;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAgreement() {
            return agreement;
        }

        public void setAgreement(int agreement) {
            this.agreement = agreement;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public int getSocArmorialId() {
            return socArmorialId;
        }

        public void setSocArmorialId(int socArmorialId) {
            this.socArmorialId = socArmorialId;
        }

        public int getSocRank() {
            return socRank;
        }

        public void setSocRank(int socRank) {
            this.socRank = socRank;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLastestUpdate() {
            return lastestUpdate;
        }

        public void setLastestUpdate(String lastestUpdate) {
            this.lastestUpdate = lastestUpdate;
        }

        public String getPremiumData() {
            return premiumData;
        }

        public void setPremiumData(String premiumData) {
            this.premiumData = premiumData;
        }

        public String getPremiumExpiredData() {
            return premiumExpiredData;
        }

        public void setPremiumExpiredData(String premiumExpiredData) {
            this.premiumExpiredData = premiumExpiredData;
        }

        public String getUserIntroduce() {
            return userIntroduce;
        }

        public void setUserIntroduce(String userIntroduce) {
            this.userIntroduce = userIntroduce;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getDictRegister() {
            return dictRegister;
        }

        public void setDictRegister(int dictRegister) {
            this.dictRegister = dictRegister;
        }

        public int getLanguageId() {
            return languageId;
        }

        public void setLanguageId(int languageId) {
            this.languageId = languageId;
        }

        public boolean isPremium() {
            return premium;
        }

        public void setPremium(boolean premium) {
            this.premium = premium;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public int getLifetime() {
            return lifetime;
        }

        public void setLifetime(int lifetime) {
            this.lifetime = lifetime;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public boolean isAppReview() {
            return appReview;
        }

        public void setAppReview(boolean appReview) {
            this.appReview = appReview;
        }
    }

    public class Profile{
        @SerializedName("profile_id")
        @Expose
        int profileId;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("image")
        @Expose
        String image;

        @SerializedName("info")
        @Expose
        int info;

        @SerializedName("job")
        @Expose
        String job;

        @SerializedName("introduction")
        @Expose
        String introduction;

        @SerializedName("translate")
        @Expose
        int translate;

        @SerializedName("card_id")
        @Expose
        String cardId;

        @SerializedName("card_address")
        @Expose
        String cardAddress;

        @SerializedName("card_date")
        @Expose
        String cardDate;

        @SerializedName("guardian")
        @Expose
        String guarDian;

        @SerializedName("guardian_card")
        @Expose
        String guarDianCard;

        @SerializedName("phone")
        @Expose
        String phone;

        @SerializedName("email")
        @Expose
        String email;

        @SerializedName("facebook")
        @Expose
        String facbook;

        @SerializedName("need")
        @Expose
        int need;

        @SerializedName("address")
        @Expose
        String address;

        @SerializedName("country")
        @Expose
        String country;

        @SerializedName("birthday")
        @Expose
        String birthday;

        @SerializedName("sex")
        @Expose
        int sex;

        @SerializedName("level")
        @Expose
        int level;

        public Profile(int profileId, String name, String image, int info, String job, String introduction, int translate, String cardId, String cardAddress, String cardDate, String guarDian, String guarDianCard, String phone, String email, String facbook, int need, String address, String country, String birthday, int sex, int level) {
            this.profileId = profileId;
            this.name = name;
            this.image = image;
            this.info = info;
            this.job = job;
            this.introduction = introduction;
            this.translate = translate;
            this.cardId = cardId;
            this.cardAddress = cardAddress;
            this.cardDate = cardDate;
            this.guarDian = guarDian;
            this.guarDianCard = guarDianCard;
            this.phone = phone;
            this.email = email;
            this.facbook = facbook;
            this.need = need;
            this.address = address;
            this.country = country;
            this.birthday = birthday;
            this.sex = sex;
            this.level = level;
        }

        public int getProfileId() {
            return profileId;
        }

        public void setProfileId(int profileId) {
            this.profileId = profileId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getInfo() {
            return info;
        }

        public void setInfo(int info) {
            this.info = info;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getTranslate() {
            return translate;
        }

        public void setTranslate(int translate) {
            this.translate = translate;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getCardAddress() {
            return cardAddress;
        }

        public void setCardAddress(String cardAddress) {
            this.cardAddress = cardAddress;
        }

        public String getCardDate() {
            return cardDate;
        }

        public void setCardDate(String cardDate) {
            this.cardDate = cardDate;
        }

        public String getGuarDian() {
            return guarDian;
        }

        public void setGuarDian(String guarDian) {
            this.guarDian = guarDian;
        }

        public String getGuarDianCard() {
            return guarDianCard;
        }

        public void setGuarDianCard(String guarDianCard) {
            this.guarDianCard = guarDianCard;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFacbook() {
            return facbook;
        }

        public void setFacbook(String facbook) {
            this.facbook = facbook;
        }

        public int getNeed() {
            return need;
        }

        public void setNeed(int need) {
            this.need = need;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
