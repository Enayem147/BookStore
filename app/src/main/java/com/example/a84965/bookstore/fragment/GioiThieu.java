package com.example.a84965.bookstore.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.activity.BookDetailActivity;
import com.example.a84965.bookstore.model.Sach;


public class GioiThieu extends Fragment {
    public GioiThieu() {
    }
    TextView txtGioiThieu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gioi_thieu,container,false);
        txtGioiThieu = view.findViewById(R.id.txtGioithieu);
        txtGioiThieu.setMovementMethod(new ScrollingMovementMethod());
        // Lấy thông tin sách từ trang BookDetail
        final BookDetailActivity activity = (BookDetailActivity)getActivity();
        Sach sach = activity.getThongTinSach();
        txtGioiThieu.setText(sach.getSach_GioiThieu());
        return  view;
    }


}
