package com.example.insomniacdreams;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Chat extends AppCompatActivity {
    FirebaseFirestore fStore;
    SharedPreferences sharedPreferences;
    String userID, name, surname, photo="";
    List<ChatMessage> chatMessages = new ArrayList<>();
    ChatAdapter adapter;
    RecyclerView rv;
    EditText inputMessage;
    ProgressBar progressBar;
    List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        fStore = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        users = (List<User>) extras.get("usersList");

        sharedPreferences = getSharedPreferences("user data", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userId", null);
        name=sharedPreferences.getString("name", "");
        surname=sharedPreferences.getString("surname", "");
        photo=sharedPreferences.getString("photo", "");
        inputMessage = (EditText) findViewById(R.id.inputMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //кнопка возврата
        ImageView backToJournal= (ImageView)  findViewById(R.id.backToJournal);
        backToJournal.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainScreen.class)));
        listenMessage();
        //кнопка отправления
        FrameLayout layoutSend = (FrameLayout) findViewById(R.id.layoutSend);
        layoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(name, surname, photo, userID,inputMessage.getText().toString());
            }
        });

        adapter = new ChatAdapter(chatMessages, userID);
        rv = (RecyclerView) findViewById(R.id.chatRecycler);
        rv.setHasFixedSize(true);
        //устанавливаем  layout manager для размещения элементов
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setAdapter(adapter);
    }

    public void sendMessage(String name, String surname, String photo, String userID, String message) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("Message", message);
        user.put("Time", new Date());
        user.put("UserID", userID);
        fStore.collection("Chat").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Message was sent:success");
                inputMessage.setText("");
            }
        });
    }

    private void listenMessage(){
        fStore.collection("Chat").addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener=(value, error)->{
        if(error!=null){
            return;
        }
        if(value!=null){
            int count = chatMessages.size();
            for(DocumentChange documentChange: value.getDocumentChanges()){
                if(documentChange.getType()==DocumentChange.Type.ADDED){
                    for (int i=0; i<users.size();i++) {
                        if(users.get(i).getUserId().equals(documentChange.getDocument().getString("UserID"))){
                            chatMessages.add(new ChatMessage(documentChange.getDocument().getString("UserID"),
                                    documentChange.getDocument().getString("Message"),
                                    documentChange.getDocument().getTimestamp("Time"),
                                    users.get(i).getName(), users.get(i).getSurname(),
                                    users.get(i).getPhoto()));
                        }
                    }

                }
            }
            Collections.sort(chatMessages, (obj1, obj2)-> obj1.getDate().compareTo(obj2.getDate()));
            if(count==0){
                adapter.notifyDataSetChanged();
            }else{
                adapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                rv.smoothScrollToPosition(chatMessages.size()-1);
            }
            rv.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    };

}