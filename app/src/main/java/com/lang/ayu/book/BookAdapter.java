package com.lang.ayu.book;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public  View getView(int position , View convertView, ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item,parent,false);
        }
        Book currentBook = getItem(position);
        TextView nameView = (TextView) listItem.findViewById(R.id.name);
        nameView.setText(currentBook.getName());
        TextView authorView = (TextView) listItem.findViewById(R.id.author);
        authorView.setText(currentBook.getmAuthor());
        ImageView imgView = (ImageView) listItem.findViewById(R.id.img1);
        Uri bookUri = Uri.parse(currentBook.geturl());
        Picasso.with(getContext()).load(bookUri).into(imgView);

        return listItem;
    }

}

