package org.RodriDev.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<String> usuaris;
    private List<String> messages;

    public ChatAdapter() {
        usuaris = new ArrayList<>();
        messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String usuari = usuaris.get(position);
        String message = messages.get(position);

        holder.bindMessage(usuari, message);
    }

    @Override
    public int getItemCount() {
        return usuaris.size();
    }

    public void addMessage(String usuari, String message) {
        usuaris.add(usuari);
        messages.add(message);
        notifyDataSetChanged();
    }

    public void clear() {
        usuaris.clear();
        messages.clear();
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewMessage;

        MessageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }

        void bindMessage(String usuari, String message) {
            textViewName.setText(usuari);
            textViewMessage.setText(message);
        }
    }
}
