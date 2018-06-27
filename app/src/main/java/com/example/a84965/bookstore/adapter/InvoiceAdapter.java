package com.example.a84965.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.GioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvoiceAdapter extends BaseAdapter {

    ArrayList<GioHang> list;
    Context context;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public InvoiceAdapter(ArrayList<GioHang> list, Context context) {
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
        TextView txtTenSach,txtDonGia,txtSoLuong;
        ImageView imgHinhAnh;
        LayoutInflater inflater  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_invoice,null);
        txtTenSach = convertView.findViewById(R.id.txtInvoice_TenSach);
        txtSoLuong = convertView.findViewById(R.id.txtInvoice_SoLuong);
        imgHinhAnh = convertView.findViewById(R.id.imgInvoice_HinhSach);

        final GioHang gioHang = (GioHang)getItem(position);
        txtSoLuong.setText(gioHang.getSach_SL()+"");
        txtTenSach.setText(gioHang.getSach_Ten());
        Picasso.get()
                .load(gioHang.getSach_HinhAnh())
                .into(imgHinhAnh);
        return convertView;
    }
}
