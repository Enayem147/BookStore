package com.example.a84965.bookstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.NhaXuatBan;
import com.example.a84965.bookstore.model.TacGia;
import com.example.a84965.bookstore.model.TacGiaChiTiet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Books extends BaseAdapter {

    Context context;
    ArrayList<Sach> list;
    private DatabaseReference mDatabase;
    public Adapter_Books(Context context, ArrayList<Sach> list) {
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
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        convertView = inflater.inflate(R.layout.list_view_books,null);
        ImageView imgBooks = convertView.findViewById(R.id.imgBooks);
        TextView txt_Ten = convertView.findViewById(R.id.txtBooks_Ten);
        final TextView txt_TG = convertView.findViewById(R.id.txtBooks_TG);
        final TextView txt_NXB = convertView.findViewById(R.id.txtBooks_NXB);
        TextView txt_Gia = convertView.findViewById(R.id.txtBooks_Gia);
        final Sach sach = (Sach) getItem(position);
        txt_Ten.setText(sach.getSach_Ten());

        //Lấy tên nhà xuất bản
        mDatabase.child("NhaXuatBan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NhaXuatBan nxb = dataSnapshot.getValue(NhaXuatBan.class);
                if(nxb.getNXB_Ma().equals(sach.getNXB_Ma())){
                    txt_NXB.setText(nxb.getNXB_Ten());
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

        //Lấy tên tác giả
        final List<String> listTG = new ArrayList<>();
        mDatabase.child("TacGiaChiTiet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final TacGiaChiTiet tacGiaChiTiet = dataSnapshot.getValue(TacGiaChiTiet.class);
                if(tacGiaChiTiet.getSach_Ma().equals(sach.getSach_Ma())){
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
                                    strTG +=" , "+listTG.get(i);
                                }
                                txt_TG.setText(strTG);
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

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txt_Gia.setText(decimalFormat.format(sach.getSach_DonGia()));

        Picasso.get()
                .load(sach.getSach_HinhAnh())
                .into(imgBooks);

        return convertView;
    }
}
