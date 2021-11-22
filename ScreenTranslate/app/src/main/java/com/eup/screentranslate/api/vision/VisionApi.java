package com.eup.screentranslate.api.vision;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VisionApi {
    @POST(value = "v1/images:annotate?$.xgafv=1&alt=json&prettyPrint=true&key=AIzaSyAa8yy0GdcGPHdtD083HiGGx_S0vMPScDM&%24.xgafv=1")
    @Headers({"Content-Type: application/json",
            "Referer: https://content-vision.googleapis.com/static/proxy.html?usegapi=1&jsh=m%3B%2F_%2Fscs%2Fapps-static%2F_%2Fjs%2Fk%3Doz.gapi.vi.S2ZrayFw1_o.O%2Fam%3DwQE%2Fd%3D1%2Frs%3DAGLTcCNciJPc_PoDS84WYnHa1tuOu3o3eg%2Fm%3D__features__",
            "Sec-Fetch-Mode: cors",
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36",
            "authority: content-vision.googleapis.com",
            "origin: https://content-vision.googleapis.com",
            "sec-fetch-site: same-origin",
            "x-client-data: CKK1yQEIjrbJAQiktskBCMS2yQEI0LfJAQipncoBCKijygEIzqXKAQjiqMoBCJetygEIza3KAQjyrcoBCMuuygEIyq/KAQjIsMoB",
            "x-clientdetails: appVersion=5.0%20(Macintosh%3B%20Intel%20Mac%20OS%20X%2010_14_5)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F76.0.3809.100%20Safari%2F537.36&platform=MacIntel&userAgent=Mozilla%2F5.0%20(Macintosh%3B%20Intel%20Mac%20OS%20X%2010_14_5)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F76.0.3809.100%20Safari%2F537.36",
            "X-Goog-Encode-Response-If-Executable: base64",
            "X-JavaScript-User-Agent: apix/3.0.0 google-api-javascript-client/1.1.0",
            "x-origin: https://explorer.apis.google.com",
            "x-referer: https://explorer.apis.google.com",
            "Accept: */*"})

    public Flowable<String> getVision(@Body RequestBody body);
}
