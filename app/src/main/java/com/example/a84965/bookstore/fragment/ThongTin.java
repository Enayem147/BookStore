package com.example.a84965.bookstore.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.activity.BookDetailActivity;
import com.example.a84965.bookstore.model.Sach;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class ThongTin extends Fragment {
    private DatabaseReference mDatabase;

    public ThongTin() {
    }

    TextView txtTG, txtNhaXB, txtNamXB, txtSoTrang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_tin, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtTG = view.findViewById(R.id.txt_tt_tacgia);
        txtNhaXB = view.findViewById(R.id.txt_tt_nhaxb);
        txtNamXB = view.findViewById(R.id.txt_tt_namxb);
        txtSoTrang = view.findViewById(R.id.txt_tt_sotrang);

        // Lấy thông tin sách từ trang BookDetail
        final BookDetailActivity activity = (BookDetailActivity) getActivity();
        final Sach sach = activity.getThongTinSach();

        txtNamXB.setText(sach.getSach_NamXB() + "");
        txtSoTrang.setText(sach.getSach_SoTrang() + "");

        //Lấy tên nhà xuất bản
        txtNhaXB.setText(activity.getNhaXB());

        //Lấy tên tác giả
        List<String> listTG = activity.getTacGia();
        String strTG = "";
        strTG = listTG.get(0);
        for (int i = 1; i < listTG.size(); i++) {
            strTG += " , " + listTG.get(i);
        }
        txtTG.setText(strTG);

        return view;
    }


}
