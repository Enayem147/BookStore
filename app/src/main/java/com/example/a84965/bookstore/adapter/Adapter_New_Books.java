package com.example.a84965.bookstore.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.a84965.bookstore.activity.Activity_Book_Detail;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.TacGia;
import com.example.a84965.bookstore.model.TacGiaChiTiet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_New_Books extends RecyclerView.Adapter<Adapter_New_Books.ViewHolder>  {

    ArrayList<Sach> list;
    Activity context;
    private DatabaseReference mDatabase;
    public Adapter_New_Books(ArrayList<Sach> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.layout_new_books,parent,false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtTen.setText(list.get(position).getSach_Ten());

    //Lấy tên tác giả
        final List<String> listTG = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("TacGiaChiTiet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final TacGiaChiTiet tacGiaChiTiet = dataSnapshot.getValue(TacGiaChiTiet.class);
                if(tacGiaChiTiet.getSach_Ma().equals(list.get(position).getSach_Ma())){
                    mDatabase.child("TacGia").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            TacGia tacGia = dataSnapshot.getValue(TacGia.class);
                            if(tacGia.getTG_Ma().equals(tacGiaChiTiet.getTG_Ma())){
                                // lấy danh sách đồng tác giả
                                listTG.add(tacGia.getTG_Ten());
                                String strTG = "";
                                    strTG = listTG.get(0);
                                    for(int i=1;i<listTG.size();i++){
                                        strTG +="\n"+listTG.get(i);
                                    }
                                    holder.txtTG.setText(strTG);
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    //Set img
        Picasso.get()
                .load(list.get(position).getSach_HinhAnh())
                .into(holder.imgHinh);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Activity_Book_Detail.class);
                intent.putExtra("Sach_Ma",list.get(position).getSach_Ma());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTen;
        TextView txtTG;
        ImageView imgHinh;
        LinearLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txt_NewBook_Ten);
            txtTG = itemView.findViewById(R.id.txt_NewBook_TG);
            imgHinh = itemView.findViewById(R.id.img_NewBook);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
