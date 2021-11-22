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

import java.util.ArrayList;
import java.util.List;

public class DefinitionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CATEGORY = 1;
    public static final int DETAIL_DEF = 2;

    private List<Translation.Entry__> entry__s;
    private TranslateViewModel viewModel;
    private Context context;

    public DefinitionAdapter( Context context, TranslateViewModel viewModel) {
        this.viewModel = viewModel;
        this.context = context;
        entry__s = new ArrayList<>();
    }

    public void setData(List<Translation.Entry__> entry__s){
        this.entry__s = entry__s;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == CATEGORY){
            view = LayoutInflater.from(context).inflate(R.layout.item_category_definition,parent,false);
            return new CategoryViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_detail_definition,parent,false);
            return new DetailDefViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Translation.Entry__ entry__ = entry__s.get(position);
        if (entry__.getExample() == null && entry__.getDefinitionId() == null) return CATEGORY;
        return DETAIL_DEF;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Translation.Entry__ entry__ = entry__s.get(position);
        if (holder instanceof  CategoryViewHolder) {
            ((CategoryViewHolder) holder).tvCategory.setText(entry__.getGloss());
        } else {
            ((DetailDefViewHolder) holder).tvGloss.setText(context.getString(R.string.bullet_symbol) + " "+ entry__.getGloss());

            if (entry__.getExample() != null && !entry__.getExample().isEmpty())
                ((DetailDefViewHolder) holder).tvExample.setText("Example: "+entry__.getExample());
            else ((DetailDefViewHolder) holder).tvExample.setText("");
        }
    }

    @Override
    public int getItemCount() {
        if (entry__s != null)
                return entry__s.size();
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory;

        public CategoryViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tvCategory);
        }
    }

    public class DetailDefViewHolder extends RecyclerView.ViewHolder{
        TextView tvGloss;
        TextView tvExample;

        public DetailDefViewHolder(View view) {
            super(view);
            tvGloss = view.findViewById(R.id.tvGloss);
            tvExample = view.findViewById(R.id.tvExample);
        }
    }
}
