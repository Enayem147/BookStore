package com.example.a84965.bookstore.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        initCart();
        setTotal();
        AllButtonClick();
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
                    Intent intentInvoice = new Intent(getApplicationContext(), InvoiceActivity.class);
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
            _cartAdapter = new CartAdapter(HomePage.gioHang, this);
            txtEmpty.setVisibility(View.INVISIBLE);
            listViewCart.setVisibility(View.VISIBLE);
            txtCart1.setVisibility(View.VISIBLE);
            txtTotal.setVisibility(View.VISIBLE);
            listViewCart.setAdapter(_cartAdapter);
            _cartAdapter.notifyDataSetChanged();
        }
    }


}
