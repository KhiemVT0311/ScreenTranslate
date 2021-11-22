package com.eup.screentranslate.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ListPopupWindow;

import com.eup.screentranslate.R;
import com.eup.screentranslate.adapter.MenuLanguage2Adapter;
import com.eup.screentranslate.adapter.MenuLanguageAdapter;
import com.eup.screentranslate.callback.OnSelectedCountryCallback;
import com.eup.screentranslate.util.language.LanguageHepler;
import com.eup.screentranslate.view.OnClickView;

public class DialogHelper {

    public static void showPopUpLanguage(Context context, View anchor, boolean isSource, int positionSelected, OnSelectedCountryCallback onSelectedSourceCountryCallback, OnSelectedCountryCallback onSelectedDestCountryCallback){
        final PopupWindow popupWindow =  new PopupWindow(context);
        OnClickView onClickView = new OnClickView() {
            @Override
            public void click(int position) {
                if (isSource) onSelectedSourceCountryCallback.onSelected(position);
                else onSelectedDestCountryCallback.onSelected(position);

                popupWindow.dismiss();
            }
        };

        View view = LayoutInflater.from(context).inflate(R.layout.layout_popup_menu_language, null);
        ListView listView = view.findViewById(R.id.rvMenuLang);
        MenuLanguageAdapter adapter = new MenuLanguageAdapter(context, positionSelected, LanguageHepler.LIST_LANGUAGE, onClickView);
        listView.setAdapter(adapter);

        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(anchor);
    }

    public static void showListPopupWindow(Context context, View anchor, boolean isSource, OnSelectedCountryCallback onSelectedSourceCountryCallback, OnSelectedCountryCallback onSelectedDestCountryCallback){
        ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setWidth(500);
        listPopupWindow.setHeight(1000);
        listPopupWindow.setAnchorView(anchor);

        final OnClickView onClickView = new OnClickView() {
            @Override
            public void click(int position) {
                listPopupWindow.dismiss();
                if(isSource)
                    onSelectedSourceCountryCallback.onSelected(position);
                else
                    onSelectedDestCountryCallback.onSelected(position);
            }
        };

        listPopupWindow.setModal(true);
        MenuLanguage2Adapter myAdapter = new MenuLanguage2Adapter(context, LanguageHepler.LIST_LANGUAGE, onClickView);
        listPopupWindow.setAdapter(myAdapter);
        listPopupWindow.show();
    }

    public static Dialog showLoadingDialog(String title, Context context){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_import_entry,null, false);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        dialog.setContentView(view);
        if (!dialog.isShowing())
            dialog.show();
        return dialog;
    }

    public static void showDialogChooseLangApp(Context context, int positionSelected, DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.select_language);
        String [] arrayLang = context.getResources().getStringArray(R.array.support_languages);
        ListAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_single_choice, arrayLang);
        builder.setSingleChoiceItems(arrayAdapter, positionSelected, onClickListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showYesNoDialog(Context context, int icon, String title, String desc, OnOkCallback onOkCallback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppAlertDialog);
        builder.setTitle(title)
                .setMessage(desc)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onOkCallback.onOkCallback();
                    }
                }).setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setIcon(icon)
                .setCancelable(true)
                .show();
    }
    public interface OnOkCallback {
        void onOkCallback();
    }
}
