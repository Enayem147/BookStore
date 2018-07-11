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
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.activity.CartActivity;
import com.example.a84965.bookstore.activity.HomePage;
import com.example.a84965.bookstore.model.GioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class CartAdapter extends BaseAdapter {
    ArrayList<GioHang> listGioHang;
    ArrayList<Integer> listSoLuongKho;
    Activity context;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    int soLuong = 1;

    public CartAdapter(ArrayList<GioHang> listGioHang, ArrayList<Integer> listSoLuongKho, Activity context) {
        this.listGioHang = listGioHang;
        this.listSoLuongKho = listSoLuongKho;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listGioHang.size();
    }

    @Override
    public Object getItem(int position) {
        return listGioHang.get(position);
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
        final int soLuongKho = listSoLuongKho.get(position);
        soLuong = gioHang.getSach_SL();
        // tăng số lượng 1 quyển sách trong giỏ hàng
        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soLuong = gioHang.getSach_SL();
                soLuong++;
                if(soLuong > soLuongKho){
                    soLuong = soLuongKho;
                }
                gioHang.setSach_SL(soLuong);
                txtSoLuong.setText(gioHang.getSach_SL() + "");
                txtDonGia.setText(decimalFormat.format(gioHang.getSach_DonGia() * gioHang.getSach_SL())+ " đ");
                CartActivity.setTotal();
            }
        });

        // giảm số lượng 1 quyển sách trong giỏ hàng
        btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soLuong = gioHang.getSach_SL();
                soLuong--;
                if (soLuong == 0) {
                    soLuong = 1;
                }
                gioHang.setSach_SL(soLuong);
                txtSoLuong.setText(gioHang.getSach_SL() + "");
                txtDonGia.setText(decimalFormat.format(gioHang.getSach_DonGia() * gioHang.getSach_SL()) + " đ");
                CartActivity.setTotal();
            }
        });

        // khởi tạo thông tin sách
        if(gioHang.getSach_SL() > soLuongKho){
            gioHang.setSach_SL(soLuongKho);
        }
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
                            HomePage.listSoLuongKho.remove(position);
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
