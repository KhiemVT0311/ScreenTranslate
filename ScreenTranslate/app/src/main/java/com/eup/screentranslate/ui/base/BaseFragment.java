package com.eup.screentranslate.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment <BD extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {
    protected BD binding;
    protected VM viewModel;

    protected abstract int getLayoutId();
    public abstract AndroidViewModel getViewModel();
    protected abstract void setNavigator();
    protected abstract void setLifeCycle();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (BD) DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        viewModel = (VM) getViewModel();
        setNavigator();
        setLifeCycle();
        return ((ViewDataBinding) binding).getRoot();
    }
}
