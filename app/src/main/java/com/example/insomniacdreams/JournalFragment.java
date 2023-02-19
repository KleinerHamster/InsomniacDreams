package com.example.insomniacdreams;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JournalFragment extends Fragment {

    FirebaseFirestore fStore;
    List<Dream> dreamsA = new ArrayList<>();
    DreamAdapter adapter;
    RecyclerView rvAllCategory;
    Button buttonAddDream;
    ImageView addDreamB, nodreamsImage;
    SharedPreferences sharedPreferences;
    TextView helpLabel;
    ProgressBar progressBar;
    List<User> users = new ArrayList<>();
    DreamModel model;

    public JournalFragment() {
    }

    public static JournalFragment newInstance() {
        return new JournalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);

        sharedPreferences = getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        model = new ViewModelProvider(requireActivity()).get(DreamModel.class);
        //находим recyclerview в активности
        rvAllCategory = (RecyclerView) rootView.findViewById(R.id.journal_recyclerView);
        rvAllCategory.setHasFixedSize(true);

        //устанавливаем  layout manager для размещения элементов
        rvAllCategory.setLayoutManager(new LinearLayoutManager(getContext()));

        fStore=FirebaseFirestore.getInstance();
        alldata();
        nodreamsImage = (ImageView) rootView.findViewById(R.id.nodreamsImage);
        helpLabel=(TextView) rootView.findViewById(R.id.helpLabel);
        progressBar=(ProgressBar) rootView.findViewById(R.id.progressBar);
        userDateLoad();
        //переход к личной странице
        ImageView goToAccount = (ImageView) rootView.findViewById(R.id.goToAccount);
        goToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Account.class));
            }
        });

        //переход к чату
        ImageView goToChat = (ImageView) rootView.findViewById(R.id.goToChat);
        goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Chat.class);
                intent.putExtra("usersList", (Serializable) users);
                startActivity(intent);
            }
        });

        //переход к добавлению новой записи о сне
        addDreamB=(ImageView) rootView.findViewById(R.id.addDream);
        addDreamB.setOnClickListener(addDreams);

        buttonAddDream=(Button) rootView.findViewById(R.id.button_add_dream);
        buttonAddDream.setOnClickListener(addDreams);

        return rootView;
    }

    //переход к добавлению сна
    View.OnClickListener addDreams = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getContext(), AddDream.class));
        }
    };

    //получаем пользователей
    public void userDateLoad(){
        fStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        users.add(new User(document.getId(), String.valueOf(document.get("Name")),
                                String.valueOf(document.get("Surname")),String.valueOf(document.get("Photo"))));
                    }
                }
                else {
                    Log.w(TAG,"Error getting documents: "+task.getException());
                }
            }
        });
    }

    //метод для получения снов
    public void alldata(){
        String userID = sharedPreferences.getString("userId", null);
        fStore.collection("dreamsUser").document(userID).collection("dreams")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        dreamsA.add(new Dream(document.getId(), String.valueOf(document.get("Title")),
                                String.valueOf(document.get("Description")), document.getTimestamp("Date"),
                                String.valueOf(document.get("Time")), String.valueOf(document.get("Sleep")),
                                String.valueOf(document.get("Emotion"))));
                    }
                    if(!dreamsA.isEmpty()) {

                        Collections.sort(dreamsA, new MyComparator());
                        //создаем адаптер, передает массив данных
                        adapter = new DreamAdapter(dreamsA);

                        adapter.setOnItemClickListener(new DreamAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(getActivity(), ChangeDream.class);
                                Date date=dreamsA.get(position).getDate().toDate();
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat( "dd");
                                intent.putExtra("day", simpleDateFormat1.format(date));
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat( "MM");
                                intent.putExtra("month", simpleDateFormat2.format(date));
                                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat( "yyyy");
                                intent.putExtra("year", simpleDateFormat3.format(date));
                                intent.putExtra("id", dreamsA.get(position).getDreamID());
                                intent.putExtra("title", dreamsA.get(position).getTitle());
                                intent.putExtra("description", dreamsA.get(position).getDescription());
                                intent.putExtra("time", dreamsA.get(position).getTime());
                                intent.putExtra("sleep", dreamsA.get(position).getSleep());
                                intent.putExtra("emotion", dreamsA.get(position).getEmotion());
                                startActivity(intent);
                            }
                        });

                        //устанавливаем адаптер к recyclerview для заполнения эл-тов
                        rvAllCategory.setAdapter(adapter);
                        rvAllCategory.setVisibility(View.VISIBLE);
                        nodreamsImage.setVisibility(View.INVISIBLE);
                        helpLabel.setVisibility(View.INVISIBLE);
                        buttonAddDream.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        model.setData(dreamsA);

                    }
                    else{
                        nodreamsImage.setVisibility(View.VISIBLE);
                        helpLabel.setVisibility(View.VISIBLE);
                        buttonAddDream.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    Log.w(TAG,"Error getting documents: "+task.getException());
                }
            }
        });
    }
}
