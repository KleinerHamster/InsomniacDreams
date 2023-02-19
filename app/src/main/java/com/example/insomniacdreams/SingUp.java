package com.example.insomniacdreams;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingUp extends AppCompatActivity {

    Button singUp;

    FirebaseAuth mAuth;
    FirebaseFirestore dataBase;
    String userId;

    TextInputEditText name_edit, surname_edit, email_edit, password_edit, confirm_password_edit;
    TextInputLayout name_input, surname_input, email_input, password_input, confirm_password_input;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onStart() {
        super.onStart();
        //проверка текущего пользователя
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences("user data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        name_edit = (TextInputEditText) findViewById(R.id.name_edit);
        surname_edit = (TextInputEditText) findViewById(R.id.surname_edit);
        email_edit = (TextInputEditText) findViewById(R.id.email_edit);
        password_edit = (TextInputEditText) findViewById(R.id.password_edit);
        confirm_password_edit = (TextInputEditText) findViewById(R.id.confirm_password_edit);

        name_input = (TextInputLayout) findViewById(R.id.name_input);
        surname_input = (TextInputLayout) findViewById(R.id.surname_input);
        email_input = (TextInputLayout) findViewById(R.id.email_input);
        password_input = (TextInputLayout) findViewById(R.id.password_input);
        confirm_password_input = (TextInputLayout) findViewById(R.id.confirm_password_input);

        //кнопка регистрации
        singUp=(Button) findViewById(R.id.button_sing_up);
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_edit.getText().toString();
                String surname = surname_edit.getText().toString();
                String email = email_edit.getText().toString();
                String password = password_edit.getText().toString();
                String confirmPassword = confirm_password_edit.getText().toString();

                name_input.setErrorEnabled(false);
                surname_input.setErrorEnabled(false);
                email_input.setErrorEnabled(false);
                password_input.setErrorEnabled(false);
                confirm_password_input.setErrorEnabled(false);

                //создаем паттерны
                Pattern pattern = Pattern.compile("[a-z0-9]+[@][a-z0-9]+[.][a-z]{1,3}");
                //проверка вводимого
                Matcher matcher = pattern.matcher(email);
                boolean match = matcher.matches();

                //валидность вводимых данных
                if(name.equals("")) {
                    name_input.setError(getResources().getString(R.string.error_msg_name));
                }
                else if(surname.equals("")) {
                    surname_input.setError(getResources().getString(R.string.error_msg_surname));
                }
                else if(email.equals("")) {
                    email_input.setError(getResources().getString(R.string.error_msg_email));
                }
                else if(password.equals("")) {
                    password_input.setError(getResources().getString(R.string.error_msg_password));
                }
                else if(confirmPassword.equals("")) {
                    confirm_password_input.setError(getResources().getString(R.string.error_msg_confirm_password));
                }
                else if(password.length()<6){
                    password_input.setError(getResources().getString(R.string.error_msg_password_length));
                }
                else if(!password.equals(confirmPassword)){
                    password_input.setError(getResources().getString(R.string.error_msg_password_not));
                    confirm_password_input.setError(getResources().getString(R.string.error_msg_password_not));
                }
                //проверка почты и паттерны
                else if(!match) {
                    email_input.setError(getResources().getString(R.string.error_msg_email_in));
                }
                else {
                    //переход на новую активность
                    registration(email, password, name, surname);
                }
            }
        });

    }

    //метод для регистрации
    public void registration(String email, String password, String name, String surname){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userId= mAuth.getCurrentUser().getUid();
                            DocumentReference dr = dataBase.collection("users").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("Surname", surname);
                            user.put("Bio", "");
                            user.put("Photo", "");
                            dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "signUp:success");
                                    //запись в настройки
                                    editor.putString("LOGIN", "true");
                                    editor.putString("userId", userId);
                                    editor.putString("userEmail", email);
                                    editor.putString("userPassword", password);
                                    editor.putString("name", name);
                                    editor.putString("surname", surname);
                                    editor.putString("bio", "");
                                    editor.putString("photo", "");
                                    editor.commit();
                                    startActivity(new Intent(getApplicationContext(), MainScreen.class));
                                }
                            });
                        } else {
                            // если регистрация провалилась, то сообщение об ошибке
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SingUp.this, "Such a user exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}