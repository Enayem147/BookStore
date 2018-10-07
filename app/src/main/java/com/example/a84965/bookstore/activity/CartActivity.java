package com.example.a84965.bookstore.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private CartAdapter _cartAdapter;
    public static boolean isUserOrder = false;
    public static String maHD = "";
    public static String ngayLapHD = "";
    Handler handler;
    ListView listViewCart;
    Toolbar toolbar;
    Button btnTiepTuc, btnDatHang;
    static public TextView txtEmpty, txtCart1;
    static public TextView txtTotal;
    static long invoice_tongtien;
    private static DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cart);
        callControls();
        setTotal();
        AllButtonClick();
        initCart();
    }

    @Override
    protected void onResume() {
        DialogCompleteOrder();
        super.onResume();
    }

    private void DialogCompleteOrder() {
        if (isUserOrder) {
            CartActivity.txtEmpty.setVisibility(View.VISIBLE);
            CartActivity.txtCart1.setVisibility(View.INVISIBLE);
            CartActivity.txtTotal.setVisibility(View.INVISIBLE);
            if(!maHD.equals("") && !ngayLapHD.equals("")){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog dialog = new Dialog(CartActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_complete_invoice);
                        TextView txtMaHD = dialog.findViewById(R.id.txtInvoiceComp_MaHD);
                        TextView txtNgayLap = dialog.findViewById(R.id.txtInvoiceComp_NgayLap);
                        txtMaHD.setText(maHD);
                        txtNgayLap.setText(ngayLapHD);
                        dialog.show();
                        isUserOrder = false;
                    }
                },1500);

            }
        }
    }

    /**
     * Event khi click vào nút Tiếp Tục Mua Hàng và Đặt Hàng
     */
    private void AllButtonClick(){
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomePage.gioHang.size() > 0) {
                    Intent intentInvoice = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(intentInvoice);
                } else {
                    Toast.makeText(CartActivity.this, "Bạn không có hàng để thanh toán !!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Hàm set giá trị cho text Tổng Tiền
     */
    public static void setTotal() {
        long tongTien = 0;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        for (int i = 0; i < HomePage.gioHang.size(); i++) {
            tongTien += (HomePage.gioHang.get(i).getSach_SL() * HomePage.gioHang.get(i).getSach_DonGia());
            txtTotal.setText(decimalFormat.format(tongTien) +" đ");
        }
        invoice_tongtien = tongTien;
    }

    /**
     * Khởi tạo giá trị đầu vào
     */
    private void callControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        handler = new Handler();
        listViewCart = findViewById(R.id.listView_Cart);
        listViewCart.setVisibility(View.INVISIBLE);
        toolbar = findViewById(R.id.toolbar_Cart);
        btnTiepTuc = findViewById(R.id.btnCart_TiepTuc);
        btnDatHang = findViewById(R.id.btnCart_DatHang);
        txtEmpty = findViewById(R.id.txt_emptyCart);
        txtTotal = findViewById(R.id.txtCart_TongTien);
        txtCart1 = findViewById(R.id.txtCart1);
        txtCart1.setVisibility(View.INVISIBLE);
        txtTotal.setVisibility(View.INVISIBLE);

        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        setTitle("Giỏ hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Khởi tạo Giỏ Hàng
     */
    private void initCart() {
        if(HomePage.gioHang == null)
            HomePage.gioHang = new ArrayList<>();
        if (HomePage.gioHang.size() > 0) {
            _cartAdapter = new CartAdapter(HomePage.gioHang,HomePage.listSoLuongKho, this);
            txtEmpty.setVisibility(View.INVISIBLE);
            listViewCart.setVisibility(View.VISIBLE);
            txtCart1.setVisibility(View.VISIBLE);
            txtTotal.setVisibility(View.VISIBLE);
            listViewCart.setAdapter(_cartAdapter);
            _cartAdapter.notifyDataSetChanged();
        }else{
            CartActivity.txtEmpty.setVisibility(View.VISIBLE);
            CartActivity.txtCart1.setVisibility(View.INVISIBLE);
            CartActivity.txtTotal.setVisibility(View.INVISIBLE);
        }
    }


}
