package com.example.insomniacdreams;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingIn extends AppCompatActivity {

    Button singIn;
    TextView singUp;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    TextInputEditText email_edit,  password_edit;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        email_edit = (TextInputEditText) findViewById(R.id.email_edit);
        password_edit = (TextInputEditText) findViewById(R.id.password_edit);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences("user data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        singIn=(Button) findViewById(R.id.button_sing_in);
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=email_edit.getText().toString();
                String password =password_edit.getText().toString();
                //создаем паттерны
                Pattern pattern = Pattern.compile("[a-z0-9]+[@][a-z0-9]+[.][a-z]{1,3}");
                //проверка вводимого
                Matcher matcher = pattern.matcher(email);
                boolean match = matcher.matches();
                if(email.equals("")){
                    showMessage("Enter email!");
                }
                else if(password.equals("")){
                    showMessage("Enter password!");
                }
                else if(!match){
                    showMessage("Enter valid email!");
                }
                else {
                    auth(email, password);
                }
            }
        });

        //переход к регистрации
        singUp=(TextView) findViewById(R.id.go_to_sing_up);
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SingUp.class));
            }
        });
    }


    //метод для показа toast
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    //аутификация поль-ля
    public void auth(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId= mAuth.getCurrentUser().getUid();
                            String userEmail = mAuth.getCurrentUser().getEmail();
                            DocumentReference dr = fStore.collection("users").document(userId);
                            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            //запись в настройки
                                            editor.putString("LOGIN", "true");
                                            editor.putString("userId", userId);
                                            editor.putString("userEmail", userEmail);
                                            editor.putString("userPassword", password);
                                            editor.putString("name", document.getString("Name"));
                                            editor.putString("surname", document.getString("Surname"));
                                            editor.putString("bio", document.getString("Bio"));
                                            editor.putString("photo", document.getString("Photo"));
                                            editor.commit();
                                            startActivity(new Intent(getApplicationContext(), MainScreen.class));
                                        }
                                        else {
                                            Log.d(TAG, "No such document");
                                        }
                                    }
                                    else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            showMessage("Check email and password!");
                        }
                    }
                });
    }

}