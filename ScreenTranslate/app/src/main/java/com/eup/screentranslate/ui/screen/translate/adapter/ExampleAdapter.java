package com.eup.screentranslate.ui.screen.translate.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
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

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ViewHolder>{
    private List<Translation.Example> examples;
    private Context context;
    private TranslateViewModel viewModel;

    public ExampleAdapter(Context context, TranslateViewModel viewModel){
        this.context = context;
        examples = new ArrayList<>();
        this.viewModel = viewModel;
    }

    public void setData(List<Translation.Example> examples){
        this.examples = examples;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExampleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_definition,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Translation.Example example = examples.get(position);
        if (example != null){
            Spanned text = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                text = Html.fromHtml(example.getText(), Html.FROM_HTML_MODE_COMPACT);
            else
                text = Html.fromHtml(example.getText());
            if (text != null)
                holder.tvExample.setText(text);

            Spanned finalText = text;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalText != null) viewModel.setTextInptTrans(finalText.toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return examples.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvExample;

        public ViewHolder(View view) {
            super(view);
            tvExample = view.findViewById(R.id.tvExample);
        }
    }
}
