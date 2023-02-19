package com.example.insomniacdreams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class StatisticFragment extends Fragment {
    private DreamModel model;

    public StatisticFragment() {
    }

    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistic, container, false);
        TextView hideText= (TextView) rootView.findViewById(R.id.hideText);
        Button buttonDiagram = (Button) rootView.findViewById(R.id.diagram_btn);
        Button buttonTable = (Button) rootView.findViewById(R.id.table_btn);
        buttonTable.setEnabled(false);
        buttonDiagram.setEnabled(false);
        model = new ViewModelProvider(requireActivity()).get(DreamModel.class);

        if(model.get().getValue()!=null) {
            buttonTable.setEnabled(true);
            buttonDiagram.setEnabled(true);
            //кнопка для просмотра диаграмм
            buttonDiagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideText.setVisibility(View.INVISIBLE);
                    DiagrammsFragment diagrammsFragment = DiagrammsFragment.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.diagram_table, diagrammsFragment)
                            .addToBackStack(null).commit();
                }
            });

            //кнопка для просмотра таблиц
            buttonTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideText.setVisibility(View.INVISIBLE);
                    TableFragment tableFragment = TableFragment.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.diagram_table, tableFragment)
                            .addToBackStack(null).commit();
                }
            });
        }
        else{
            Toast.makeText(getContext(), getResources().getString(R.string.nodreamsremind), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

}
