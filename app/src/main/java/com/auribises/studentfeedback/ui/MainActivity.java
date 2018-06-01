package com.auribises.studentfeedback.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auribises.studentfeedback.R;
import com.auribises.studentfeedback.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText eTxtName, eTxtEmail, eTxtPassword;
    Button btnRegister, btnSignIn;

    User user;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    void initViews(){
        eTxtName = findViewById(R.id.editTextName);
        eTxtEmail = findViewById(R.id.editTextEmail);
        eTxtPassword = findViewById(R.id.editTextPassword);

        btnRegister = findViewById(R.id.buttonRegister);
        btnSignIn = findViewById(R.id.buttonSignIn);

        user = new User();

        btnRegister.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = auth.getCurrentUser();

        if(firebaseUser!=null){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,"Dear User Please Sign-In",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    void deleteFromCloud(){
        String uid = auth.getCurrentUser().getUid();
        firestore.collection("users").document(uid).delete();//.addOnCompleteListener()
    }

    void saveUserInCloud(){

        String uid = auth.getCurrentUser().getUid();

        firestore.collection("users").document(uid).set(user).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        /*firestore.collection("users").add(user).addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isComplete()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });*/

    }

    void registerUser(){

        auth.createUserWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()) {
                           saveUserInCloud();
                        }
                    }
                });

    }

    void signInUser(){
        auth.signInWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        user.name = eTxtName.getText().toString();
        user.email = eTxtEmail.getText().toString();
        user.password = eTxtPassword.getText().toString();

        if(v.getId() == R.id.buttonRegister){
            registerUser();
        }else{
            signInUser();
        }
    }
}
