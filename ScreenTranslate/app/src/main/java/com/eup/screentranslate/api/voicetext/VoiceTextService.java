package com.eup.screentranslate.api.voicetext;

import com.eup.screentranslate.util.constants.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VoiceTextService {
    public static VoiceTextApi voiceTextApi = null;
    public static VoiceTextApi getVoiceTextApi(){
        if (voiceTextApi == null){
            voiceTextApi = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_SPEAK_TEXT)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(VoiceTextApi.class);
        }
        return voiceTextApi;
    }
}
