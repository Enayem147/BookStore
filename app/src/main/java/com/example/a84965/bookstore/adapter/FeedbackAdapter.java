package com.example.a84965.bookstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.DanhGia;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FeedbackAdapter extends BaseAdapter {
    Context context;
    ArrayList<DanhGia> list;
    private DatabaseReference mDatabase;

    public FeedbackAdapter(Context context, ArrayList<DanhGia> list) {
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        convertView = inflater.inflate(R.layout.list_view_feedback, null);
        RatingBar ratingBarReviews = convertView.findViewById(R.id.lvReview_RatingBar);
        TextView txtTieuDe = convertView.findViewById(R.id.lvReview_TieuDe);
        TextView txtNoiDung = convertView.findViewById(R.id.lvReview_NoiDung);
        final TextView txtHoTen = convertView.findViewById(R.id.lvReview_HoTen);

        final DanhGia danhGia = (DanhGia) getItem(position);
        mDatabase.child("KhachHang").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                if(khachHang.getKH_SDT().equals(danhGia.getKH_SDT())){
                    if(khachHang.getKH_HoTen().equals("")){
                        txtHoTen.setText("Người dùng mới");
                    }else{
                        txtHoTen.setText(khachHang.getKH_HoTen());
                    }

                }
                super.onChildAdded(dataSnapshot, s);
            }
        });

        ratingBarReviews.setRating(danhGia.getDG_XepHang());
        txtTieuDe.setText(danhGia.getDG_ChuDe());
        txtNoiDung.setText(danhGia.getDG_NoiDung());
        return convertView;
    }
}
