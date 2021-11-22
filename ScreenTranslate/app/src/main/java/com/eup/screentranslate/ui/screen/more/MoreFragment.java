package com.eup.screentranslate.ui.screen.more;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eup.screentranslate.BuildConfig;
import com.eup.screentranslate.R;
import com.eup.screentranslate.databinding.FragmentMoreBinding;
import com.eup.screentranslate.ui.base.BaseFragment;
import com.eup.screentranslate.util.dialog.DialogHelper;
import com.eup.screentranslate.util.event.EventHelper;

import org.greenrobot.eventbus.EventBus;

public class MoreFragment extends BaseFragment<FragmentMoreBinding, MoreViewModel> implements MoreNavigator {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public AndroidViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(MoreViewModel.class);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewmodel(viewModel);
        initUI();
    }

    private void initUI() {
    }

    @Override
    public void onFastScreenTrans() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_select_lang:
                showDialogChooseLang();
                return;
            case R.id.titleTerms:
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jtranslate.net/terms.html"));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            case R.id.titlePolicy:
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jtranslate.net/policy.html"));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            case R.id.titleContact:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/palain");
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] {"eup.gaming@gmail.com", "sandk@eupgroup.net"});
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.feedback_screen_translate));
                intent.putExtra(Intent.EXTRA_TEXT,
                        "\n\n----------------------------\n"
                        + "Device name: " + Build.MODEL
                        + "\nAndroid ver: " + Build.VERSION.RELEASE
                        + "\nApp ver: " + BuildConfig.VERSION_CODE
                        + "\n----------------------------\n");
                intent.setType("message/rfc822");
                try {
                    startActivity(Intent.createChooser(intent, getString(R.string.user_feedback)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(requireContext(), R.string.error_feed_back, Toast.LENGTH_SHORT).show();
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = getString(R.string.action_share);
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
                    e.printStackTrace();
                }
                return;
            default:
                return;
        }
    }

    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i){
                case 0:
                    viewModel.preferenceHelper.setLanguageApp("en");
                    break;
                case 1:
                    viewModel.preferenceHelper.setLanguageApp("vi");
                    break;
                case 2:
                    viewModel.preferenceHelper.setLanguageApp("ja-JP");
                    break;
                default:
                    break;
            }
            binding.tvLanguage.setText(viewModel.getCurrentLangAppName());
            EventBus.getDefault().post(new EventHelper(EventHelper.StateChange.CHANGE_LANG_APP));
            dialogInterface.dismiss();
        }
    };

    public void showDialogChooseLang() {
        DialogHelper.showDialogChooseLangApp(requireContext(),getPositionLangApp(), onClickListener);
    }

    public int getPositionLangApp() {
        String langApp = viewModel.preferenceHelper.getLanguageApp();
        if (langApp.equals("en")) return 0;
        else if (langApp.equals("vi")) return 1;
        else if (langApp.equals("ja-JP")) return 2;
        return 1;
    }

    @Override
    public void onSwitchTextAutoSpeech() {
        boolean isChecked = binding.switchTextAutoSpeech.isChecked();
        viewModel.preferenceHelper.setAutoTextToSpeech(isChecked);
        viewModel.isAutoTextSpeech.setValue(isChecked);
    }

    @Override
    public void onResume() {
        viewModel.onInitLangAppName();
        viewModel.onInitTextToSpeech();
        super.onResume();
    }
}
