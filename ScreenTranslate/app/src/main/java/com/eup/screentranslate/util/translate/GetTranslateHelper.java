package com.eup.screentranslate.util.translate;

import androidx.annotation.NonNull;

import com.eup.screentranslate.api.GoogleApi;
import com.eup.screentranslate.api.translate.TranslateApi;
import com.eup.screentranslate.api.translate.TranslateGoogleApi;
import com.eup.screentranslate.model.Translation;
import com.eup.screentranslate.callback.OnTranslateCallBack;
import com.eup.screentranslate.callback.OnTranslateTokenCallback;
import com.eup.screentranslate.util.token.TokenGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GetTranslateHelper {
    private static TranslatorOptions options;
    private static GoogleApi googleApi = null;
    private static TranslateApi translateApi = null;
    private static TranslateGoogleApi translateGoogleApi = null;
    private static Translator translateModel;
    private static int numModel = 0;
    private static String sll;
    private static String tll;

    public static GoogleApi getGoogleApi() {
        if (googleApi == null) {
            googleApi = new Retrofit.Builder()
                    .baseUrl("https://www.google.com/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(GoogleApi.class);
        }
        return googleApi;
    }

    public static TranslateApi getTranslateApi() {
        if (translateApi == null) {
            translateApi = new Retrofit.Builder()
                    .baseUrl("https://translate.google.com/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TranslateApi.class);
        }
        return translateApi;
    }

    public static TranslateGoogleApi getTranslateGoogleApi() {
        if (translateGoogleApi == null) {
            translateGoogleApi = new Retrofit.Builder()
                    .baseUrl("https://translate.googleapis.com/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TranslateGoogleApi.class);
        }

        return translateGoogleApi;
    }

    public static Disposable translate(String sl, String tl, String query, OnTranslateCallBack onTranslateCallback) {
        String body = "";
        try {
            body = "async=translate,sl:" + sl + ",tl:" + tl + ",st:" + URLEncoder.encode(query.replaceAll("\n", " "), "utf-8") + ",id:1541997843172,qc:true,ac:true,_id:tw-async-translate,_pms:s,_fmt:pc";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return getGoogleApi().translate(body)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String regex = "<span id=\"tw-answ-spelling\">(.*?)</span>";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(s);
                        String spelling = "";

                        if (matcher.find()) spelling = matcher.group(1);

                        if (spelling.trim().isEmpty() || spelling.equals(query)) {
                            regex = "<span id=\"tw-answ-target-text\">(.*?)</span>";
                            pattern = Pattern.compile(regex);
                            matcher = pattern.matcher(s);

                            String mean = "";
                            if (matcher.find()) mean = matcher.group(1);

                            regex = "<span id\"tw-answ-source-romanization\">(.*?)</span>";
                            pattern = Pattern.compile(regex);
                            matcher = pattern.matcher(s);

                            String romaji = "";
                            if (matcher.find()) romaji = matcher.group(1);
                            onTranslateCallback.onSuccess(query, mean, romaji, sl, tl, query);
                        } else {
                            onTranslateCallback.onSuccess(spelling, "", null, sl, tl, query);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onTranslateCallback.onFailed(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }

    public static Disposable tranlateWithToken(String from, String to, String query, String hl, OnTranslateTokenCallback onTranslateTokenCallback) {
        return getTranslateApi().getToken().flatMap(new Function<String, ObservableSource<Translation>>() {
            @Override
            public ObservableSource<Translation> apply(String s) throws Exception {
                String tkk = "";
                String token = "";
                Matcher matcher = Pattern.compile("tkk:'(.*?)'").matcher(s);
                if (matcher.find()) {
                    tkk = matcher.group(0);
                    tkk.replaceAll("tkk:'(.*?)'", "$1");
                    TokenGenerator tokenGenerator = new TokenGenerator(tkk);
                    token = tokenGenerator.getTokenKey();
                }
                return getTranslateGoogleApi().translate(hl, from, to, token, query);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Translation>() {
                    @Override
                    public void accept(Translation translation) throws Exception {
                        onTranslateTokenCallback.onSuccess(translation);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onTranslateTokenCallback.onFailed(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }

    public static boolean setLanguage(String from, String to) {
        String sl = from;
        String tl = to;

        if (from.contains("-")) {
            sl = from.substring(0, from.indexOf("-"));
        }
        if (to.contains("-")) {
            tl = to.substring(0, to.indexOf("-"));
        }

        if (sl == sll && tl == tll && translateModel != null) return true;
        if (TranslateLanguage.getAllLanguages().contains(sl) == false || TranslateLanguage.getAllLanguages().contains(tl) == false)
            return false;
        sll = sl;
        tll = tl;
        if (null != translateModel) {
            translateModel.close();
        }
        if (TranslateLanguage.fromLanguageTag(sl) == null) {
            options = new TranslatorOptions.Builder().
                    setSourceLanguage("").
                    setTargetLanguage(TranslateLanguage.fromLanguageTag(tl)).build();
        } else if (TranslateLanguage.fromLanguageTag(tl) == null) {
            options = new TranslatorOptions.Builder().
                    setSourceLanguage(TranslateLanguage.fromLanguageTag(sl)).
                    setTargetLanguage("").build();
        } else if (TranslateLanguage.fromLanguageTag(sl) == null && TranslateLanguage.fromLanguageTag(tl) == null) {
            options = new TranslatorOptions.Builder().
                    setSourceLanguage("").
                    setTargetLanguage("").build();
        } else if (TranslateLanguage.fromLanguageTag(tl) != null && TranslateLanguage.fromLanguageTag(tl) == null) {
            options = new TranslatorOptions.Builder().
                    setSourceLanguage(TranslateLanguage.fromLanguageTag(sl)).
                    setTargetLanguage(TranslateLanguage.fromLanguageTag(tl)).build();
        } else if (options == null) {
            options = new TranslatorOptions.Builder().
                    setSourceLanguage(TranslateLanguage.fromLanguageTag("en")).
                    setTargetLanguage("vi").build();
        }
        translateModel = com.google.mlkit.nl.translate.Translation.getClient(options);
        return true;
    }


    /**
     * val a= .jpg
     * var b= System.CurreTime().jpg
     * a+b
     *
     * @param translateOfflineCallback
     */

    public static void isExistModel(onTranslateOfflineCallback translateOfflineCallback) {
        RemoteModelManager modelManager = RemoteModelManager.getInstance();
        modelManager.getDownloadedModels(TranslateRemoteModel.class).
                addOnSuccessListener(new OnSuccessListener<Set<TranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(@NonNull Set<TranslateRemoteModel> translateRemoteModels) {
                        numModel = 0;
                        for (TranslateRemoteModel m : translateRemoteModels) {
                            if (m.getLanguage() == sll || m.getLanguage() == tll) {
                                numModel += 1;
                            }
                        }
                        if (numModel >= 2) translateOfflineCallback.onFinished(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                translateOfflineCallback.onFinished(false);
            }
        });
    }

    public static void translate(String text, onTranslateOffline translateOffline) {
        translateModel.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(@NonNull String s) {
                translateOffline.onResult(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                translateOffline.onResult(null);
            }
        });
    }

    public static void downloadModel(onDownloadModelCallback onDownloadFinished) {
        if (null!=translateModel) {
            translateModel.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    onDownloadFinished.onDownloadFinished(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    onDownloadFinished.onDownloadFinished(false);
                }
            });
        }
    }

    public interface onTranslateOfflineCallback {
        void onFinished(boolean b);
    }

    public interface onTranslateOffline {
        void onResult(String s);
    }

    public interface onDownloadModelCallback {
        void onDownloadFinished(boolean b);
    }


}
