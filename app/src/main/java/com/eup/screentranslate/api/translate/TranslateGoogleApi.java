package com.eup.screentranslate.api.translate;

import com.eup.screentranslate.model.Translation;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TranslateGoogleApi {
    @GET("translate_a/single?client=gtx&dt=t&dt=bd&dj=1&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=at")
    @Headers({"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36",
            "charset: UTF-8"})
    Observable<Translation> translate(@Query("hl") String hl, @Query("sl") String sl, @Query("tl") String tl, @Query("tk") String token, @Query("q") String query);
}
