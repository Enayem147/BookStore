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
import com.example.a84965.bookstore.activity.Activity_Book_Detail;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GioiThieu extends Fragment {
    private DatabaseReference mDatabase;
    public GioiThieu() {
    }
    TextView txtGioiThieu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gioi_thieu,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txtGioiThieu = view.findViewById(R.id.txtGioithieu);
        txtGioiThieu.setMovementMethod(new ScrollingMovementMethod());

        final Activity_Book_Detail activity = (Activity_Book_Detail)getActivity();
        Sach sach = activity.getThongTinSach();
        txtGioiThieu.setText(sach.getSach_GioiThieu());
        return  view;
    }


}
