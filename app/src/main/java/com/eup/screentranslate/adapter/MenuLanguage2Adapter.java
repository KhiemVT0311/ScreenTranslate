package com.eup.screentranslate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eup.screentranslate.R;
import com.eup.screentranslate.model.LanguageItem;
import com.eup.screentranslate.view.OnClickView;

import java.util.ArrayList;

public class MenuLanguage2Adapter extends ArrayAdapter<LanguageItem> implements View.OnClickListener {
    private int resId;
    private ArrayList<LanguageItem> listItem;
    private Context context;
    private OnClickView onClickView;

    public MenuLanguage2Adapter(Context context, ArrayList<LanguageItem> listItem, OnClickView onClickView){
        super(context, 0, listItem);
        this.context = context;
        this.listItem = listItem;
        this.onClickView = onClickView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_language, parent, false);

        LanguageItem item = listItem.get(position);
        ImageView ivFlag = convertView.findViewById(R.id.im_flag);
        TextView tvCountry = convertView.findViewById(R.id.tv_country);
        ivFlag.setImageResource(item.flag);
        tvCountry.setLabelFor(position);
        tvCountry.setText(item.name);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public void onClick(View v) {
        TextView tvCountry = v.findViewById(R.id.tv_country);
        onClickView.click(tvCountry.getLabelFor());
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }


}
