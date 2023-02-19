package com.example.insomniacdreams;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {

    TextView cancelButton, btnLogout, btnChangePhoto, btnDone;
    ImageView show_eye, photoChange;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText nameChange, surnameChange, bioChange, emailChange, passwordChange;
    int f=0;
    FirebaseAuth mAuth;
    FirebaseFirestore dataBase;
    String imageEncode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        sharedPreferences = getSharedPreferences("user data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseFirestore.getInstance();

        nameChange=(EditText) findViewById(R.id.nameChange);
        nameChange.setText(sharedPreferences.getString("name", null));
        surnameChange=(EditText) findViewById(R.id.surnameChange);
        surnameChange.setText(sharedPreferences.getString("surname", null));
        bioChange =(EditText) findViewById(R.id.bioChange);
        bioChange.setText(sharedPreferences.getString("bio", null));
        emailChange =(EditText) findViewById(R.id.emailChange);
        emailChange.setText(sharedPreferences.getString("userEmail", null));
        passwordChange =(EditText) findViewById(R.id.passwordChange);
        passwordChange.setText(sharedPreferences.getString("userPassword", null));
        photoChange = (ImageView)  findViewById(R.id.photoChange);
        if(sharedPreferences.getString("photo", null)!=null && !sharedPreferences.getString("photo", null).equals("")){
            photoChange.setImageBitmap(getUserImage(sharedPreferences.getString("photo", null)));
        }
        else{
            photoChange.setImageResource(R.drawable.account_icon);
        }

        //кнопка обновления
        btnDone= (TextView) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameChange.getText().toString();
                String surname = surnameChange.getText().toString();
                String bio = bioChange.getText().toString();
                String password = passwordChange.getText().toString();
                String email = emailChange.getText().toString();
                String photo = sharedPreferences.getString("photo", null);
                if(name.equals("") || surname.equals("") || password.equals("")){
                    Toast.makeText(Account.this, "Enter all data!", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6){
                    Toast.makeText(Account.this, getResources().getString(R.string.error_msg_password_length), Toast.LENGTH_SHORT).show();
                }
                else{
                    updateData(sharedPreferences.getString("userId", null), name, surname, bio, email, password, photo);
                }
            }
        });

        //кнопка для загрузки фото
        btnChangePhoto= (TextView) findViewById(R.id.btnChangePhoto);
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        //кнопка показа пароля
        show_eye=(ImageView)  findViewById(R.id.show_eye);
        show_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(f==0) {
                    show_eye.setImageResource(R.drawable.hide);
                    passwordChange.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    f=1;
                }

                else{
                    show_eye.setImageResource(R.drawable.eye);
                    passwordChange.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    f=0;
                }
            }
        });

        //отмена действий
        cancelButton = (TextView) findViewById(R.id.btnCanel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
            }
        });

        //кнопка выхода из аккунта
        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), SingIn.class));
            }
        });
    }

    //
    public void updateData(String userId, String name, String surname, String bio, String email, String password, String photo){
        FirebaseUser updateUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, sharedPreferences.getString("userPassword", null));

        updateUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated");
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Name", name);
                                        user.put("Surname", surname);
                                        user.put("Bio", bio);
                                        user.put("Photo", photo);
                                        dataBase.collection("users").document(userId).set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG, "Update data");
                                                        Toast.makeText(Account.this, "You update data!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "update:failure");
                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });

        editor.putString("LOGIN", "true");
        editor.putString("userId", userId);
        editor.putString("userEmail", email);
        editor.putString("userPassword", password);
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.putString("bio", bio);
        editor.putString("photo", photo);
        editor.apply();
    }

    //метод для кодирования фото
    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight=bitmap.getHeight()*previewWidth/ bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes =  byteArrayOutputStream.toByteArray();
        editor.putString("photo", Base64.getEncoder().encodeToString(bytes));
        editor.apply();
        return Base64.getEncoder().encodeToString(bytes);
    }


    //загрузка фото
    private ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            photoChange.setImageBitmap(bitmap);
                            imageEncode = encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Bitmap getUserImage(String codeImage){
        byte[] t = Base64.getDecoder().decode(codeImage);
        byte[] bytes= Base64.getDecoder().decode(codeImage);
        return BitmapFactory.decodeByteArray(t, 0, t.length);
    }

}