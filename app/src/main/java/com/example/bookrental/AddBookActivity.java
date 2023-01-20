package com.example.bookrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AddBookActivity extends AppCompatActivity {
    TextInputEditText titleEditText, rentEditText, informationEditText, usernameEditText, phoneEditText, addressEditText;
    RadioGroup radioGroup;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        titleEditText = findViewById(R.id.title_editText);
        rentEditText = findViewById(R.id.rent_editText);
        informationEditText = findViewById(R.id.information_editText);
        usernameEditText = findViewById(R.id.username_editText);
        phoneEditText = findViewById(R.id.phone_editText);
        addressEditText = findViewById(R.id.address_editText);
        radioGroup = findViewById(R.id.radio_group);

        SharedPreferences editor = getSharedPreferences("loginPreference", Context.MODE_PRIVATE);
        usernameEditText.setText(editor.getString("username",null));
        phoneEditText.setText(editor.getString("userPhone",null));
        addressEditText.setText(editor.getString("userAddress",null));


    }

    public void selectBookImage(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData().toString();

        }
    }

    public void saveData(View view){

//        FileOutputStream outputStream;

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedCategory = selectedRadioButton.getText().toString();

        String title = titleEditText.getText().toString();
        String rent = rentEditText.getText().toString();
        String information = informationEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String username = usernameEditText.getText().toString();

        if(!validateInputs(title,rent,information,username,phone,address)){
            Toast.makeText(this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
        }else {

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            File file = new File(getFilesDir(), "book_info.txt");

            FileOutputStream outputStream;
            BufferedWriter writer = null;
            try {
                outputStream = new FileOutputStream(file, true);
                writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.append("Category: " + selectedCategory);
                writer.newLine();
                writer.append("Image Uri: " + imageUri);
                writer.newLine();
                writer.append("Title: " + title);
                writer.newLine();
                writer.append("Rent: " + rent);
                writer.newLine();
                writer.append("Information: " + information);
                writer.newLine();
                writer.append("Username: " + username);
                writer.newLine();
                writer.append("Phone: " + phone);
                writer.newLine();
                writer.append("Address: " + address);
                writer.newLine();
                writer.flush();

                Intent i = new Intent(AddBookActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void viewData(View view){
        Intent i = new Intent(AddBookActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public boolean validateInputs(String ...items){
        for(String i: items){
            if(i.trim().length()==0) return false;
        }
        return true;
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
        }else if(id == R.id.action_profile){
            Intent i = new Intent(AddBookActivity.this,UserProfile.class);
            startActivity(i);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
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