package com.example.insomniacdreams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WordDreamAdapter extends RecyclerView.Adapter<WordDreamAdapter.ViewHolder>{



    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;

        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.dreamName12);

        }
    }

    private final ArrayList<WordDream> wordDreams;

    //конструктор
    public WordDreamAdapter(ArrayList<WordDream> wordDreams) {
        this.wordDreams = wordDreams;
    }

    //метод создания новой ячейки
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //создаем inflate с костомной разметкой
        View contactView = inflater.inflate(R.layout.word_dream_book, parent, false);

        //возвращаем созданую ячейку
        WordDreamAdapter.ViewHolder viewHolder = new WordDreamAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    //метод для привязки данных к ячейке
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //получаем по номеру позиции данные
        WordDream app = wordDreams.get(position);

        //устанавливаем эл-ту на view и модели данных
        TextView name = holder.nameView;
        name.setText(app.getDreamName());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                WordMeaningFragment wordMeaningFragment = WordMeaningFragment.newInstance(app.getDreamName(), app.getDreamText());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, wordMeaningFragment).addToBackStack(null).commit();
            }
        });
    }

    //метод для получения кол-ва эл-тов списка
    @Override
    public int getItemCount() {
        return wordDreams.size();
    }

}
