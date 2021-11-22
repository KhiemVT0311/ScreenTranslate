package com.eup.screentranslate.ui.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.eup.screentranslate.util.MyContextWrapper;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.event.EventHelper;
import com.eup.screentranslate.util.preference.PreferenceHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity <BD extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {
    protected BD binding;
    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EventHelper eventHelper){
        if (eventHelper.stateChange == EventHelper.StateChange.CHANGE_LANG_APP){
            recreate();
        }
    }


    protected abstract Class<VM> getViewModelClass();

    protected abstract void init();

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(newBase, Constants.PREFERENCE_SCREEN_TRANS);
        String langApp = preferenceHelper.getLanguageApp();
        super.attachBaseContext(MyContextWrapper.wrap(newBase,langApp));
    }
}
