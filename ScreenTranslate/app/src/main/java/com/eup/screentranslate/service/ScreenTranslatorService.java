package com.eup.screentranslate.service;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.NotificationCompat;

import com.eup.screentranslate.R;
import com.eup.screentranslate.callback.OnSelectedCountryCallback;
import com.eup.screentranslate.callback.OnTranslateCallBack;
import com.eup.screentranslate.callback.OnTranslateTokenCallback;
import com.eup.screentranslate.model.Translation;
import com.eup.screentranslate.ui.screen.splash.SplashActivity;
import com.eup.screentranslate.util.Extentions;
import com.eup.screentranslate.util.translate.GetWordByImageHelper;
import com.eup.screentranslate.util.TextAnnotations;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.dialog.DialogHelper;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.util.network.NetworkHelper;
import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.eup.screentranslate.util.speaktext.OnSpeakCallback;
import com.eup.screentranslate.util.speaktext.SpeakTextHelper;
import com.eup.screentranslate.util.translate.GetTranslateHelper;
import com.eup.screentranslate.view.FuriganaView;
import com.eup.screentranslate.view.ScreenOverlayView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ScreenTranslatorService extends Service {
    public static final String EXTRA_RESULT_CODE = "resultCode";
    public static final String EXTRA_RESULT_INTENT = "resultIntent";
    private ScreenOverlayView screenView;
    private WindowManager mWindowManager;
    private ImageButton iconSearchView;
    private View resultView;
    private AppCompatEditText editText;
    private FuriganaView fvContent;
    private FuriganaView fvMean;
    private TextView tvSrc;
    private TextView tvDes;
    private TextView btnSpeak;
    private TextView btnSpeakOutput;
    private ProgressBar pbMean;
    private ImageButton btnSwap;
    private ImageButton btnClear;
    private TextView btnTranslate;

    private Handler handler;
    private SpeakTextHelper speakTextHelper;
    int currentPosSourceLang = -1;
    private GetTranslateHelper translateHelper;
    private MediaProjectionManager mMediaProjectionManager;
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private int mScreenDensity = 0;
    private int mResultCode = 0;
    private int mStatusBarHeight = 0;
    private Intent mresulData;
    int currentPosDestLang = -1;
    private Float iconCloseSize;
    private PreferenceHelper preferenceHelper;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private TextRecognizer recognizer = TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());

    private Notification createNotification(Context context) {
        String channeId = context.getString(R.string.default_screen_trans_channel_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Notification ScreenTranslate";
            NotificationChannel mChannel = new NotificationChannel(channeId, channelName, NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("Screen translate");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channeId)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("ScreenTranslate")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(context.getString(R.string.message_screen_trans_enable))
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        Intent notifyIntent = new Intent(context, SplashActivity.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context,
                0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);
        return builder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(2, createNotification(this));
        iconCloseSize = Extentions.convertDpToPixel(getApplication().getApplicationContext(), 60F);
        preferenceHelper = new PreferenceHelper(getApplication().getApplicationContext(), Constants.PREFERENCE_SCREEN_TRANS);

//        ViewGroup wrapper = new FrameLayout(this) {
//            @Override
//            public boolean dispatchKeyEvent(KeyEvent event) {
//                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                    if (editText.isFocused() == true) {
//                        editText.clearFocus();
//                        if (editText != null) {
//                            Extentions.hideKeyboard(getApplicationContext(), editText);
//                        }
//                        return true;
//                    } else if (resultView.getVisibility() != GONE) {
//                        resultView.setVisibility(GONE);
//                        return true;
//                    }
//                }
//                return super.dispatchKeyEvent(event);
//            }
//        };
//        resultView = LayoutInflater.from(this).inflate(R.layout.layput_result_screen_trans, wrapper);

    }

    long time = 0;

    private void createView() {
        if (mWindowManager != null) {
            return;
        }
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        createVirtualEnvironment();
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    PixelFormat.TRANSLUCENT);
        }

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = windowHeight / 2;
        try {
            screenView = new ScreenOverlayView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams flags = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        PixelFormat.TRANSLUCENT);
                flags.gravity = Gravity.TOP | Gravity.LEFT;
                flags.x = 0;
                flags.y = 0;
                mWindowManager.addView(screenView, flags);
            } else {
                WindowManager.LayoutParams flags = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        PixelFormat.TRANSLUCENT);
                flags.gravity = Gravity.TOP | Gravity.LEFT;
                flags.x = 0;
                flags.y = 0;
                mWindowManager.addView(screenView, flags);
            }

            iconSearchView = (ImageButton) LayoutInflater.from(this).inflate(R.layout.icon_screen_translator, null);

            mWindowManager.addView(iconSearchView, params);
            if (preferenceHelper.isShowTipScreenTrans())
                Toast.makeText(getApplication().getApplicationContext(), R.string.message_tip_click_screen_trans, Toast.LENGTH_SHORT).show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        screenView.addCloseIcon(windowWidth / 2 - iconCloseSize / 2, windowHeight - iconCloseSize - mStatusBarHeight - 50, iconCloseSize);
        iconSearchView.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction = 0;
            private int initialX = 0;
            private int initialY = 0;
            private float initialTouchX = 0f;
            private float initialTouchY = 0f;


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        time = System.currentTimeMillis();
                        initialX = params.x;
                        initialY = params.y;

                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();

                        if (resultView != null) {
                            if (resultView.getVisibility() != View.GONE) {
                                resultView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        resultView.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                        lastAction = motionEvent.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:

                        if (System.currentTimeMillis() - time < 200) {
                            field = !field;
                            setIsActiveTrans(field);
                        }

                        if (lastAction != MotionEvent.ACTION_DOWN) {
                            if (screenView.iconClose.getVisibility() == View.VISIBLE) {
                                if (params.x >= windowWidth / 2 - iconCloseSize && params.x <= windowWidth / 2 + iconCloseSize &&
                                        params.y >= windowHeight - 2 * iconCloseSize - mStatusBarHeight - 50 && params.y <= windowHeight - mStatusBarHeight - 50) {
                                    stopForeground(true);
                                    stopSelf();
                                } else {
                                    screenView.iconClose.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            screenView.iconClose.setVisibility(View.GONE);
                                        }
                                    });
                                    screenView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            screenView.clearRect();
                                        }
                                    });
                                }
                            } else if (isActiveTrans()) {
                                startCapture();
                                setIsActiveTrans(field);
                            } else {
                                screenView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        screenView.clearRect();
                                    }
                                });
                            }
                        } else {
                            screenView.post(new Runnable() {
                                @Override
                                public void run() {
                                    screenView.clearRect();
                                }
                            });
                            field = !field;
                            setIsActiveTrans(field);
                        }
                        lastAction = motionEvent.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int x = Math.round(initialX + (motionEvent.getRawX() - initialTouchX));
                        int y = Math.round(initialY + (motionEvent.getRawY() - initialTouchY));
                        if (x < 0 || y < 0 || x > windowWidth || y > windowHeight || initialX == x && initialY == y) {
                            return true;
                        }
                        //Update the layout with new X & Y coordinate

                        params.x = x;
                        params.y = y;
                        if (isActiveTrans()) {
                            screenView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (initialY < params.y) {
                                        if (initialX < params.x) {
                                            screenView.setRect(initialX, initialY, params.x, params.y);
                                        } else {
                                            screenView.setRect(params.x, initialY, initialX, params.y);
                                        }
                                    } else {
                                        if (initialX < params.x) {
                                            screenView.setRect(initialX, params.y, params.x, initialY);
                                        } else {
                                            screenView.setRect(params.x, params.y, initialX, initialY);
                                        }
                                    }
                                }
                            });
                        } else if (screenView.iconClose.getVisibility() != View.VISIBLE) {
                            screenView.iconClose.post(new Runnable() {
                                @Override
                                public void run() {
                                    screenView.iconClose.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        mWindowManager.updateViewLayout(iconSearchView, params);
                        lastAction = motionEvent.getAction();
                        return true;
                }
                return false;
            }

        });
    }


    private boolean field = false;

    private void setIsActiveTrans(boolean value) {
        iconSearchView.post(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    iconSearchView.setImageResource(R.drawable.ic_screnn_trans_active);
                } else {
                    iconSearchView.setImageResource(R.drawable.ic_screen_translator);
                }
            }

        });

        this.field = value;
        if (preferenceHelper.isShowTipScreenTrans()) {
            Toast.makeText(getApplication().getApplicationContext(), R.string.message_tip_move_screen_trans, Toast.LENGTH_SHORT).show();
            preferenceHelper.setIsBooleanScreenTran(false);
        }
    }

    private boolean isActiveTrans() {
        return field;
    }

    public void initView(int y) {
        if (resultView == null) {
            ViewGroup wrapper = new FrameLayout(this) {
                @Override
                public boolean dispatchKeyEvent(KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        if (editText.isFocused() == true) {
                            editText.clearFocus();
                            if (editText != null) {
                                Extentions.hideKeyboard(getApplicationContext(), editText);
                            }
                            return true;
                        } else if (resultView.getVisibility() != GONE) {
                            resultView.setVisibility(GONE);
                            return true;
                        }
                    }
                    return super.dispatchKeyEvent(event);
                }
            };
            resultView = LayoutInflater.from(this).inflate(R.layout.layput_result_screen_trans, wrapper);

            speakTextHelper = new SpeakTextHelper((Application) getApplicationContext());
            fvContent = resultView.findViewById(R.id.fv_content);
            fvMean = resultView.findViewById(R.id.fv_mean);
            editText = resultView.findViewById(R.id.inputEditText);
            tvSrc = resultView.findViewById(R.id.tvSrc);
            tvDes = resultView.findViewById(R.id.tvDes);
            btnSpeak = resultView.findViewById(R.id.btn_speak);
            btnSpeakOutput = resultView.findViewById(R.id.btn_speak_mean);
            pbMean = resultView.findViewById(R.id.pb_mean);
            btnSwap = resultView.findViewById(R.id.btnSwap);
            btnTranslate = resultView.findViewById(R.id.tv_translate);

            SpannableString trans = new SpannableString(getApplicationContext().getString(R.string.translate));
            trans.setSpan(new UnderlineSpan(), 0, trans.length(), 0);
            btnTranslate.setText(trans);
            btnTranslate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = editText.getText().toString();
                    if (null != text && !text.isEmpty()) {
                        if (editText.isFocusable() == true) {
                            editText.clearFocus();
                            if (editText != null) {
                                Extentions.hideKeyboard(getApplicationContext(), editText);
                            }
                        }
                        translate(text);
                    }
                }
            });

            btnSwap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swap();
                }
            });
            TextView textDetail = resultView.findViewById(R.id.tv_detail);
            SpannableString content = new SpannableString(getApplicationContext().getString(R.string.detail));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textDetail.setText(content);
            tvSrc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUpChooseLanguage(true, tvSrc, currentPosSourceLang);
                }
            });
            tvDes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUpChooseLanguage(false, tvDes, currentPosDestLang);
                }
            });
            btnClear = resultView.findViewById(R.id.btn_clear);
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setText("");
                    editText.setVisibility(View.VISIBLE);
                    btnTranslate.setVisibility(View.VISIBLE);
                    editText.requestFocusFromTouch();
                    fvContent.setVisibility(View.GONE);
                    btnClear.setVisibility(View.GONE);
                }
            });

            fvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setVisibility(View.VISIBLE);
                    btnTranslate.setVisibility(View.VISIBLE);
                    fvContent.setVisibility(View.GONE);
                    editText.requestFocusFromTouch();
                    Extentions.showKeybroad(getApplicationContext());
                    int pos = editText.getText().toString().length();
                    if (pos > 0)
                        editText.setSelection(pos);
                }
            });


            if (null != editText) {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        btnClear.setVisibility(View.GONE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            } else {
                btnClear.setVisibility(View.VISIBLE);
            }

            resultView.findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fvMean.getOrgText().trim();
                    if (null != fvMean) {
                        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("text", fvMean.getOrgText());
                        manager.setPrimaryClip(clipData);
                        Toast.makeText(getApplicationContext(), R.string.copy_done, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnSpeak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = Objects.requireNonNull(editText.getText()).toString();
                    if (speakTextHelper != null && text.isEmpty()) {
                        speakTextHelper.speakText(text
                                , LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom())
                                , onSpeakSourceCallback);
                    } else {
                        speakTextHelper = new SpeakTextHelper((Application) getApplicationContext());
                        speakTextHelper.speakText(text,
                                LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom()),
                                onSpeakSourceCallback);
                    }
                }
            });

            btnSpeakOutput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = fvMean.getOrgText();
                    if (speakTextHelper != null && text.isEmpty()) {
                        speakTextHelper.speakText(text
                                , LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo())
                                , onSpeakDestCallback);
                    } else {
                        speakTextHelper = new SpeakTextHelper((Application) getApplicationContext());
                        speakTextHelper.speakText(text
                                , LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo())
                                , onSpeakDestCallback);
                    }
                }
            });

            resultView.findViewById(R.id.btnClose).setOnClickListener(view -> {
                if (editText.isFocused() == true) {

                    editText.clearFocus();
                    if (editText != null)
                        Extentions.hideKeyboard(getApplicationContext(), editText);
                }
                if (resultView.getVisibility() != View.GONE) {
                    resultView.setVisibility(View.GONE);
                }
                if (isActiveTrans()) {
                    setIsActiveTrans(false);
                }
            });
            WindowManager.LayoutParams params;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        windowWidth * 4 / 5,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        PixelFormat.TRANSLUCENT);
            } else {
                params = new WindowManager.LayoutParams(
                        windowWidth * 4 / 5,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        PixelFormat.TRANSLUCENT);
            }
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = (int) (windowWidth / 10 + Extentions.convertDpToPixel(getApplicationContext(), 12F));
            if (y > windowHeight * 2 / 3)
                params.y = windowHeight * 2 / 3;
            else
                params.y = y;
            mWindowManager.addView(resultView, params);
            setUpLanguage();
        } else {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) resultView.getLayoutParams();
            if (y > windowHeight * 2 / 3)
                params.y = windowHeight * 2 / 3;
            else
                params.y = y;
            if (null != mWindowManager) {
                mWindowManager.updateViewLayout(resultView, params);
            }
            if (resultView.getVisibility() != View.VISIBLE)
                resultView.setVisibility(View.VISIBLE);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createVirtualEnvironment() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        int resStatusBarID = getApplication().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resStatusBarID > 0) {
            mStatusBarHeight = getApplication().getResources().getDimensionPixelSize(resStatusBarID);
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        windowWidth = outMetrics.widthPixels;
        windowHeight = outMetrics.heightPixels;
        mScreenDensity = outMetrics.densityDpi;
        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 2);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        tearDownMediaProjection();
        mDisposable.dispose();
        try {
            if (screenView != null) mWindowManager.removeView(screenView);
            if (iconSearchView != null) mWindowManager.removeView(iconSearchView);
            if (resultView != null) mWindowManager.removeView(resultView);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() == null) {
            createView();
            mResultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
            mresulData = intent.getParcelableExtra(EXTRA_RESULT_INTENT);
            startVirtual();
        }
        return START_REDELIVER_INTENT;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setUpMediaProjection() {
        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mresulData);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(
                "screen-mirror",
                windowWidth,
                windowHeight,
                mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(),
                null,
                null);
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startCapture() {
        if (screenView != null) {
            screenView.showPb();
        }
        Image image = mImageReader.acquireLatestImage();
        if (null != mImageReader && null != image) {

            int width = image.getWidth();
            int height = image.getHeight();
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);

            if (screenView != null) {
                int cut_width = (int) screenView.getRectRight() - screenView.getRectLeft() - 2;
                int cut_height = (int) (screenView.getRectBottom() - screenView.getRectTop() - 2);
                int x1 = (int) (screenView.getRectLeft() + 1);
                int y1 = (int) screenView.getRectTop() + mStatusBarHeight + 1;
                if (cut_width > 0 && cut_height > 0) {
                    bitmap = Bitmap.createBitmap(bitmap, x1, y1, cut_width, cut_height);
                } else {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
                }
            } else {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            }
            image.close();
            if (bitmap != null) {
                try {
                    File fileImage = new File(getExternalFilesDir("images"), "screenshot.png");
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    getDataBase(fileImage, (int) screenView.getRectTop());
                    MediaScannerConnection.scanFile(this,
                            new String[]{fileImage.getAbsolutePath()},
                            new String[]{"image/png"},
                            null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getDataBase(File file, int y) {
        if (mDisposable == null)
            mDisposable = new CompositeDisposable();
        else
            mDisposable.clear();
        mDisposable.add(
                new Compressor(getApplication().getApplicationContext())
                        .compressToBitmapAsFlowable(file)
                        .flatMap(new Function<Bitmap, Flowable<List<TextAnnotations>>>() {
                            @Override
                            public Flowable<List<TextAnnotations>> apply(@NonNull Bitmap bitmap) throws Exception {
                                return GetWordByImageHelper.getText(bitmap);
                            }
                        }).map(new Function<List<TextAnnotations>, String>() {

                    @Override
                    public String apply(@NonNull List<TextAnnotations> textAnnotations) throws Exception {
                        String text = "";
                        for (int i = 0; i < textAnnotations.size(); i++) {
                            if (null != textAnnotations.get(i).description || !textAnnotations.get(i).description.isEmpty()) {
                                text += " " + textAnnotations.get(i).description;
                            }
                        }
                        return text;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if (s.isEmpty()) {
                                    getTextOffLine(file, y);
                                } else {
                                    initView(y);
                                    fvContent.setVisibility(View.VISIBLE);
                                    editText.setVisibility(View.GONE);

                                    fvContent.setText(s);
                                    screenView.clearRect();
                                    translate(s);
                                    editText.setText(s);
                                }
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            getTextOffLine(file, y);
                        })
        );
    }


    private void getTextOffLine(File file, int y) {
        try {
            InputImage image = InputImage.fromFilePath(this, Uri.fromFile(file));
            recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(@NonNull Text text) {
                    String t = text.getText().toString();
                    if (t.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.no_result, Toast.LENGTH_SHORT).show();
                        screenView.clearRect();
                    } else {
                        initView(y);
                        if (fvContent != null)
                            fvContent.setVisibility(View.VISIBLE);
                        if (editText != null)
                            editText.setVisibility(View.GONE);
                        fvContent.setText(t);
                        screenView.clearRect();
                        translate(t);
                        editText.setText(t);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.no_result, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //swap
    private void rotateSwap() {
        RotateAnimation rotateAnimation = new RotateAnimation(0f,
                180f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        btnSwap.startAnimation(rotateAnimation);
    }

    private void fadeLeft() {
        Animation outRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_slide_out_to_left);
        Animation inLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_slide_in_from_right);

        outRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvDes.startAnimation(inLeft);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tvSrc.startAnimation(outRight);
    }


    // swap language
    private void swap() {
        rotateSwap();
        fadeLeft();
        fadeRight();

        int positionTo = preferenceHelper.getPositionTranslateFrom();
        int positionFrom = preferenceHelper.getPositionTranslateTo();

        preferenceHelper.setPositionTranslateFrom(positionFrom);
        preferenceHelper.setPositionTranslateTo(positionTo);
        setUpLanguage();

        String mean = fvMean.getOrgText();
        if (null == mean || mean.isEmpty()) {
            translate(editText.getText().toString());
        } else {
            translate(mean);
        }
    }

    private void setUpLanguage() {
        String src = LanguageHepler.getNameCountryByPosition(preferenceHelper.getPositionTranslateFrom());
        tvSrc.setText(src);
        btnSpeak.setText(src);

        String des = LanguageHepler.getNameCountryByPosition(preferenceHelper.getPositionTranslateTo());
        tvDes.setText(des);
        btnSpeakOutput.setText(des);
    }

    private void fadeRight() {
        Animation outRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_slide_out_to_right);
        Animation inLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_slide_in_from_left);

        outRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvSrc.startAnimation(inLeft);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    // popup chossw language
    private void showPopUpChooseLanguage(boolean isSoure, View anchor, int positionChoose) {
        DialogHelper.showListPopupWindow(getApplicationContext(), anchor, isSoure, onSelectedSourceCountryCallback, onSelectedDestCountryCallback);
    }

    private OnSelectedCountryCallback onSelectedSourceCountryCallback = new OnSelectedCountryCallback() {
        @Override
        public void onSelected(int position) {
            preferenceHelper.setPositionTranslateFrom(position);
            tvSrc.setText(LanguageHepler.LIST_LANGUAGE.get(position).name + " " + LanguageHepler.getEmojiFlag(position));
            translate(editText.getText().toString().trim());
            currentPosSourceLang = position;
        }
    };

    private OnSelectedCountryCallback onSelectedDestCountryCallback = new OnSelectedCountryCallback() {
        @Override
        public void onSelected(int position) {
            preferenceHelper.setPositionTranslateTo(position);
            tvDes.setText(LanguageHepler.LIST_LANGUAGE.get(position).name + " " + LanguageHepler.getEmojiFlag(position));
            translate(editText.getText().toString().trim());
            currentPosDestLang = position;
        }
    };

    // speak

    private OnSpeakCallback onSpeakSourceCallback = new OnSpeakCallback() {
        @Override
        public void success() {
            onSpeakSourceComplete();
        }

        @Override
        public void failure() {
            onSpeakSourceComplete();
        }
    };

    private OnSpeakCallback onSpeakDestCallback = new OnSpeakCallback() {
        @Override
        public void success() {
            onSpeakDestComplete();
        }

        @Override
        public void failure() {
            onSpeakDestComplete();
        }
    };

    public void onSpeakSourceComplete() {
    }

    public void onSpeakDestComplete() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                resultView.invalidate();
            }
        });
    }

    //translate

    private void translate(String query) {
        if (query.isEmpty())
            return;
        fvMean.setText("");
        if (pbMean.getVisibility() != View.VISIBLE) {
            pbMean.setVisibility(View.VISIBLE);
        }
        if (btnTranslate.getVisibility() != View.GONE) {
            btnTranslate.setVisibility(View.GONE);
        }
        if (fvContent != null && fvContent.getVisibility() != View.VISIBLE) {
            fvContent.setVisibility(View.VISIBLE);
        }
        if (editText != null && editText.getVisibility() != View.GONE) {
            editText.setVisibility(View.GONE);
        }

        fvContent.setText(query);

        String sl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom());
        String tl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo());
        if (!NetworkHelper.isNetworkConnected(getApplicationContext()) && GetTranslateHelper.setLanguage(sl, tl)) {
            GetTranslateHelper.isExistModel(new GetTranslateHelper.onTranslateOfflineCallback() {
                @Override
                public void onFinished(boolean b) {
                    if (b) {
                        GetTranslateHelper.translate(query, new GetTranslateHelper.onTranslateOffline() {
                            @Override
                            public void onResult(String s) {
                                if (null == s || s.isEmpty()) {
                                    fvMean.setText(getApplicationContext().getString(R.string.something_went_wrong));
                                } else {
                                    fvMean.setText(s);
                                }
                                pbMean.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        transOnline(query);
                    }
                }
            });
        } else {
            transOnline(query);
        }

    }


    private void transOnline(String query) {
        String sl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom());
        String tl = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo());
        mDisposable.clear();
        mDisposable.add(GetTranslateHelper.tranlateWithToken(sl, tl, query, "en", onTranslateTokenCallback));
    }

    OnTranslateTokenCallback onTranslateTokenCallback = new OnTranslateTokenCallback() {
        @Override
        public void onSuccess(Translation translation) {

            if (null == translation) {
                fvMean.setText(getApplicationContext().getString(R.string.something_went_wrong));
            } else {
                fvMean.setText(translation.getTranslateContent());
            }
            if (pbMean.getVisibility() != View.GONE) {
                pbMean.setVisibility(View.GONE);
            }

        }

        @Override
        public void onFailed(String message) {
            //fvMean.setText(getApplicationContext().getString(R.string.no_internet));
            if (pbMean.getVisibility() == View.GONE) {
                pbMean.setVisibility(View.GONE);
            }
            if (NetworkHelper.isNetworkConnected(getApplicationContext())) {
                fvMean.setText(getApplicationContext().getString(R.string.something_went_wrong));
            } else {
                fvMean.setText(getApplicationContext().getString(R.string.error_no_internet_connect_continue));
            }
        }
    };


}
