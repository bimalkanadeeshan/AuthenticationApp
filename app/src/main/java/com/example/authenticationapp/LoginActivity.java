package com.example.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth       = FirebaseAuth.getInstance();
        mLoginBtn   = findViewById(R.id.loginBtn);
        mCreateBtn  = findViewById(R.id.createText);

        mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }else{
                    mEmail.setError("");
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }else{
                    mPassword.setError("");
                }
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }else{
                    mPassword.setError("");
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(LoginActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

    }
}