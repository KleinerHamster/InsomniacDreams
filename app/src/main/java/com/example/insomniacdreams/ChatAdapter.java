package com.example.insomniacdreams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMessage> chatMessages;
    private final String senderId;

    public static final int VIEW_TYPE_SENT=1;
    public static final int VIEW_TYPE_RECEIVED=2;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final TextView sentMessage;
        private final TextView timeMessage;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessage= (TextView) itemView.findViewById(R.id.textMessage);
            timeMessage= (TextView) itemView.findViewById(R.id.textDateTime);

        }

        void setData(ChatMessage chatMessage){
            sentMessage.setText(chatMessage.getMessage());
            Date date=chatMessage.getDate().toDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "MMMM dd, yyyy - hh:mm a");
            timeMessage.setText(simpleDateFormat.format(date));
        }

    }

    static class RecivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final TextView sentMessage;
        private final TextView timeMessage;
        private final TextView textName;
        private final ImageView imageProfile;

        public RecivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessage= (TextView) itemView.findViewById(R.id.textMessage);
            timeMessage= (TextView) itemView.findViewById(R.id.textDateTime);
            textName= (TextView) itemView.findViewById(R.id.textName);
            imageProfile=(ImageView) itemView.findViewById(R.id.imageProfile);
        }

        void setData(ChatMessage chatMessage){
            sentMessage.setText(chatMessage.getMessage());
            Date date=chatMessage.getDate().toDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "MMMM dd, yyyy - hh:mm a");
            timeMessage.setText(simpleDateFormat.format(date));
            textName.setText(chatMessage.getName()+" "+chatMessage.getSurname());
            if(!chatMessage.getPhoto().equals("")) {
                imageProfile.setImageBitmap(getUserImage(chatMessage.getPhoto()));
            }
            else{
                imageProfile.setImageResource(R.drawable.account_icon);
            }
        }
    }

    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_SENT){
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            //создаем inflate с костомной разметкой
            View contactView = inflater.inflate(R.layout.item_container_sent_message, parent, false);

            //возвращаем созданую ячейку
            ChatAdapter.SentMessageViewHolder viewHolder = new ChatAdapter.SentMessageViewHolder(contactView);
            return viewHolder;
        }
        else{
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            //создаем inflate с костомной разметкой
            View contactView = inflater.inflate(R.layout.item_container_received_message, parent, false);

            //возвращаем созданую ячейку
            ChatAdapter.RecivedMessageViewHolder viewHolder = new ChatAdapter.RecivedMessageViewHolder(contactView);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }
        else {
            ((RecivedMessageViewHolder) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).getSenderId().equals(senderId)){
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    //метод раскодировки фото
    private static Bitmap getUserImage(String codeImage){
        byte[] t = Base64.getDecoder().decode(codeImage);
        return BitmapFactory.decodeByteArray(t, 0, t.length);
    }
}
