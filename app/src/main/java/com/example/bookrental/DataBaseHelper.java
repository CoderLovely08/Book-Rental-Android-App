package com.example.bookrental;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String COLUMN_BOOK_IMAGE = "book_image";
    private static String TABLE_NAME = "BookInfo";
    private static String COLUMN_BOOK_TITLE = "book_title";
    private static String COLUMN_BOOK_RENT = "book_rent";
    private static String COLUMN_BOOK_ABOUT = "book_about";
    private static String COLUMN_BOOK_CATEGORY = "book_category";
    private static String COLUMN_BOOK_OWNER = "book_owner";
    private static String COLUMN_BOOK_PHONE = "book_phone";
    private static String COLUMN_BOOK_ADDRESS = "book_address";

    private static String USER_TABLE = "UserInfo";
    private static String COLUMN_USER_NAME = "user_name";
    private static String COLUMN_USER_EMAIL ="user_email";
    private static String COLUMN_USER_PASSWORD ="user_password";
    private static String COLUMN_USER_PHONE = "user_phone";
    private static String COLUMN_USER_ADDRESS = "user_address";
    public DataBaseHelper(@Nullable Context context) {
        super(context, "Rent.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "Create table "+TABLE_NAME+"(book_id INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_BOOK_TITLE+" text, "+COLUMN_BOOK_RENT+" text, "+COLUMN_BOOK_ABOUT+" text, "+COLUMN_BOOK_CATEGORY+" text, "+COLUMN_BOOK_OWNER+" text, "+COLUMN_BOOK_PHONE+" text, "+COLUMN_BOOK_ADDRESS+" text,"+COLUMN_BOOK_IMAGE+" text)";
        db.execSQL(createTable);

        String userCreateTable = "CREATE TABLE "+USER_TABLE+"(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_USER_NAME+" TEXT,"+COLUMN_USER_EMAIL+" TEXT,"+COLUMN_USER_PASSWORD+" TEXT,"+COLUMN_USER_PHONE+" TEXT, "+COLUMN_USER_ADDRESS+" TEXT)";
        db.execSQL(userCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addBook(Book book){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv =new ContentValues();

        cv.put(COLUMN_BOOK_TITLE,book.getTitle());
        cv.put(COLUMN_BOOK_RENT,book.getRentPerWeek());
        cv.put(COLUMN_BOOK_ABOUT, book.getDescription());
        cv.put(COLUMN_BOOK_CATEGORY,book.getCategory());
        cv.put(COLUMN_BOOK_OWNER,book.getUsername());
        cv.put(COLUMN_BOOK_PHONE, book.getPhone());
        cv.put(COLUMN_BOOK_ADDRESS, book.getAddress());
        cv.put(COLUMN_BOOK_IMAGE, book.getImage());

        long insert = db.insert(TABLE_NAME, null, cv);
        if(insert == -1){
            return false;
        }else return true;
    }

    public List<Book> getAllBooks(){
        List<Book> resultList = new ArrayList<>();

        String selectStatement = "Select * from "+TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()){
//            if there are any results
            do{
                int bookId = cursor.getInt(0);
                String bookTitle = cursor.getString(1);
                String bookRent = cursor.getString(2);
                String bookInformation = cursor.getString(3);
                String bookCategory = cursor.getString(4);
                String bookOwner = cursor.getString(5);
                String bookPhone = cursor.getString(6);
                String bookAddress = cursor.getString(7);
                String bookImage = cursor.getString(8);

                Book fetchedBook = new Book(bookId,bookTitle,bookRent,bookInformation, bookCategory, bookOwner, bookPhone, bookAddress, bookImage);
                resultList.add(fetchedBook);

            }while (cursor.moveToNext());
        }else {
//            No book added
        }

        cursor.close();
        db.close();
        return resultList;
    }

    public List<Book> getBooksByEmail(String userName){
        List<Book> resultList = new ArrayList<>();

        String query = "Select * from "+TABLE_NAME+ " WHERE "+ COLUMN_BOOK_OWNER + " = '" + userName + "'" ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
//            if there are any results
                do {
                    int bookId = cursor.getInt(0);
                    String bookTitle = cursor.getString(1);
                    String bookRent = cursor.getString(2);
                    String bookInformation = cursor.getString(3);
                    String bookCategory = cursor.getString(4);
                    String bookOwner = cursor.getString(5);
                    String bookPhone = cursor.getString(6);
                    String bookAddress = cursor.getString(7);
                    String bookImage = cursor.getString(8);

                    Book fetchedBook = new Book(bookId, bookTitle, bookRent, bookInformation, bookCategory, bookOwner, bookPhone, bookAddress, bookImage);
                    resultList.add(fetchedBook);

                } while (cursor.moveToNext());
            } else {
//            No book added
            }
        }

        cursor.close();
        db.close();
        return resultList;
    }

    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv =new ContentValues();

        // Check if email already exists in the "user" table
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = '" + user.getEmail() + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            // Email already exists, return false
            return false;
        }
        cursor.close();

        // Email does not exist, proceed with inserting the new user
        cv.put(COLUMN_USER_NAME,user.getName());
        cv.put(COLUMN_USER_EMAIL,user.getEmail());
        cv.put(COLUMN_USER_PASSWORD,user.getPassword());
        cv.put(COLUMN_USER_PHONE,user.getPhone());
        cv.put(COLUMN_USER_ADDRESS,user.getAddress());

        long insert = db.insert(USER_TABLE, null, cv);
        if(insert == -1){
            return false;
        }else return true;
    }


    public User getUser(String userEmail, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Select query to retrieve user from "user" table
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = '" + userEmail + "' AND " + COLUMN_USER_PASSWORD + " = '" + password + "'";
        Cursor cursor = db.rawQuery(query, null);

        User user = null;
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String phone = cursor.getString(3);
            String address = cursor.getString(4);
            user = new User(userId, name, email, password, phone, address);
            System.out.println(name+"\n"+email);
        }
        cursor.close();
        return user;
    }

    public boolean deleteBook(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME, "book_id = ?", new String[]{String.valueOf(bookId)});
        if(result > 0) return true;
        else return false;
    }

}
