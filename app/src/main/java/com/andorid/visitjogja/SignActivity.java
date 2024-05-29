package com.andorid.visitjogja;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        String emailNew = emailEditText.getText().toString();
        String userNew = usernameEditText.getText().toString();
        String passNew = passwordEditText.getText().toString();

        emailEditText = findViewById(R.id.inputNewEmail);
        usernameEditText = findViewById(R.id.inputNewUsername);
        passwordEditText = findViewById(R.id.inputNewPassword);
        if (emailNew.isEmpty()){
            emailEditText.setError("Email cannot be empty");
        }
        if (userNew.isEmpty()) {
            usernameEditText.setError("Username cannot be empty");
        }
        if (passNew.isEmpty()){
            passwordEditText.setError("Password cannot be empty");
        }
        else{
            auth.createAccount(user, pass, email).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(SignActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
});