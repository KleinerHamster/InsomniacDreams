package com.example.insomniacdreams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DreamAdapter extends RecyclerView.Adapter<DreamAdapter.ViewHolder>{

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView titleDreams;
        final TextView descriptionDreams;
        final TextView dateDream;
        final TextView type;
        final ImageView moodDream;

        ViewHolder(View view, OnItemClickListener listener){
            super(view);
            titleDreams= (TextView) view.findViewById(R.id.titleDreams);
            descriptionDreams= (TextView) view.findViewById(R.id.descriptionDreams);
            dateDream = (TextView) view.findViewById(R.id.dateDream);
            type= (TextView) view.findViewById(R.id.type);
            moodDream =(ImageView) view.findViewById(R.id.moodDream);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    private final List<Dream> dreams;

    public DreamAdapter(List<Dream> dreams) {
        this.dreams = dreams;
    }

    //метод создания новой ячейки
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //создаем inflate с костомной разметкой
        View contactView = inflater.inflate(R.layout.item_dreams, parent, false);

        //возвращаем созданую ячейку
        DreamAdapter.ViewHolder viewHolder = new DreamAdapter.ViewHolder(contactView, mListener);
        return viewHolder;
    }

    //метод для привязки данных к ячейке
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //получаем по номеру позиции данные
        Dream dream = dreams.get(position);
        //устанавливаем эл-ту на view и модели данных
        TextView title= holder.titleDreams;
        title.setText(dream.getTitle());
        TextView description= holder.descriptionDreams;
        description.setText(dream.getDescription());
        TextView typeDream = holder.type;
        typeDream.setText(dream.getSleep());

        TextView dateDream = holder.dateDream;
        Date date=dream.getDate().toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "MMM dd yyyy");
        dateDream.setText(simpleDateFormat.format(date));

       ImageView mood = holder.moodDream;
       if(dream.getEmotion().equals("joy")){
           mood.setImageResource(R.drawable.amizingenable);
       }
       else if(dream.getEmotion().equals("good")){
           mood.setImageResource(R.drawable.goodenable);
       }
       else if(dream.getEmotion().equals("normal")){
            mood.setImageResource(R.drawable.normalenable);
       }
       else if(dream.getEmotion().equals("bad")){
            mood.setImageResource(R.drawable.badnormalenable);
       }
       else if(dream.getEmotion().equals("sadness")){
           mood.setImageResource(R.drawable.badenable);
       }

    }

    //метод для получения кол-ва эл-тов списка
    @Override
    public int getItemCount() {
        return dreams.size();
    }
}
