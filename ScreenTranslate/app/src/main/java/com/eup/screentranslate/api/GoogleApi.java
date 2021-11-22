package com.eup.screentranslate.api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GoogleApi {
    @POST("async/translate?vet=12ahUKEwiAhIWthc7eAhVNat4KHegJCiwQqDgwAHoECAIQFQ..i&ei=zwTpW8DgBs3U-Qbok6jgAg&yv=3")
    @Headers(value = {"Cookie: NID=146=p-KPB8sQ6nqjr8I56LiEJzjdcsk7Wh91oDwr0jU0rfwOfN4Y_l9T4j_5uaSDg_6tDMSEXmPdhueoxwYM4w6meuHTK1R-Mej8-9Fm4kiEb8kFw8wVPnrgtaefkgNPq3W9ro81wpyImN-QtPVKILiNYq5UN07oTQWarcfgEXHOl0w6PR7uE4Xh14o; 1P_JAR=2018-11-12-04; OGP=-5061451:; DV=AwAhS-7BuJMeYH8oIYu_J3hJpKxjcBY",
            "content-type: application/x-www-form-urlencoded;charset=utf-8",
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0; Waterfox) Gecko/20100101 Firefox/56.2.5"})
    Observable<String> translate(@Body String body);
}
