package com.example.bookrental;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    List<Book> bookList = new ArrayList<>();
    Button button;
    ListView listView;
    BookUserAdapter bookAdapter;
    TextInputEditText address,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView nameTextView = findViewById(R.id.textName);
        TextView emailTextView = findViewById(R.id.textEmail);
        address =findViewById(R.id.address_editText);
        phone = findViewById(R.id.phone_editText);

        button = findViewById(R.id.button);
        button.setEnabled(false);
        User user = readUserFromTextFile();
        if (user != null) {

            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            DataBaseHelper db = new DataBaseHelper(this);
            User currentUser = db.getUserByEmail(user.getEmail());

            address.setText(currentUser.getAddress());
            phone.setText(currentUser.getPhone());
            address.setEnabled(false);
            phone.setEnabled(false);

        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        bookList = dataBaseHelper.getBooksByEmail(user.getName());

        bookAdapter = new BookUserAdapter(this, bookList);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(bookAdapter);
    }

    private User readUserFromTextFile() {

        SharedPreferences sharedPreferences = getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("userEmail", "");
        String phone = sharedPreferences.getString("userPhone","");
        String address = sharedPreferences.getString("userAddress", "");
        return new User(-1, name, email,null, phone, address);
    }


    public void saveInformation(View view){
        TextInputEditText address,phone;
        address =findViewById(R.id.address_editText);
        phone = findViewById(R.id.phone_editText);
        String userAddress = address.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        if(userPhone.length()<10){
            Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
        }else if(userAddress.length()<10){
            Toast.makeText(this, "Address Too short", Toast.LENGTH_SHORT).show();
        }else {
            SharedPreferences.Editor editor = getSharedPreferences("loginPreference", Context.MODE_PRIVATE).edit();
            editor.putString("userPhone", userPhone);
            editor.putString("userAddress", userAddress);
            editor.apply();

            SharedPreferences sharedPreferences = getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
            String email = sharedPreferences.getString("userEmail", "");

            DataBaseHelper db =new DataBaseHelper(this);

            if(db.updateUser(email,userPhone,userAddress))
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();

//            Toast.makeText(this, "Profile information saved!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, UserProfile.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
    }

    public void editInformation(View view){
        Button editButton = findViewById(R.id.buttonEdit);
        editButton.setEnabled(false);
        button.setEnabled(true);
        phone.setText("");
        phone.setEnabled(true);
        address.setText("");
        address.setEnabled(true);
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
    public List<Book> readBookDataBase(){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        return dataBaseHelper.getAllBooks();
    }

}