package com.example.bookrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText mEmailFieldLogin, mPasswordFieldLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailFieldLogin = findViewById(R.id.userEmail);
        mPasswordFieldLogin = findViewById(R.id.userPassword);
    }

    public void redirectSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void redirectHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginClicked(View view) {
        String email = mEmailFieldLogin.getText().toString();
        String password = mPasswordFieldLogin.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Email field is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password field is required", Toast.LENGTH_SHORT).show();
            return;
        }
        
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        User user = dataBaseHelper.getUser(email,password);
        System.out.println(user);
        if(user!=null){
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            // Save login information in SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("loginPreference", Context.MODE_PRIVATE).edit();
            editor.putBoolean("is_logged_in", true);
            editor.putString("username",user.getName());
            editor.putString("userEmail",user.getEmail());
            editor.apply();
            redirectHome();
        }else Toast.makeText(this, "Incorrect credentials!", Toast.LENGTH_SHORT).show();
    }
}