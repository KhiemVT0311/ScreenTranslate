package com.eup.screentranslate.util.translate;

import android.graphics.Bitmap;
import android.util.Base64;

import com.eup.screentranslate.util.TextAnnotations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import kotlin.collections.AbstractMutableList;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetWordByImageHelper {
    public static String URL_GET_WORD_BY_IMAGE = "https://content-vision.googleapis.com/v1/images:annotate?$.xgafv=1&alt=json&prettyPrint=true&key=AIzaSyAa8yy0GdcGPHdtD083HiGGx_S0vMPScDM&%24.xgafv=1";

    public static Flowable<List<TextAnnotations>> getText(Bitmap bitmap) {
        return Flowable.fromCallable(new Callable<List<TextAnnotations>>() {

            @Override
            public List<TextAnnotations> call() throws Exception {
                String base64 = getStringBase64(bitmap);
                String jsonBody = "{\"requests\": [{\n" +
                        "                \"image\": {\n" +
                        "                    \"content\": \"" + base64 + "\"\n" +
                        "                },\n" +
                        "                \"features\": [{\n" +
                        "                    \"type\": \"TEXT_DETECTION\",\n" +
                        "                    \"maxResults\": 50\n" +
                        "                }]\n" +
                        "            }]}";
                return getTextAnnotations(jsonBody);
            }
        });
    }

    public static String getStringBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT).replaceAll("\\s+", "");
    }


    private static List<TextAnnotations> getTextAnnotations(String jsonBody) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
            Request request = new Request.Builder()
                    .url(URL_GET_WORD_BY_IMAGE)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Referer", "https://content-vision.googleapis.com/static/proxy.html?usegapi=1&jsh=m%3B%2F_%2Fscs%2Fapps-static%2F_%2Fjs%2Fk%3Doz.gapi.vi.S2ZrayFw1_o.O%2Fam%3DwQE%2Fd%3D1%2Frs%3DAGLTcCNciJPc_PoDS84WYnHa1tuOu3o3eg%2Fm%3D__features__")
                    .addHeader("Sec-Fetch-Mode", "cors")
                    .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36")
                    .addHeader("authority", "content-vision.googleapis.com")
                    .addHeader("origin", "https://content-vision.googleapis.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("x-client-data", "CKK1yQEIjrbJAQiktskBCMS2yQEI0LfJAQipncoBCKijygEIzqXKAQjiqMoBCJetygEIza3KAQjyrcoBCMuuygEIyq/KAQjIsMoB")
                    .addHeader("x-clientdetails", "appVersion=5.0%20(Macintosh%3B%20Intel%20Mac%20OS%20X%2010_14_5)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F76.0.3809.100%20Safari%2F537.36&platform=MacIntel&userAgent=Mozilla%2F5.0%20(Macintosh%3B%20Intel%20Mac%20OS%20X%2010_14_5)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F76.0.3809.100%20Safari%2F537.36")
                    .addHeader("X-Goog-Encode-Response-If-Executable", "base64")
                    .addHeader("X-JavaScript-User-Agent", "apix/3.0.0 google-api-javascript-client/1.1.0")
                    .addHeader("x-origin", "https://explorer.apis.google.com")
                    .addHeader("x-referer", "https://explorer.apis.google.com")
                    .addHeader("Accept", "*/*")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            String result = response.body().string();

            JSONObject jsonObject = new JSONObject(result);
            TypeToken<ArrayList<TextAnnotations>> list = new TypeToken<ArrayList<TextAnnotations>>() {
            };
            JSONArray jsonObjectTextAnnotations = jsonObject.getJSONArray("reponses").getJSONObject(0).getJSONArray("textAnnotations");
            ArrayList<TextAnnotations> arrTextAnnotations = new Gson().fromJson(jsonObjectTextAnnotations.toString(), list.getType());
            arrTextAnnotations.remove(0);
            return arrTextAnnotations;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return new ArrayList<TextAnnotations>();
    }
}
