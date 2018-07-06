package com.example.a84965.bookstore.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.activity.CartActivity;
import com.example.a84965.bookstore.activity.HomePage;
import com.example.a84965.bookstore.model.GioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class CartAdapter extends BaseAdapter {
    ArrayList<GioHang> list;
    Activity context;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    int soluong = 1;

    public CartAdapter(ArrayList<GioHang> list, Activity context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView txtTenSach, txtDonGia, txtSoLuong;
        Button btnCong, btnTru;
        ImageView imgHinhAnh;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_view_cart, null);
        txtTenSach = convertView.findViewById(R.id.txtCart_TenSach);
        txtDonGia = convertView.findViewById(R.id.txtCart_DonGia);
        txtSoLuong = convertView.findViewById(R.id.txtCart_SoLuong);
        btnCong = convertView.findViewById(R.id.btnCart_Cong);
        btnTru = convertView.findViewById(R.id.btnCart_Tru);
        imgHinhAnh = convertView.findViewById(R.id.imgCart_HinhSach);

        final GioHang gioHang = (GioHang) getItem(position);
        soluong = gioHang.getSach_SL();
        // tăng số lượng 1 quyển sách trong giỏ hàng
        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong = gioHang.getSach_SL();
                soluong++;
                gioHang.setSach_SL(soluong);
                txtSoLuong.setText(gioHang.getSach_SL() + "");
                txtDonGia.setText(decimalFormat.format(gioHang.getSach_DonGia() * gioHang.getSach_SL())+ " đ");
                CartActivity.setTotal();
            }
        });

        // giảm số lượng 1 quyển sách trong giỏ hàng
        btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong = gioHang.getSach_SL();
                soluong--;
                if (soluong == 0) {
                    soluong = 1;
                }
                gioHang.setSach_SL(soluong);
                txtSoLuong.setText(gioHang.getSach_SL() + "");
                txtDonGia.setText(decimalFormat.format(gioHang.getSach_DonGia() * gioHang.getSach_SL()) + " đ");
                CartActivity.setTotal();
            }
        });

        // khởi tạo thông tin sách
        txtSoLuong.setText(gioHang.getSach_SL() + "");
        txtTenSach.setText(gioHang.getSach_Ten());
        txtDonGia.setText(decimalFormat.format(gioHang.getSach_DonGia() * gioHang.getSach_SL()) + " đ");
        Picasso.get()
                .load(gioHang.getSach_HinhAnh())
                .into(imgHinhAnh);

        // event long click - xóa phần tử trong giỏ hàng
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa quyển sách này không ? ");
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (HomePage.gioHang.size() <= 0) {
                            CartActivity.txtEmpty.setVisibility(View.VISIBLE);
                            CartActivity.txtCart1.setVisibility(View.INVISIBLE);
                            CartActivity.txtTotal.setVisibility(View.INVISIBLE);
                        } else {
                            HomePage.gioHang.remove(position);
                            notifyDataSetChanged();
                            CartActivity.setTotal();
                            if (HomePage.gioHang.size() <= 0) {
                                CartActivity.txtEmpty.setVisibility(View.VISIBLE);
                                CartActivity.txtCart1.setVisibility(View.INVISIBLE);
                                CartActivity.txtTotal.setVisibility(View.INVISIBLE);
                            } else {
                                CartActivity.txtEmpty.setVisibility(View.INVISIBLE);
                                notifyDataSetChanged();
                                CartActivity.setTotal();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });


        return convertView;
    }
}
