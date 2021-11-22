package com.eup.screentranslate.ui.screen.conversation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eup.screentranslate.R;
import com.eup.screentranslate.ui.screen.conversation.ConversationViewModel;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;
    private List<Message> messages;
    private ConversationViewModel viewModel;

    public ConversationAdapter(List<Message> messages, ConversationViewModel viewModel){
        this.messages = messages;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_LEFT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_left, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_right, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isLeftConversation) return TYPE_LEFT;
        return TYPE_RIGHT;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textOrigin.setText(message.message);
        holder.textTranslate.setText(message.translation);
    }

    public void addMessage(Message message){
        message.position = this.messages.size();
        this.messages.add(message);
        notifyItemInserted(this.messages.size() - 1);
    }

    public void updateMessage(int position, String newTranslation){
        this.messages.get(position).translation = newTranslation;
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textOrigin;
        TextView textTranslate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrigin = itemView.findViewById(R.id.tv_source);
            textTranslate = itemView.findViewById(R.id.tv_trans);
        }
    }

    public static class Message {
        public String message;
        public String translation;
        public boolean isLeftConversation;
        public int position = -1;

        public Message(String message, String translation, boolean isLeftConversation) {
            this.message = message;
            this.translation = translation;
            this.isLeftConversation = isLeftConversation;
        }
    }
}
