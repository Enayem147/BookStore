package com.example.a84965.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.LichSu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    ArrayList<LichSu> list ;
    Context context;

    public HistoryAdapter(ArrayList<LichSu> list, Context context) {
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
        final TextView txtTenSach, txtSoLuong, txtMaHD , txtNgayMua;
        ImageView imgHinhAnh;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_view_history, null);
        txtTenSach = convertView.findViewById(R.id.txtHistory_TenSach);
        txtSoLuong = convertView.findViewById(R.id.txtHistory_SL);
        txtMaHD = convertView.findViewById(R.id.txtHistory_MaHD);
        txtNgayMua = convertView.findViewById(R.id.txtHistory_NgayMua);
        imgHinhAnh = convertView.findViewById(R.id.imgHistory_HinhSach);

        // khởi tạo các giá trị cho list view Lịch Sử
        LichSu lichSu = (LichSu) getItem(position);
        txtSoLuong.setText(lichSu.getSach_SL() + "");
        txtTenSach.setText(lichSu.getSach_Ten());
        txtMaHD.setText(lichSu.getHD_Ma());
        txtNgayMua.setText(lichSu.getLichSu_NgayDat());

        Picasso.get()
                .load(lichSu.getSach_HinhAnh())
                .into(imgHinhAnh);

        return convertView;
    }
}
