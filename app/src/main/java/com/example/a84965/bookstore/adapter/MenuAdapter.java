package com.example.a84965.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.TheLoai;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    Context context;
    ArrayList<TheLoai> list;

    public MenuAdapter(Context context, ArrayList<TheLoai> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtTheLoai;
        ImageView imgTheLoai;
        LayoutInflater inflater  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_menu,null);
        txtTheLoai = convertView.findViewById(R.id.txt_TheLoai);
        imgTheLoai = convertView.findViewById(R.id.imageView_TheLoai);
        // Khởi tạo từng thể loại cho listview TheLoai ( menu left )
        TheLoai theLoai = (TheLoai)getItem(position);
        txtTheLoai.setText(theLoai.getTL_Ten());
        Picasso.get()
                .load(theLoai.getTL_Anh())
                .into(imgTheLoai);


        return convertView;
    }
}
