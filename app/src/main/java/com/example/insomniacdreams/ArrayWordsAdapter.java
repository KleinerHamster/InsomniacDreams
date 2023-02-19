package com.example.insomniacdreams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArrayWordsAdapter extends RecyclerView.Adapter<ArrayWordsAdapter.ViewHolder> implements Filterable {

    public class ViewHolder extends RecyclerView.ViewHolder {
        final RecyclerView recyclerView1;
        final TextView letter;
        ViewHolder(View view){
            super(view);
            recyclerView1 = view.findViewById(R.id.recycler_main_dreams);
            letter = view.findViewById(R.id.dreamLetter);
        }
    }

    private ArrayList<ArrayWords> arrayWords;
    private ArrayList<ArrayWords> fullArrayWords;

    //конструктор
    public ArrayWordsAdapter(ArrayList<ArrayWords> arrayWords) {
        this.arrayWords = arrayWords;
        fullArrayWords=new ArrayList<>(arrayWords);
    }

    //метод создания новой ячейки
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //создаем inflate с костомной разметкой
        View contactView = inflater.inflate(R.layout.item_dream_book, parent, false);

        //возвращаем созданую ячейку
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    //метод для привязки данных к ячейке
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //получаем по номеру позиции данные
        ArrayWords category = arrayWords.get(position);

        //устанавливаем эл-ту на view и модели данных
        TextView letterDream= holder.letter;
        letterDream.setText(category.getLetter());

        ArrayList<WordDream> words= category.getWordDreams();
        WordDreamAdapter wordsAdapter = new WordDreamAdapter(words);
        holder.recyclerView1.setHasFixedSize(true);
        holder.recyclerView1.setLayoutManager(new LinearLayoutManager(letterDream.getContext(), LinearLayoutManager.VERTICAL,false));
        holder.recyclerView1.setAdapter(wordsAdapter);
    }

    //метод для получения кол-ва эл-тов списка
    @Override
    public int getItemCount() {
        return arrayWords.size();
    }

    //метод для поиска - фильтр, для нужных данных
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    //созадаем свой собственный фильтр
    private Filter exampleFilter = new Filter() {
        //метод для сравнение поискового запроса с данными
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            //CharSequence - интерфейс, может быть CharBuffer, Segment, String, StringBuffer, StringBuilder
            ArrayList<ArrayWords> filteredCategory =new ArrayList<>();
            int f=0;
            //если строка пуста
            if(charSequence==null || charSequence.length()==0){
                filteredCategory.addAll(fullArrayWords);
            }
            else{
                //trim() - удаление лишних пробелов в строке
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(ArrayWords item: fullArrayWords){
                    ArrayList<WordDream> apps = new ArrayList<>();
                    f=0;
                    for(WordDream app: item.getWordDreams()){
                        if(app.getDreamName().toLowerCase().contains(filterPattern)){
                            apps.add(app);
                            f=1;
                        }
                    }
                    if(f!=0){
                        filteredCategory.add(new ArrayWords(item.getLetter(), apps));
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filteredCategory;
            return results;
        }
        //вывод результата
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            arrayWords.clear();
            arrayWords.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
