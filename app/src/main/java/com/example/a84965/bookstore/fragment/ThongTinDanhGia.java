package com.example.a84965.bookstore.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.activity.BookDetailActivity;
import com.example.a84965.bookstore.adapter.ReviewsAdapter;

import com.example.a84965.bookstore.model.DanhGia;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ThongTinDanhGia extends Fragment {
    private DatabaseReference mDatabase;
    ReviewsAdapter reviewsAdapter;
    ListView listView;
    ArrayList<DanhGia> listDanhGia;
    public ThongTinDanhGia() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_gia, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listView = view.findViewById(R.id.listView_Reviews);
        final BookDetailActivity activity = (BookDetailActivity) getActivity();
        listDanhGia = activity.getReviews();
        reviewsAdapter = new ReviewsAdapter(getContext(),listDanhGia);
        listView.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();
        //load Reviews
        return view;
    }

}
