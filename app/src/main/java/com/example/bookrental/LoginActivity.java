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

//        --------------------------------------------------------------------
        /*
        File file = new File(getFilesDir(), "signup_info.txt");
        if (!file.exists()) {
            Toast.makeText(this, "Signup information not found", Toast.LENGTH_SHORT).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            String name = null;
            String userEmail =null;
            while ((line = br.readLine()) != null) {

                if(line.startsWith("Name: ")) {
                    name = line.substring("Name: ".length());
                    System.out.println(name);
                }if (line.startsWith("Email: ") && line.substring(7).equals(email)) {
                    userEmail=line.substring(7);
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("Password: ") && line.substring(10).equals(password)) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            // Save login information in SharedPreferences
                            SharedPreferences.Editor editor = getSharedPreferences("loginPreference", Context.MODE_PRIVATE).edit();
                            editor.putBoolean("is_logged_in", true);
                            System.out.println(name+" "+userEmail);
                            editor.putString("username",name);
                            editor.putString("userEmail",userEmail);
                            editor.apply();
                            redirectHome();
                            return;
                        }
                    }
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error reading signup info", Toast.LENGTH_SHORT).show();
        }

         */
    }
}