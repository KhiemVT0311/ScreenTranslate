package com.eup.screentranslate.ui.screen.translate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eup.screentranslate.R;
import com.eup.screentranslate.model.Translation;
import com.eup.screentranslate.ui.screen.translate.TranslateViewModel;
import com.eup.screentranslate.util.string.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class DictoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int CATEGORY = 1;
    public static final int DETAIL_DIC = 2;
    private Context context;
    private List<Translation.Entry> entries;

    public DictoryAdapter(Context context){
        this.context = context;
        entries = new ArrayList<>();
    }

    public void setData(List<Translation.Entry> entries){
        this.entries = entries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CATEGORY){
            View view = LayoutInflater.from(context).inflate(R.layout.item_category_definition, parent, false);
            return new CategoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_detail_dict, parent, false);
            return new DetailDicViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Translation.Entry entry = entries.get(position);
        if (holder instanceof CategoryViewHolder){
            ((CategoryViewHolder) holder).tvCategory.setText(entry.getWord());
        } else {
            ((DetailDicViewHolder) holder).tvWord.setText(entry.getWord());
            String wordTrans = StringHelper.appendAllList(entry.getReverseTranslation(), ',', '.');
            ((DetailDicViewHolder) holder).tvReverTrans.setText(wordTrans);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Translation.Entry entry = entries.get(position);
        if (entry.getReverseTranslation() == null) return CATEGORY;
        return DETAIL_DIC;
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        public CategoryViewHolder(View view){
            super(view);
            tvCategory = view.findViewById(R.id.tvCategory);
        }
    }

    public class DetailDicViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord;
        TextView tvReverTrans;
        public DetailDicViewHolder(View view){
            super(view);
            tvWord = view.findViewById(R.id.tvWord);
            tvReverTrans = view.findViewById(R.id.tvReverTrans);
        }
    }
}
