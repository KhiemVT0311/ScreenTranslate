package com.eup.screentranslate.ui.screen.screentrans;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.eup.screentranslate.R;
import com.eup.screentranslate.databinding.FragmentScreenTransBinding;
import com.eup.screentranslate.google.ads.AdsBanner;
import com.eup.screentranslate.service.ScreenTranslatorService;
import com.eup.screentranslate.ui.base.BaseFragment;
import com.eup.screentranslate.util.OverlayPermission;
import com.eup.screentranslate.util.event.EventHelper;
import com.google.android.gms.ads.AdSize;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ScreenTransFragment extends  BaseFragment<FragmentScreenTransBinding,ScreenTransViewModel> implements ScreenTransNavigator{
    public static int REQUEST_CODE_HOVER_PERMISISON = 999;
    public static int REQUEST_CODE_SCREEN_SHOT = 111;
    MediaProjectionManager mpm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_screen_trans;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        binding.setViewModel(viewModel);
        mpm = (MediaProjectionManager) getActivity().getSystemService(AppCompatActivity.MEDIA_PROJECTION_SERVICE);

        initUI();
//        initAds();
    }

    @Subscribe
    public void onMainThreadEvent(EventHelper e){
        if (e.stateChange.equals(EventHelper.StateChange.STOP_SERVICE)){
            binding.btnScreeTrans.setActivated(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_HOVER_PERMISISON){
            if (resultCode == Activity.RESULT_OK && OverlayPermission.hasRuntimePermissionToDrawOverlay(getContext())){
                requestScreenShotPermission();
            } else binding.btnScreeTrans.setActivated(false);
        } else if (requestCode == REQUEST_CODE_SCREEN_SHOT) {
            if (resultCode == Activity.RESULT_OK){
                if (OverlayPermission.hasRuntimePermissionToDrawOverlay(getContext())){
                    binding.btnScreeTrans.setActivated(true);
                    startScreenTransService(resultCode, data);
                }else{
                    binding.btnScreeTrans.setActivated(false);
                    requestHoverPermission();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startScreenTransService(int resultCode, Intent data) {
        Intent intent = new Intent(requireContext(), ScreenTranslatorService.class)
                .putExtra(ScreenTranslatorService.EXTRA_RESULT_CODE, resultCode)
                .putExtra(ScreenTranslatorService.EXTRA_RESULT_INTENT, data);
        ContextCompat.startForegroundService(requireContext(), intent);
    }

    private void initUI() {
        if (isMyServiceRunning(ScreenTranslatorService.class)){
            binding.btnScreeTrans.setActivated(true);
        } else {
            binding.btnScreeTrans.setActivated(false);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }



    private void onClickScreenTransBtn(){
        if (binding.btnScreeTrans.isActivated()){
            binding.btnScreeTrans.setActivated(false);
            stopScreenTransService();
        } else {
            binding.btnScreeTrans.setActivated(false);
            startScreenTrans();
        }
    }

    private void startScreenTrans() {
        if (!OverlayPermission.hasRuntimePermissionToDrawOverlay(getContext())){
            requestHoverPermission();
        }else{
            requireScreenService();
        }
    }

    private void requireScreenService() {
        requestScreenShotPermission();
    }

    private void requestHoverPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            startActivityForResult(OverlayPermission.createIntentToRequestOverlayPermission(getContext()), REQUEST_CODE_HOVER_PERMISISON);
        } else requestScreenShotPermission();
    }

    private void requestScreenShotPermission() {
        startActivityForResult(mpm.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_SHOT);
    }

    private void stopScreenTransService() {
        getActivity().stopService(new Intent(getActivity(), ScreenTranslatorService.class));
    }

    private void initAds(){
        AdsBanner adsBanner = new AdsBanner(getContext(), viewModel.preferenceHelper);
        adsBanner.banner(binding.layoutBanner, AdSize.MEDIUM_RECTANGLE);
    }

    @Override
    public ScreenTransViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(ScreenTransViewModel.class);
    }

    @Override
    protected void setNavigator() {
        viewModel.setNavigator(this);
    }

    @Override
    protected void setLifeCycle() {
        binding.setLifecycleOwner(this);
    }


    @Override
    public void onStartScreenTrans() {
       onClickScreenTransBtn();
    }
}
