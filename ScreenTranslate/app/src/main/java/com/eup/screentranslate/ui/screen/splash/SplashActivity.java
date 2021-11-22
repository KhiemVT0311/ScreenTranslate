package com.eup.screentranslate.ui.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.eup.screentranslate.R;
import com.eup.screentranslate.databinding.ActivitySplashBinding;
import com.eup.screentranslate.google.ads.AdsBanner;
import com.eup.screentranslate.ui.base.BaseActivity;
import com.eup.screentranslate.ui.screen.main.MainActivity;
import com.google.android.gms.ads.AdSize;

public class SplashActivity extends BaseActivity<ActivitySplashBinding,SplashViewModel> {
    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //allow the window to expand to the outside of the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initAds();
        startMainActivity();
    }

    private void initAds() {

        AdsBanner adsBanner = new AdsBanner(this,viewModel.preferenceHelper);
        adsBanner.banner((FrameLayout) binding.idLayoutAdsBanner, AdSize.SMART_BANNER);

    }

    private void startMainActivity(){
        if (isFinishing()) return;
        Intent intentMain = new Intent(SplashActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

       startActivity(intentMain);
        ActivityCompat.finishAffinity(SplashActivity.this);
    }

    private void initUI() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
