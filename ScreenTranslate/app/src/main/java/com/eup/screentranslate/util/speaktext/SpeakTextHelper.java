package com.eup.screentranslate.util.speaktext;

import android.app.Application;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import com.eup.screentranslate.R;
import com.eup.screentranslate.api.voicetext.VoiceTextService;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.util.network.NetworkHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SpeakTextHelper implements TextToSpeech.OnInitListener {
    private MediaPlayer mediaPlayer;
    private TextToSpeech tts;
    private HashMap<String, String> mapNameUrl;
    private Application application;
    private boolean isSupportTTS = true;

    public SpeakTextHelper(Application application) {
        this.application = application;
        mediaPlayer = new MediaPlayer();
        tts = new TextToSpeech(application.getApplicationContext(), this);
        mapNameUrl = new HashMap<>();
    }

    public void speakText(String text, String languageCode, OnSpeakCallback onSpeakCallback){
        if (text == null || text.isEmpty()) return;
        if (LanguageHepler.isJapanese(text) && NetworkHelper.isNetworkConnected(application.getApplicationContext())){
            if (mapNameUrl != null && mapNameUrl.containsKey(text)){
                playAudio(mapNameUrl.get(text), text, languageCode, onSpeakCallback);
            } else {
                RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"),
                        "text=" + text + "&talkID=308&volume=100&speed=80&pitch=96&dict=3");
                VoiceTextService.getVoiceTextApi()
                        .getNameAudio(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if (s.contains("=")){
                                    String nameAudio = s.substring(s.indexOf('=') + 1);
                                    String audioUrl = Constants.BASE_URL_SPEAK_TEXT + "ASLCLCLVVS/JMEJSYGDCHMSMHSRKPJL/" + nameAudio;
                                    if (mapNameUrl != null){
                                        mapNameUrl.put(text, audioUrl);
                                    }
                                    playAudio(audioUrl, text, languageCode, onSpeakCallback);
                                } else {
                                    textToSpeech(text, languageCode, onSpeakCallback);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                textToSpeech(text, languageCode, onSpeakCallback);
                            }
                        });
            }
        } else {
            textToSpeech(text, languageCode, onSpeakCallback);
        }
    }

    public void playAudio(String audioUrl, String text, String languageCode, OnSpeakCallback onSpeakCallback){
        if (audioUrl == null || audioUrl.isEmpty()) return;
        if (tts.isSpeaking()) tts.stop();
        if (mediaPlayer.isPlaying()) mediaPlayer.stop();
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(it -> it.start());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (onSpeakCallback != null) onSpeakCallback.success();
                }
            });
        } catch (IOException e){
            e.printStackTrace();
            textToSpeech(text, languageCode, onSpeakCallback);
        }
    }

    //convert text to speech
    public void textToSpeech(String text, String languageCode, OnSpeakCallback onSpeakCallback){
        if (isSupportTTS){
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            if (tts.isSpeaking()) tts.stop();

            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {

                }

                @Override
                public void onDone(String utteranceId) {
                    if (onSpeakCallback != null) onSpeakCallback.success();
                }

                @Override
                public void onError(String utteranceId) {
                    if (onSpeakCallback != null) onSpeakCallback.failure();
                }
            });

            if (languageCode.equals("ja") || LanguageHepler.isJapanese(text)){
                tts.setLanguage(Locale.JAPANESE);
            } else {
                tts.setLanguage(new Locale(languageCode));
            }
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
        } else {
            useGoogleSpeaker(text, languageCode, onSpeakCallback);
        }
    }

    private void useGoogleSpeaker(String text, String langCode, OnSpeakCallback onSpeakCallback) {
        if (NetworkHelper.isNetworkConnected(application.getApplicationContext())) {
            String audioUrl = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&q=" + text + "&tl=" + langCode;
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(it -> it.start());
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (onSpeakCallback != null) onSpeakCallback.success();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(application.getApplicationContext(), R.string.no_support_text_to_speech, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            isSupportTTS = true;
            int result = tts.setLanguage(Locale.JAPANESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(application.getApplicationContext(), R.string.no_support_lang_code, Toast.LENGTH_SHORT).show();
                isSupportTTS = false;
            }
        } else {
            isSupportTTS = false;
        }
    }
}
