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
import com.example.a84965.bookstore.adapter.Adapter_Cart;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    private Adapter_Cart adapter_cart;
    Handler handler;
    ListView listViewCart;
    Toolbar toolbar;
    Button btnTiepTuc, btnThanhToan;
    static public TextView txtEmpty, txtCart1;
    static public TextView txtTotal;
    static long invoice_tongtien;
    private static DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cart);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        callControls();
        initCart();
        setTotal();
        AllButtonClick();
    }

    private void AllButtonClick(){
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static void setTotal() {
        long tongTien = 0;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        for (int i = 0; i < HomePage.gioHang.size(); i++) {
            tongTien += (HomePage.gioHang.get(i).getSach_SL() * HomePage.gioHang.get(i).getSach_DonGia());
            txtTotal.setText(decimalFormat.format(tongTien) +" đ");
        }
        invoice_tongtien = tongTien;
    }

    private void callControls() {
        handler = new Handler();
        listViewCart = findViewById(R.id.listView_Cart);
        listViewCart.setVisibility(View.INVISIBLE);
        toolbar = findViewById(R.id.toolbar_Cart);
        btnTiepTuc = findViewById(R.id.btnCart_TiepTuc);
        btnThanhToan = findViewById(R.id.btnCart_ThanhToan);
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

    private void initCart() {
        if (HomePage.gioHang.size() > 0) {
            adapter_cart = new Adapter_Cart(HomePage.gioHang, this);
            txtEmpty.setVisibility(View.INVISIBLE);
            listViewCart.setVisibility(View.VISIBLE);
            txtCart1.setVisibility(View.VISIBLE);
            txtTotal.setVisibility(View.VISIBLE);
            listViewCart.setAdapter(adapter_cart);
            adapter_cart.notifyDataSetChanged();
        }
    }


}
