package com.andorid.visitjogja;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailCreated, usernameCreated, passwordCreated;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailCreated = findViewById(R.id.inputEmail);
        passwordCreated = findViewById(R.id.inputPassword);

        authProfile = FirebaseAuth.getInstance();

        ImageView hidePassword = findViewById(R.id.hidePassword);
        hidePassword.setImageResource(R.drawable.hide_pwd);
        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordCreated.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    passwordCreated.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hidePassword.setImageResource(R.drawable.hide_pwd);
                } else {
                    passwordCreated.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePassword.setImageResource(R.drawable.show_pwd);
                }
            }
        });
        Button login = findViewById(R.id.buttonLoginInAction);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailCreated.getText().toString();
                String password = passwordCreated.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    usernameCreated.setError("Email cannot be empty");
                    usernameCreated.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    emailCreated.setError("Invalid email address");
                    emailCreated.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    passwordCreated.setError("Password cannot be empty");
                    usernameCreated.requestFocus();
                } else if (password.length() < 8) {
                    Toast.makeText(LoginActivity.this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                    passwordCreated.setError("Password must be at least 8 characters");
                    passwordCreated.requestFocus();
                } else {
                    loginUser(email, password);
                }
            }
        });
    }
    private void loginUser(String email, String password){
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        emailCreated.setError("This account does not exist or is no longer valid. Please register again.");
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        emailCreated.setError("Invalid credentials. Please try again.");
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}