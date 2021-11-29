package com.eup.screentranslate.ui.screen.translate;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.eup.screentranslate.R;
import com.eup.screentranslate.ui.screen.translate.adapter.DefinitionAdapter;
import com.eup.screentranslate.ui.screen.translate.adapter.DictoryAdapter;
import com.eup.screentranslate.ui.screen.translate.adapter.ExampleAdapter;
import com.eup.screentranslate.ui.screen.translate.adapter.HistoryAdapter;
import com.eup.screentranslate.databinding.FragmentTranslateBinding;
import com.eup.screentranslate.model.Translation;
import com.eup.screentranslate.ui.base.BaseFragment;
import com.eup.screentranslate.ui.screen.dialog.record.RecordNavigator;
import com.eup.screentranslate.ui.screen.dialog.record.RecordViewModel;
import com.eup.screentranslate.callback.OnSelectedCountryCallback;
import com.eup.screentranslate.util.constants.Constants;
import com.eup.screentranslate.util.dialog.DialogHelper;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.util.network.NetworkHelper;
import com.eup.screentranslate.util.preference.PreferenceHelper;
import com.eup.screentranslate.util.speechrecognizer.SpeechRecognizerHelper;
import com.eup.screentranslate.util.translate.GetTranslateHelper;
import com.eup.screentranslate.view.BounceView;
import com.skyfishjy.library.RippleBackground;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class TranslateFragment extends BaseFragment<FragmentTranslateBinding,TranslateViewModel> implements TranslateNavigator , RecordNavigator {
    public static final int PERMISSIONS_RECORD_REQ = 1000;
    private PreferenceHelper preferenceHelper;
    private DefinitionAdapter definitionAdapter;
    private HistoryAdapter historyAdapter;
    private DictoryAdapter dictoryAdapter;
    private ExampleAdapter exampleAdapter;
    private RecordViewModel recordViewModel;
    private Dialog recorderDialog;
    private int positionFrom = 41;
    private int positionTo = LanguageHepler.getDefaultPositionLanguage();
    private Dialog loadingdialog ;

    private OnSelectedCountryCallback onSoure = new OnSelectedCountryCallback() {
        @Override
        public void onSelected(int position) {
            preferenceHelper.setPositionTranslateFrom(position);
            String langSource = LanguageHepler.getNameCountryByPosition(position);
            viewModel.langSource.setValue(langSource);
            showDownloadLanguage();
        }
    };

    private OnSelectedCountryCallback onDest = new OnSelectedCountryCallback() {
        @Override
        public void onSelected(int position) {
         preferenceHelper.setPositionTranslateTo(position);
         String langDest = LanguageHepler.getNameCountryByPosition(position);
         viewModel.langDest.setValue(langDest);
         showDownloadLanguage();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceHelper = new PreferenceHelper(getContext(), Constants.PREFERENCE_SCREEN_TRANS);
        binding.setViewModel(viewModel);
        viewModel.onInitLang();
        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        recordViewModel.setNavigatorConversation(null,this);
        binding.setRecordViewModel(recordViewModel);
        initUI();
    }

    private void initUI() {
        historyAdapter = new HistoryAdapter(getContext(), viewModel);
        binding.rvHistory.setAdapter(historyAdapter);

        definitionAdapter = new DefinitionAdapter(getContext(),viewModel);
        binding.rvDefinition.setAdapter(definitionAdapter);

        dictoryAdapter = new DictoryAdapter(getContext());
        binding.rvDict.setAdapter(dictoryAdapter);

        exampleAdapter = new ExampleAdapter(getContext(),viewModel);
        binding.rvExample.setAdapter(exampleAdapter);

        showDownloadLanguage();
        BounceView.addAnimTo(binding.constraintDownloadTranslate);
        binding.constraintDownloadTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionFrom = preferenceHelper.getPositionTranslateFrom();
                positionTo = preferenceHelper.getPositionTranslateTo();
                String from = LanguageHepler.getLangCodeByPosition(positionFrom);
                String to = LanguageHepler.getLangCodeByPosition(positionTo);
                String lang = (from+"-"+to).toUpperCase();
                GetTranslateHelper.setLanguage(from,to);
                DialogHelper.showYesNoDialog(requireContext(),
                        R.drawable.ic_question_dialog,
                        getString(R.string.download_translation_title, lang),
                        getString(R.string.download_translation_desc), new DialogHelper.OnOkCallback() {
                            @Override//
                            public void onOkCallback() {
                                if (!NetworkHelper.isNetworkConnected(getContext())){
                                    Toast.makeText(getContext(),R.string.error_no_internet_connect_continue,Toast.LENGTH_SHORT).show();
                                } else if (!isDetached()){
                                    loadingdialog = DialogHelper.showLoadingDialog(getString(R.string.downloading_data), requireContext());
                                    GetTranslateHelper.downloadModel(new GetTranslateHelper.onDownloadModelCallback() {
                                        @Override
                                        public void onDownloadFinished(boolean b) {
                                            if (!isDetached()){
                                                hideLoadingDialog();
                                                showDownloadLanguage();
                                                if (b) {
                                                    Toast.makeText(getContext(), R.string.download_data_success,Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(),R.string.download_audio_failed,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    private void hideLoadingDialog() {
        if (loadingdialog != null && loadingdialog.isShowing()) {
            loadingdialog.dismiss();
        }
    }

    private void showDownloadLanguage(){
        positionFrom = preferenceHelper.getPositionTranslateFrom();
        positionTo = preferenceHelper.getPositionTranslateTo();
        String from = LanguageHepler.getLangCodeByPosition(positionFrom);
        String to = LanguageHepler.getLangCodeByPosition(positionTo);
        String lang = (from+"-"+to).toUpperCase();
        binding.tvLanguageDownload.setText(lang.toUpperCase());
        if (!GetTranslateHelper.setLanguage(from,to) || from == to){
            binding.constraintDownloadTranslate.setVisibility(View.GONE);
        } else {
            binding.constraintDownloadTranslate.setVisibility(View.VISIBLE);
            GetTranslateHelper.isExistModel(new GetTranslateHelper.onTranslateOfflineCallback() {
                @Override
                public void onFinished(boolean b) {
                    if (b){
                        binding.constraintDownloadTranslate.setVisibility(View.GONE);
                    } else {
                        binding.constraintDownloadTranslate.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onInitLang();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_translate;
    }

    @Override
    public AndroidViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(TranslateViewModel.class);
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
    public void showDialogChooseLangSource() {
        DialogHelper.showListPopupWindow(getContext(),binding.btnLangSource,true,onSoure,null);
    }

    @Override
    public void showDialogChooseLangDest() {
        DialogHelper.showListPopupWindow(getContext(),binding.btnLangDest,false,null,onDest);
    }

    @Override
    public void showHistory() {
        binding.viewHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHistory() {
        binding.viewHistory.setVisibility(View.GONE);
    }

    @Override
    public void doCopy() {
        String textTrans = binding.tvTrans.getText().toString();
        if (textTrans != null && !textTrans.isEmpty()){
            ClipboardManager cbm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("COPY_TEXT", textTrans.trim());
            cbm.setPrimaryClip(data);
            Toast.makeText(getContext(), R.string.copy_successful, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showFailedTranslate(String message) {
        binding.tvTrans.setText(R.string.no_internet);
    }

    @Override
    public void swapLang() {
        rotateSwap();
        fadeRight();
        fadeLeft();
        binding.tvSource.setText(LanguageHepler.getNameCountryByPosition(positionFrom) + " " + LanguageHepler.getEmojiFlag(positionFrom));
        binding.tvDest.setText(LanguageHepler.getNameCountryByPosition(positionTo) + " " + LanguageHepler.getEmojiFlag(positionTo));
        binding.edtTextTrans.setText(viewModel.resultTranslate.getValue());
        showDownloadLanguage();
    }

    private void rotateSwap(){
        RotateAnimation rotate = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        binding.ibSwap.startAnimation(rotate);
    }

    private void fadeRight(){
        Animation outRight = AnimationUtils.loadAnimation(getContext(), R.anim.fab_slide_out_to_right);
        Animation inLeft = AnimationUtils.loadAnimation(getContext(), R.anim.fab_slide_in_from_left);
        outRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.btnLangSource.startAnimation(inLeft);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.btnLangDest.startAnimation(outRight);
    }

    private void fadeLeft(){
        Animation outRight = AnimationUtils.loadAnimation(getContext(), R.anim.fab_slide_out_to_right);
        Animation inLeft = AnimationUtils.loadAnimation(getContext(), R.anim.fab_slide_in_from_left);
        outRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.btnLangDest.startAnimation(inLeft);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.btnLangSource.startAnimation(outRight);
    }

    @Override
    public void showLoading() {
        binding.tvTrans.setText(R.string.translating);
    }

    @Override
    public void clearInputText() {
        binding.edtTextTrans.setText("");
        binding.tvTrans.setText("");
        binding.viewHistory.setVisibility(View.GONE);
        binding.viewExample.setVisibility(View.GONE);
    }

    @Override
    public void showViewTrans() {
        binding.viewTrans.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideViewTrans() {
        binding.viewTrans.setVisibility(View.GONE);
    }

    @Override
    public void setTextInputTrans(String text) {
        binding.edtTextTrans.setText(text);
    }

    @Override
    public void hideLayoutDict() {
        binding.viewDict.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutDict(List<Translation.Entry> entries) {
        binding.viewDict.setVisibility(View.VISIBLE);
        dictoryAdapter.setData(entries);
    }

    @Override
    public void hideLayoutDefinition() {
        binding.viewDefinition.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutDefinition(List<Translation.Entry__> entries) {
        binding.viewDefinition.setVisibility(View.VISIBLE);
        definitionAdapter.setData(entries);
    }

    @Override
    public void hideLayoutExample() {
        binding.viewExample.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutExample(List<Translation.Example> examples) {
        binding.viewExample.setVisibility(View.VISIBLE);
        exampleAdapter.setData(examples);
    }

    @Override
    public void hideLayoutSynset() {
        binding.viewSynset.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutSynset(List<Translation.Synset> synsets) {
        binding.viewSynset.setVisibility(View.VISIBLE);
        String title = "";
        StringBuilder text = new StringBuilder();
        for (Translation.Synset synset : synsets){
            if (title.isEmpty() && synset.getBaseForm() != null && !synset.getBaseForm().isEmpty()){
                title = getString(R.string.synonym_of, synset.getBaseForm());
            }
            if (synset.getEntry() != null){
                List<Translation.Entry_> entry_s = synset.getEntry();
                for (int j = 0; j < entry_s.size(); j++){
                    List<String> synonymes = entry_s.get(j).getSynonym();
                    for (int i = 0; i < synonymes.size(); i++){
                        if (i == 0) text.append("\u2022   " + synonymes.get(i));
                        else if (i == synonymes.size() - 1) text.append(",   " + synonymes.get(i));
                        else text.append(",   " + synonymes.get(i));

                        if (i == synonymes.size() - 1) text.append("\n\n");
                    }
                }
            }
        }
        text.deleteCharAt(text.length() - 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            binding.tvTitleSynset.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
        else
            binding.tvTitleSynset.setText(Html.fromHtml(title));

        SpannableString spanString = new SpannableString(text);
        Matcher matcher = Pattern.compile("\\w+").matcher(spanString);
        while (matcher.find()){
            String tag = matcher.group(0);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    if (!tag.isEmpty()){
                        setTextInputTrans(tag);
                    }
                }
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };

            spanString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        binding.tvSynsets.setText(spanString);
        binding.tvSynsets.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvSynsets.setHighlightColor(Color.parseColor("#33B5E5"));
        binding.tvSynsets.setLinkTextColor(Color.parseColor("#191919"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_RECORD_REQ){
            for (int grantResult : grantResults){
                if (grantResult != PackageManager.PERMISSION_GRANTED) return;
            }
            showRecorderDialog();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBeginOfSpeak(boolean isSource) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSource){
                    binding.imbSpeakSource.setImageResource(R.drawable.ic_pause_black);
                } else {
                    binding.imbSpeakDest.setImageResource(R.drawable.ic_pause_white);
                }
            }
        });
    }

    @Override
    public void onEndOfSpeak(boolean isSource) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSource){
                    binding.imbSpeakSource.setImageResource(R.drawable.ic_speaker_black);
                } else {
                    binding.imbSpeakDest.setImageResource(R.drawable.ic_speaker_white);
                }
            }
        });
    }

    @Override
    public void showRecorderDialog() {
        if (!SpeechRecognizerHelper.checkPermissions(getContext())){
            requestPermissions(SpeechRecognizerHelper.permissions, PERMISSIONS_RECORD_REQ);
        } else {

            // Giao dien ghi am
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                recorderDialog = new Dialog(getContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar);
            } else {
                recorderDialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog);
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
        binding.edtTextTrans.setText(s);
    }

    @Override
    public void onNoTextRecognizeFromRecord() {
        Toast.makeText(getContext(),R.string.no_text,Toast.LENGTH_SHORT);
    }
}
