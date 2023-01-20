package com.example.bookrental;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class BookAdapter extends BaseAdapter {
    private List<Book> bookList;

    private List<Book> books;

    Context context;
    private LayoutInflater inflater;

    BookAdapter(Context context, List<Book> bookList) {
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
        view = inflater.inflate(R.layout.list_item, null);
        ImageView bookImge = view.findViewById(R.id.bookImg);
        TextView bookTitle = view.findViewById(R.id.bookTitle);
        TextView bookPrice = view.findViewById(R.id.bookPrice);

        Uri imguri = Uri.parse(bookList.get(i).getImage());
        System.out.println(imguri);
        String title = bookList.get(i).getTitle();
        String price = bookList.get(i).getRentPerWeek();

        Glide.with(context).load(imguri).into(bookImge);

        bookTitle.setText(title);
        bookPrice.setText("₹"+price+"/week");

        return view;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }
}
