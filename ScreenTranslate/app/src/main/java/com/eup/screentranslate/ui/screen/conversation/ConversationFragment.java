package com.eup.screentranslate.ui.screen.conversation;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eup.screentranslate.R;
import com.eup.screentranslate.callback.OnSelectedCountryCallback;
import com.eup.screentranslate.callback.OnTranslateTokenCallback;
import com.eup.screentranslate.model.Translation;
import com.eup.screentranslate.ui.screen.conversation.adapter.ConversationAdapter;
import com.eup.screentranslate.databinding.FragmentConversationBinding;
import com.eup.screentranslate.ui.base.BaseFragment;
import com.eup.screentranslate.ui.screen.dialog.record.RecordNavigator;
import com.eup.screentranslate.ui.screen.dialog.record.RecordViewModel;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.dialog.DialogHelper;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.eup.screentranslate.util.speechrecognizer.SpeechRecognizerHelper;
import com.eup.screentranslate.util.translate.GetTranslateHelper;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationFragment extends BaseFragment<FragmentConversationBinding, ConversationViewModel> implements ConversationNavigator, RecordNavigator {

    public static final int PERMISSIONS_RECORD_REQ = 1000;
    private PreferenceHelper preferenceHelper;
    private RecordViewModel recordViewModel;
    private Dialog recorderDialog;
    private ConversationAdapter conversationAdapter;

    private OnSelectedCountryCallback onSource = new OnSelectedCountryCallback() {
        @Override
        public void onSelected(int position) {
            preferenceHelper.setPositionTranslateFrom(position);
            String langSource = LanguageHepler.getNameCountryByPosition(position);
            viewModel.langSource.setValue(langSource);
        }
    };

    private OnSelectedCountryCallback onDest = new OnSelectedCountryCallback() {
        @Override
        public void onSelected(int position) {
            preferenceHelper.setPositionTranslateTo(position);
            String langSource = LanguageHepler.getNameCountryByPosition(position);
            viewModel.langDest.setValue(langSource);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_conversation;
    }

    @Override
    public AndroidViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(ConversationViewModel.class);
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
        preferenceHelper = new PreferenceHelper(getContext(), Constants.PREFERENCE_SCREEN_TRANS);
        binding.setViewModel(viewModel);
        this.recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        binding.setRecordModel(recordViewModel);
        recordViewModel.setNavigatorConversation(this, null);
        initUI();
    }

    private void initUI() {
        conversationAdapter = new ConversationAdapter(new ArrayList<>(), viewModel);
        binding.rvConversation.setAdapter(conversationAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_RECORD_REQ) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) return;
            }
            showRecorderDialog();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showDialogChooseLangDest() {
        DialogHelper.showListPopupWindow(getContext(),binding.tvChooseDest,false,null,onDest);
    }

    @Override
    public void showDialogChooseLangSource() {
        DialogHelper.showListPopupWindow(getContext(),binding.tvChooseSource,true,onSource,null);
    }

    @Override
    public void showRecorderDialog() {
        if (!SpeechRecognizerHelper.checkPermissions(getContext())){
            requestPermissions(SpeechRecognizerHelper.permissions,PERMISSIONS_RECORD_REQ);
        } else {
            //recording screen
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                recorderDialog = new Dialog(getContext()
                        , android.R.style.Theme_Material_Light_Dialog_NoActionBar);
            } else {
                recorderDialog = new Dialog(getContext(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog);
            }

            recorderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            recorderDialog.setContentView(R.layout.dialog_recorder);
            recorderDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            RippleBackground rippleBackground = recorderDialog.findViewById(R.id.rippleBg);
            CircleImageView circleImageView = recorderDialog.findViewById(R.id.im_big_mic);
            TextView textRecord = recorderDialog.findViewById(R.id.tv_text_record);


            rippleBackground.startRippleAnimation();
            // An vao nut dung ghi am
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recordViewModel.isListening()) {
                        rippleBackground.stopRippleAnimation();
                        textRecord.setText(R.string.loading);
                        recordViewModel.stopListening();
                    }
                }
            });
            recorderDialog.show();
        }
    }

    @Override
    public void dismissRecorderDialog() {
        recorderDialog.dismiss();
    }

    @Override
    public void clearFocus() {

    }

    @Override
    public void onRecordComplete() {
        if (recorderDialog == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RippleBackground rippleBackground = recorderDialog.findViewById(R.id.rippleBg);
                TextView tvTextRecord = recorderDialog.findViewById(R.id.tv_text_record);
                tvTextRecord.setText(R.string.loading);
                rippleBackground.stopRippleAnimation();
            }
        });
    }

    @Override
    public void onGetRecordedText(String s) {
        ConversationAdapter.Message message = new ConversationAdapter.Message(
                s,
                "...",
                recordViewModel.isLeftConversation
        );
        ConversationAdapter adapter = (ConversationAdapter) binding.rvConversation.getAdapter();
        adapter.addMessage(message);
        binding.rvConversation.smoothScrollToPosition(adapter.getItemCount() - 1);
        // Translate
        String langSourceCode, langDestCode;
        if (message.isLeftConversation){
            langSourceCode = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom());
        } else langSourceCode = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo());

        if (!message.isLeftConversation){
            langDestCode = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateFrom());
        } else langDestCode = LanguageHepler.getLangCodeByPosition(preferenceHelper.getPositionTranslateTo());

        viewModel.compositeDisposable.add(GetTranslateHelper.tranlateWithToken(
                langSourceCode,
                langDestCode,
                message.message,
                "en",
                new OnTranslateTokenCallback() {
                    @Override
                    public void onSuccess(Translation translation) {
                        conversationAdapter.updateMessage(message.position,translation.getTranslateContent());
                    }

                    @Override
                    public void onFailed(String message) {

                    }
                }
        ));

    }

    @Override
    public void onNoTextRecognizeFromRecord() {
        Toast.makeText(getContext(),R.string.no_text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        viewModel.onInitLang();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (recordViewModel.isListening()){
            recordViewModel.stopListening();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        recordViewModel.destroySpeedchRecognizer();
        super.onDestroy();
    }
}
