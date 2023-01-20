package com.example.bookrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        User user = readUserFromTextFile();
        if (user != null) {
            TextView nameTextView = findViewById(R.id.textName);
            nameTextView.setText(user.getName());
            TextView emailTextView = findViewById(R.id.textEmail);
            emailTextView.setText(user.getEmail());

        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

    }

    private User readUserFromTextFile() {
        User user = new User();
        File file = new File(getFilesDir(), "signup_info.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            String userData[] = getEmailFromSharedPreferences();
            String loggedInEmail = userData[0];
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    user.setName(line.substring(6));
                } else if (line.startsWith("Email: ")) {
                    String email = line.substring(7);
                    if (email.equals(loggedInEmail)) {
                        user.setEmail(email);
                        user.setPhone(userData[1]);
                        user.setAddress(userData[2]);
                        break;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    private String[] getEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPreference", MODE_PRIVATE);
        String result[]={sharedPreferences.getString("userEmail", ""),sharedPreferences.getString("userPhone", ""),sharedPreferences.getString("userAddress", "")};
        System.out.println(Arrays.toString(result));
        return result;
    }

    public void saveInformation(View view){
        TextInputEditText address,phone;
        address =findViewById(R.id.address_editText);
        phone = findViewById(R.id.phone_editText);
        String userAddress = address.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        if(userAddress.length()<10){
            Toast.makeText(this, "Address Too short", Toast.LENGTH_SHORT).show();
        }else if(userPhone.length()<10){
            Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
        }else {
            SharedPreferences.Editor editor = getSharedPreferences("loginPreference", Context.MODE_PRIVATE).edit();
            editor.putString("userPhone",userPhone);
            editor.putString("userAddress",userAddress);
            editor.apply();
            Toast.makeText(this, "Profile information saved!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Clear the saved login information
            showAlertLogout();
            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public  void showAlertLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void logout(){
        SharedPreferences.Editor editor = getSharedPreferences("loginPreference", Context.MODE_PRIVATE).edit();
        editor.putBoolean("is_logged_in",false);
        editor.clear();
        editor.apply();

        // Redirect to the login page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}