package com.eup.screentranslate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eup.screentranslate.R;
import com.eup.screentranslate.model.LanguageItem;
import com.eup.screentranslate.view.OnClickView;

import java.util.List;

public class MenuLanguageAdapter extends ArrayAdapter<LanguageItem> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private int positionSelected;
    private List<LanguageItem> countries;
    private OnClickView onClickView;

    public MenuLanguageAdapter(@NonNull Context context, int resource, List<LanguageItem> countries, OnClickView onClickView) {
        super(context, resource);
        this.mContext = context;
        this.positionSelected = resource;
        this.countries = countries;
        this.onClickView = onClickView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LanguageItem country = countries.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_language, parent, false);
        }

        ImageView imFlag = convertView.findViewById(R.id.im_flag);
        TextView tvCountry = convertView.findViewById(R.id.tv_country);

        imFlag.setImageResource(country.flag);
        tvCountry.setText(country.name.trim());

        //checkBox.setOnCheckedChangeListener(this);
        convertView.setOnClickListener(this);
        return convertView;
    }


    @Override
    public void onClick(View view) {
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setChecked(true);
        onClickView.click(checkBox.getLabelFor());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b)
            onClickView.click(compoundButton.getLabelFor());
    }
}
