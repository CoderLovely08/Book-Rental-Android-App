package com.example.bookrental;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class BookUserAdapter extends BaseAdapter {
    private List<Book> bookList;

    private List<Book> books;

    Context context;
    private LayoutInflater inflater;

    BookUserAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.profile_list_item, null);
        ImageView bookImage = view.findViewById(R.id.bookImg);
        TextView bookTitle = view.findViewById(R.id.bookTitle);
        TextView bookPrice = view.findViewById(R.id.bookPrice);

        String title = bookList.get(i).getTitle();
        String price = bookList.get(i).getRentPerWeek();

        Bitmap bitmap = BitmapFactory.decodeFile(bookList.get(i).getImage());
        bookImage.setImageBitmap(bitmap);
        bookTitle.setText(title);
        bookPrice.setText("₹"+price+"/week");

        System.out.println(bookList.get(i).getId());

        Button button = view.findViewById(R.id.delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this book?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        boolean success = dataBaseHelper.deleteBook(bookList.get(i).getId());
                        if(success) {
                            Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            Intent i = new Intent(context, UserProfile.class);
                            ((Activity)context).finish();
                            ((Activity)context).overridePendingTransition(0, 0);
                            context.startActivity(i);
                            ((Activity)context).overridePendingTransition(0, 0);
                        }
                        else Toast.makeText(context, "Error deleting book", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        return view;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }
}
