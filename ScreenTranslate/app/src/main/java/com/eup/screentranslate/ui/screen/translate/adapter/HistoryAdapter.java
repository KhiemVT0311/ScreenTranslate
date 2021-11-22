package com.eup.screentranslate.ui.screen.translate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eup.screentranslate.R;
import com.eup.screentranslate.databinding.ItemHistoryBinding;
import com.eup.screentranslate.model.History;
import com.eup.screentranslate.ui.screen.translate.TranslateViewModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<History> histories;
    private TranslateViewModel viewModel;

    public HistoryAdapter(Context context, TranslateViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setHistories(List<History> listHistories) {
        histories = listHistories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_history, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = histories.get(position);
        holder.binding.setViewModel(viewModel);
        holder.binding.setHistory(history);
    }

    @Override
    public int getItemCount() {
        if (histories != null)
            return histories.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ItemHistoryBinding binding;

        public ViewHolder(ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
