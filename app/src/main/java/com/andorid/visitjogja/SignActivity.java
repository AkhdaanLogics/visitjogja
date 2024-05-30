package com.andorid.visitjogja;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        emailEditText = findViewById(R.id.inputNewEmail);
        usernameEditText = findViewById(R.id.inputNewUsername);
        passwordEditText = findViewById(R.id.inputNewPassword);

        Button buttonSign = findViewById(R.id.buttonSignInAction);
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailNew = emailEditText.getText().toString();
                String userNew = usernameEditText.getText().toString();
                String passNew = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(emailNew)) {
                    Toast.makeText(SignActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    emailEditText.setError("Email cannot be empty");
                    emailEditText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailNew).matches()) {
                    Toast.makeText(SignActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    emailEditText.setError("Valid email address is required");
                    emailEditText.requestFocus();
                } else if (TextUtils.isEmpty(userNew)) {
                    Toast.makeText(SignActivity.this, "Please enter your username", Toast.LENGTH_LONG).show();
                    usernameEditText.setError("Username cannot be empty");
                    usernameEditText.requestFocus();
                } else if (TextUtils.isEmpty(passNew)) {
                    Toast.makeText(SignActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    passwordEditText.setError("Password cannot be empty");
                    passwordEditText.requestFocus();
                } else if (passNew.length() < 8) {
                    Toast.makeText(SignActivity.this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                    passwordEditText.setError("Password must be at least 8 characters");
                    passwordEditText.requestFocus();
                } else {
                    registerUser(emailNew, userNew, passNew);
                }
            }
        });
    }

    private void registerUser(String emailNew, String userNew, String passNew) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(emailNew, passNew).addOnCompleteListener(SignActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    firebaseUser.sendEmailVerification();
//                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
                }
            }
        });
    }
}