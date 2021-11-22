package com.eup.screentranslate.util.speechrecognizer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.core.content.ContextCompat;

import com.eup.screentranslate.R;
import com.eup.screentranslate.callback.OnRecognitionCallback;

import java.util.ArrayList;

public class SpeechRecognizerHelper {//trình trợ giúp nhận dạng giọng nói
    public static final String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public SpeechRecognizer recognizer;
    private Intent mSpeechRecognizerIntent;
    private long timeout = 2000l;
    private boolean mIsListening = false;
    private Context mContext;

    public SpeechRecognizerHelper(Context context, OnRecognitionCallback callback ) {
        this.mContext = context;
        recognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                callback.onStartSpeech();
            }

            @Override
            public void onRmsChanged(float rmsdb) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                callback.onEndOfSpeech();
                MediaPlayer mp = MediaPlayer.create(mContext, R.raw.speak_finish);
                if (mp != null && mp.isPlaying()){
                    mp.stop();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    mp.start();
                }
            }

            @Override
            public void onError(int error) {
                mIsListening = false;
                if (callback == null) return;
                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        callback.toastMessage(context.getString(R.string.error_audio));
                        return;
                    case SpeechRecognizer.ERROR_CLIENT:
                        callback.toastMessage(context.getString(R.string.error_client));
                        return;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        callback.toastMessage(context.getString(R.string.error_insufficient_permissions));
                        return;
                    case SpeechRecognizer.ERROR_NETWORK:
                        callback.toastMessage(context.getString(R.string.no_internet));
                        return;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        callback.toastMessage(context.getString(R.string.network_timeout));
                        return;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        callback.toastMessage(context.getString(R.string.no_text));
                        return;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        callback.toastMessage(context.getString(R.string.system_busy));
                        return;
                    case SpeechRecognizer.ERROR_SERVER:
                        callback.toastMessage(context.getString(R.string.system_busy));
                        return;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        callback.toastMessage(context.getString(R.string.no_text));
                        return;
                }
            }

            @Override
            public void onResults(Bundle results) {
                if (results != null && callback != null){
                    ArrayList<String> texts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    callback.onResults(texts);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    public void startListen(String landCode){
        MediaPlayer mp = MediaPlayer.create(mContext,R.raw.speak_start);
        if (mp != null){
            if (mp.isPlaying()) {
                mp.stop(); mp.reset();
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mp = null;
                    startListening(landCode);
                }
            });
            mp.start();
        }
    }

    public void startListening(String landCode){
        if (!mIsListening){
            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,mContext.getPackageName());
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,landCode);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,landCode);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,landCode);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,landCode);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,timeout);

            mIsListening = true;
            recognizer.startListening(mSpeechRecognizerIntent);
        }
    }

    public void stopListening(){
        if (recognizer != null && mIsListening){
            mIsListening = false;
            recognizer.stopListening();
        }
    }

    public void stop(){
        if (mIsListening && recognizer != null){
            recognizer.stopListening();
            recognizer.cancel();
            recognizer.destroy();
            recognizer = null;
        }
    }

    public void destroy(){
        mIsListening = false;
        if (recognizer != null){
            recognizer.stopListening();
            recognizer.cancel();
            recognizer.destroy();
            recognizer = null;
        }
    }

    public boolean ismIsListening() {
        return mIsListening;
    }

    public void setmIsListening(boolean mIsListening) {
        this.mIsListening = mIsListening;
    }

    public static boolean checkPermissions(Context context){
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED);
        }

        return true;
    }
}
