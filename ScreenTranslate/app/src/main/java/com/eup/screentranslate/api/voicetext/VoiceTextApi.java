package com.eup.screentranslate.api.voicetext;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VoiceTextApi {
    @POST("tomcat/servlet/vt")
    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36")
    Observable<String> getNameAudio(@Body RequestBody body);

}
