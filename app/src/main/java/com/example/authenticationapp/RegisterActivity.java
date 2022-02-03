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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    TextInputLayout mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mFullName    = findViewById(R.id.fullName);
        mEmail       = findViewById(R.id.email);
        mPassword    = findViewById(R.id.password);
        mPhone       = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn    = findViewById(R.id.createText);

        fAuth        = FirebaseAuth.getInstance();
        fStore       = FirebaseFirestore.getInstance();
        progressBar  = findViewById(R.id.progressBar);

        //if user logged previously load main activity
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String email    = mEmail.getEditText().getText().toString();
              String password = mPassword.getEditText().getText().toString();
              String fullName = mFullName.getEditText().getText().toString();
              String phone    = mPhone.getEditText().getText().toString();

              //Validate Phone Number
              Pattern p = Pattern.compile("[0][0-9]{9}");
              Matcher m = p.matcher(phone);
              if(!m.matches()){
                  mPhone.setError("Please Enter a Valid Phone Number.");
                  return;
              }else{
                  mPhone.setError("");
              }

              //Validate Name
              if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Name is Required.");
                    return;
              }else{
                  mFullName.setError("");
              }

              //Validate Email
              if(TextUtils.isEmpty(email)){
                  mEmail.setError("Email is Required.");
                  return;
              }else{
                  mEmail.setError("");
              }

              //Validate Password
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

              // register the user in firebase

              fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                          userID = fAuth.getCurrentUser().getUid();
                          DocumentReference documentReference = fStore.collection("users").document(userID);
                          Map<String,Object> user = new HashMap<>();
                          user.put("fName",fullName);
                          user.put("email",email);
                          user.put("phone",phone);
                          documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  //Toast.makeText(RegisterActivity.this, "onSuccess.", Toast.LENGTH_SHORT).show();
                              }
                          });
                          startActivity(new Intent(getApplicationContext(),MainActivity.class));

                      }else {
                          Toast.makeText(RegisterActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                          progressBar.setVisibility(View.GONE);
                      }
                  }
              });

            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}