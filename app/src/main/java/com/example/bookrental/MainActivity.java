package com.example.bookrental;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Book> bookList = new ArrayList<>();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    ListView listView;
    SearchView searchView;

    BookAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_rent_book:
                        // code to be executed when "Item 1" is clicked
                        redirectAddBook();
                        break;
                    case R.id.nav_logout:
                        // code to be executed when "Item 2" is clicked
                        showAlertLogout();
                        break;
                    case R.id.nav_profile:
                        redirectUserProfile();
                    //
                }
                // close the navigation drawer
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        RadioGroup categoryRadioGroup = findViewById(R.id.category_radio_group);
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected radio button from the group
                RadioButton selectedRadioButton = group.findViewById(checkedId);
                String selectedCategory = selectedRadioButton.getText().toString();

                // Do something with the selected category
                // for example filterBooksByCategory(selectedCategory)

                List<Book> book = readBookFileInfo();
                filterBooksByCategory(selectedCategory,book);
            }
        });


        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File file = new File(getFilesDir(), "book_info.txt");

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            String imageUri = null;
            String title = null;
            String rent = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Image Uri: ")) {
                    imageUri = line.substring("Image Uri: ".length());
                } else if (line.startsWith("Title: ")) {
                    title = line.substring("Title: ".length());
                } else if (line.startsWith("Rent: ")) {
                    rent = line.substring("Rent: ".length());
                    bookList.add(new Book(imageUri, title, rent));
                    imageUri = null;
                    title = null;
                    rent = null;
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bookAdapter = new BookAdapter(this, bookList);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the clicked book object
                ArrayList<Book> books = readBookFileInfo();
                int index = books.indexOf(books);
                Book clickedBook = books.get(position);
                // Create an intent to open the BookDetailsActivity
                Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
                // Pass the clicked book object to the BookDetailsActivity using the putExtra() method
                intent.putExtra("book", clickedBook);
                // Start the BookDetailsActivity
                startActivity(intent);
            }
        });

    }

    private void redirectUserProfile() {
        Intent i = new Intent(MainActivity.this,UserProfile.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the submitted query here
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the text change here
                System.out.println(newText);
                List<Book> filteredBooks = new ArrayList<>();
                for (Book book : bookList) {
                    if (book.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                        filteredBooks.add(book);
                    }
                }
                List<Book> currentBookList = filteredBooks;
                bookAdapter = new BookAdapter(MainActivity.this, filteredBooks);
                bookAdapter.setBooks(filteredBooks);
                listView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Get the clicked book object
                        Book clickedBook = currentBookList.get(position);
                        // Create an intent to open the BookDetailsActivity
                        Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
                        // Pass the clicked book object to the BookDetailsActivity using the putExtra() method
                        intent.putExtra("book", clickedBook);
                        // Start the BookDetailsActivity
                        startActivity(intent);
                    }
                });

                return false;
            }
        });
        return true;
    }

    private void redirectAddBook() {
        Intent i = new Intent(MainActivity.this, AddBookActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Book> readBookFileInfo(){
        ArrayList<Book> bookList = new ArrayList<>();

        File file = new File(getFilesDir(), "book_info.txt");

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            String imageUri = null;
            String title = null;
            String rent = null;
            String information = null;
            String username = null;
            String phone = null;
            String address = null;
            String category =null;
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("Category: ")){
                      category = line.substring("Category: ".length());
                } else if (line.startsWith("Image Uri: ")) {
                    imageUri = line.substring("Image Uri: ".length());
                } else if (line.startsWith("Title: ")) {
                    title = line.substring("Title: ".length());
                } else if (line.startsWith("Rent: ")) {
                    rent = line.substring("Rent: ".length());
                } else if(line.startsWith("Information: ")){
                    information = line.substring("Information: ".length());
                }else if(line.startsWith("Username: ")){
                    username = line.substring("Username: ".length());
                }else if(line.startsWith("Phone: ")){
                    phone = line.substring("Phone: ".length());
                }else if(line.startsWith("Address: ")){
                    address = line.substring("Address: ".length());
                    bookList.add(new Book(title, information, rent, imageUri, username, phone, category, address));
                    imageUri = null;
                    title = null;
                    rent = null;
                    information = null;
                    username = null;
                    phone = null;
                    category = null;
                    address = null;
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    List<Book> currentBookList;
    private void filterBooksByCategory(String category, List<Book> bookList) {
        List<Book> filteredBooks = new ArrayList<>();
        if(!category.equals("All")) {
            for (Book book : bookList) {
                if (book.getCategory().equalsIgnoreCase(category)) {
                    filteredBooks.add(book);
                }
            }
        } else {
            filteredBooks = bookList;
        }
        currentBookList = filteredBooks;
        bookAdapter = new BookAdapter(this, filteredBooks);
        bookAdapter.setBooks(filteredBooks);
        listView.setAdapter(bookAdapter);
        bookAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the clicked book object
                Book clickedBook = currentBookList.get(position);
                // Create an intent to open the BookDetailsActivity
                Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
                // Pass the clicked book object to the BookDetailsActivity using the putExtra() method
                intent.putExtra("book", clickedBook);
                // Start the BookDetailsActivity
                startActivity(intent);
            }
        });
    }



    public  void showAlertLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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