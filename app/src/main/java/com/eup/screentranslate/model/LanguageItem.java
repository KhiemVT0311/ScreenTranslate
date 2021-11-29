package com.eup.screentranslate.model;

import android.net.Uri;

import androidx.annotation.DrawableRes;

public class LanguageItem {
    public String code;
    public String name;
    public String subCode;
    public String country;
    public String emojiFlag;
    @DrawableRes
    public int flag;

    public LanguageItem(String code, String name, String subCode, String country, String emojiFlag, int flag) {
        this.code = code;
        this.name = name;
        this.subCode = subCode;
        this.country = country;
        this.emojiFlag = emojiFlag;
        this.flag = flag;
    }


}
