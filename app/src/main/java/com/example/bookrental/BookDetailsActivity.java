package com.example.bookrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Book book = (Book) getIntent().getSerializableExtra("book");

        ImageView imageView = findViewById(R.id.image_view);
        TextView titleTextView = findViewById(R.id.title_text_view);
        TextView rentTextView = findViewById(R.id.rent_text_view);
        TextView informationTextView = findViewById(R.id.information_text_view);
        TextView categoryTextView = findViewById(R.id.category_text_view);
        TextView contactTextView = findViewById(R.id.username_contact);
        TextView addressTextView = findViewById(R.id.address_text_view);

        String bookTitle = book.getTitle();
        String bookRent = book.getRentPerWeek()+"/week";
        String bookInfo =  book.getDescription();
        String category = "Category: "+book.getCategory();
        String bookAuthor = book.getUsername();
        String authorAddress = book.getAddress();

        // Set the book information to the views
//        imageView.setImageURI(Uri.parse(book.getImage()));
        System.out.println(book.getImage());
        Glide.with(this).load(Uri.parse(book.getImage())).into(imageView);
        titleTextView.setText(bookTitle);
        rentTextView.setText(bookRent);
        informationTextView.setText(bookInfo);
        addressTextView.setText(authorAddress);
        contactTextView.setText(bookAuthor);
        categoryTextView.setText(category);

    }

    public void callOwner(View view){
        Book book = (Book) getIntent().getSerializableExtra("book");
        String phone = book.getPhone();
        String uri = "tel:" + phone.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
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
            Intent i = new Intent(BookDetailsActivity.this,UserProfile.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(BookDetailsActivity.this);
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