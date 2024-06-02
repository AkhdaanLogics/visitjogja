package com.andorid.visitjogja;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
    private static final String TAG = "SignActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        emailEditText = findViewById(R.id.inputNewEmail);
        usernameEditText = findViewById(R.id.inputNewUsername);
        passwordEditText = findViewById(R.id.inputNewPassword);

        ImageView hidePassword = findViewById(R.id.hidePassword);
        hidePassword.setImageResource(R.drawable.hide_pwd);
        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordEditText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hidePassword.setImageResource(R.drawable.hide_pwd);
                } else {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePassword.setImageResource(R.drawable.show_pwd);
                }
            }
        });
        Button buttonSign = findViewById(R.id.buttonSignInAction);
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailNew = emailEditText.getText().toString().trim();
                String userNew = usernameEditText.getText().toString().trim();
                String passNew = passwordEditText.getText().toString().trim();

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
                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(SignActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        passwordEditText.setError("Your password is too weak, you should mix letters, numbers, and symbols");
                        passwordEditText.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e){
                        emailEditText.setError("Your email is invalid or already exists");
                        emailEditText.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e){
                        emailEditText.setError("Your email is already registered. Try to use another email");
                        emailEditText.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}