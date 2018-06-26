package com.example.a84965.bookstore.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.Adapter_Invoice;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.model.LichSu;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InvoiceActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView txtTotal , txtMaHD , txtTen , txtSDT , txtDiaChi;
    ListView listView;
    Toolbar toolbar;
    Button btnThanhToan;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat ("E dd.MM.yyy 'lúc' k:mm ");
    String ngayDat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__invoice);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        callControls();
        initCartReview();
        initInvoice();
        ButtonThanhToanClick();
    }

    private void ButtonThanhToanClick(){
        ngayDat = sdf.format(date);
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < HomePage.gioHang.size(); i++) {
                    LichSu lichSu = new LichSu(txtMaHD.getText().toString(),
                            HomePage.gioHang.get(i).getSach_Ma(),
                            HomePage.gioHang.get(i).getSach_Ten(),
                            HomePage.gioHang.get(i).getSach_HinhAnh(),
                            HomePage.gioHang.get(i).getSach_DonGia(),
                            HomePage.gioHang.get(i).getSach_SL(),
                            ngayDat);
                    mDatabase.child("LichSu").child(HomePage.KH_SDT).push().setValue(lichSu);
                }

                DatabaseReference dataCart = FirebaseDatabase.getInstance().getReference("GioHang").child(HomePage.KH_SDT);
                dataCart.removeValue();

                HomePage.gioHang.clear();

                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void callControls() {
        listView = findViewById(R.id.listView_Invoice);
        toolbar = findViewById(R.id.toolbar_Invoice);
        btnThanhToan = findViewById(R.id.btnOrder_DatHang);

        txtTotal = findViewById(R.id.txtInvoice_Total);
        txtMaHD = findViewById(R.id.txtInv_MaHD);
        txtSDT = findViewById(R.id.txtInv_SDT);
        txtTen = findViewById(R.id.txtInv_Ten);
        txtDiaChi = findViewById(R.id.txtInv_DiaChi);


        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        setTitle("ĐƠN ĐẶT HÀNG");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initInvoice(){
        txtMaHD.setText("SBS"+calendar.getTimeInMillis());
        txtSDT.setText(HomePage.KH_SDT);

        txtTen.setText(HomePage.khachHang.getKH_HoTen().toUpperCase());
        txtDiaChi.setText(HomePage.khachHang.getKH_DiaChi());
    }

    private void initCartReview() {
        final Adapter_Invoice adapter_invoice;
        adapter_invoice = new Adapter_Invoice(HomePage.gioHang,getApplicationContext());
        listView.setAdapter(adapter_invoice);
        adapter_invoice.notifyDataSetChanged();
        txtTotal.setText(decimalFormat.format(CartActivity.invoice_tongtien) + " đ");
    }
}
