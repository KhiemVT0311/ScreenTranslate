package com.eup.screentranslate.api.translate;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface TranslateApi {
    @GET("/")
    Observable<String> getToken();
}
