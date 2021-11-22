package com.eup.screentranslate.ui.screen.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.eup.screentranslate.R;
import com.eup.screentranslate.google.ads.AdsBanner;
import com.eup.screentranslate.google.ads.AdsInterstitial;
import com.eup.screentranslate.google.firebase.FirebaseConfig;
import com.eup.screentranslate.ui.screen.main.adapter.ViewPagerMainAdapter;
import com.eup.screentranslate.databinding.ActivityMainBinding;
import com.eup.screentranslate.ui.base.BaseActivity;
import com.eup.screentranslate.util.constants.Constants;
import com.google.android.gms.ads.AdSize;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    private int numTabNav =0;
    private AdsInterstitial adsInterstitial;

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        initUI();
        initFirebaseConfig();
        //initAds();
        getSizeWindow();
    }

    private void initFirebaseConfig() {
        FirebaseConfig firebaseConfig = new FirebaseConfig();
        firebaseConfig.fetchNewConfig(viewModel.preferenceHelper);
    }

    private void initAds() {
        AdsBanner adsBanner = new AdsBanner(this,viewModel.preferenceHelper);
        adsBanner.banner((FrameLayout) binding.layoutBanner, AdSize.SMART_BANNER);

        adsInterstitial = new AdsInterstitial(this,viewModel.preferenceHelper);
    }

    private void getSizeWindow(){
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        Constants.SCREEN_WIDTH = point.x;
        Constants.SCREEN_HEIGHT = point.y;
    }

    private void getData(){
        Intent intent = getIntent();
        boolean isOpenSettingsApp = false;
        if (intent != null)
            isOpenSettingsApp = intent.getBooleanExtra(Constants.OPEN_SETTINGS_APP,false);
        if (isOpenSettingsApp){
            if (binding.vpMain != null) binding.vpMain.setCurrentItem(3);
        }
    }

    public void initUI(){
        ViewPagerMainAdapter viewPagerMainAdapter = new ViewPagerMainAdapter(getSupportFragmentManager());
        binding.vpMain.setCanScrollHorizontal(false);
        binding.vpMain.setAdapter(viewPagerMainAdapter);
        binding.vpMain.setEnabled(true);
        binding.navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.screen_trans:
                        binding.vpMain.setCurrentItem(0, false);
                        break;
                    case R.id.translate:
                        binding.vpMain.setCurrentItem(1, false);
                        break;
                    case R.id.conversation:
                        binding.vpMain.setCurrentItem(2, false);
                        break;
                    case R.id.more:
                        binding.vpMain.setCurrentItem(3, false);
                        break;
                    default:
                        return false;
                }
               showAdInterstitial();
                return true;
            }
        });

        binding.vpMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        binding.navBottom.getMenu().findItem(R.id.screen_trans).setChecked(true);
                        break;
                    case 1:
                        binding.navBottom.getMenu().findItem(R.id.translate).setChecked(true);
                        break;
                    case 2:
                        binding.navBottom.getMenu().findItem(R.id.conversation).setChecked(true);
                        break;
                    case 3:
                        binding.navBottom.getMenu().findItem(R.id.more).setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragment() {
    }

    private void showAdInterstitial(){
        numTabNav++ ;
        if (numTabNav > 0 && numTabNav % 10 == 0){
            if (adsInterstitial != null) {
                adsInterstitial.show();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}