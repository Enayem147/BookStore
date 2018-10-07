package com.example.a84965.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.DonHang;

import java.util.ArrayList;

public class CustomerOrderAdapter extends BaseAdapter {
    ArrayList<DonHang> list;
    Context context;

    public CustomerOrderAdapter(ArrayList<DonHang> list, Context context) {
        this.list = list;
        this.context = context;
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
        TextView txtMaDH,txtThoiGian,txtTrangThai;
        LayoutInflater inflater  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_customer_order,null);
        txtMaDH = convertView.findViewById(R.id.txtCusOrder_DH);
        txtThoiGian = convertView.findViewById(R.id.txtCusOrder_NgayMua);
        txtTrangThai = convertView.findViewById(R.id.txtCusOrder_TrangThai);

        final DonHang donHang = (DonHang) getItem(position);
        txtMaDH.setText(donHang.getDH_Ma());
        txtThoiGian.setText(donHang.getDH_NgayDat());
        txtTrangThai.setText("Đang giao hàng");


        return convertView;
    }
}
