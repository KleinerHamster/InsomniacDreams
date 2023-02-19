package com.example.insomniacdreams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WordMeaningFragment extends Fragment {

    //ключу для сохранения настроек
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //параметры view, которые будем менять -
    //инициализировать в конструкторе
    private String mParam1;
    private String mParam2;

    public WordMeaningFragment() {
    }

    public static WordMeaningFragment newInstance(String param1, String param2) {
        WordMeaningFragment fragment = new WordMeaningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //иницилизируем данные из бандла, если он не нулевой
        //проверим, есть ли нужные ключи
        //получаем данные
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_meaning, container, false);

        TextView word = (TextView) rootView.findViewById(R.id.word);
        word.setText(mParam1);

        TextView meaningWord = (TextView) rootView.findViewById(R.id.meaning_word);
        meaningWord.setText(mParam2);

        //кнопка для возврата к справачнику
        ImageView nextButton = (ImageView) rootView.findViewById(R.id.back_to_search);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DreamBookFragment dreamBookFragment = new DreamBookFragment ();
                FragmentManager ft = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = ft.beginTransaction();
                fragmentTransaction.replace(R.id.fl_content, dreamBookFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }

}
