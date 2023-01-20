package com.example.bookrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {
    TextInputEditText titleEditText, rentEditText, informationEditText, usernameEditText, phoneEditText, addressEditText;
    RadioGroup radioGroup;
    ImageView imageView;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    String imageUri = "";

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
        usernameEditText.setEnabled(false);
        phoneEditText.setText(editor.getString("userPhone",null));
        addressEditText.setText(editor.getString("userAddress",null));

    }

    public void selectBookImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = getPath(data.getData());
        }
    }

    public void saveData(View view){
        Book newBook ;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(AddBookActivity.this);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedCategory = selectedRadioButton.getText().toString();

        String title = titleEditText.getText().toString();
        String rent = rentEditText.getText().toString();
        String information = informationEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String username = usernameEditText.getText().toString();

        if(!validateInputs(title,rent,information,username,phone,address,imageUri)){
            Toast.makeText(this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
        }else {

            try {
                newBook = new Book(-1,title, rent, information, selectedCategory, username, phone, address, imageUri);
                boolean success = dataBaseHelper.addBook(newBook);
                if(success) Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Unable to add book", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(this, "Error creating book!", Toast.LENGTH_SHORT).show();
            } finally {
                Intent i = new Intent(AddBookActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
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

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
}