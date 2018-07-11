package com.example.a84965.bookstore.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.InvoiceAdapter;
import com.example.a84965.bookstore.model.Kho;
import com.example.a84965.bookstore.model.LichSu;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InvoiceActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    Handler handler;
    TextView txtTotal, txtMaHD, txtTen, txtSDT, txtDiaChi;
    ListView listView;
    Toolbar toolbar;
    Button btnDatHang;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("E dd.MM.yyy 'lúc' k:mm ");
    String ngayDat;
    ArrayList<Integer> listSoLuong = new ArrayList<>();
    ArrayList<String> listKey = new ArrayList<>();
    boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__invoice);
        callControls();
        initCartReview();
        initInvoice();
        getKeynSoLuong();
        clickButtonDatHang();
    }

    /**
     * Lấy khóa(Firebase) và số lượng của Sách từ Kho
     */
    private void getKeynSoLuong() {
        mDatabase.child("Kho").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Kho kho = dataSnapshot.getValue(Kho.class);
                for (int i = 0; i < HomePage.gioHang.size(); i++) {
                    if (HomePage.gioHang.get(i).getSach_Ma().equals(kho.getSach_Ma())) {
                        listSoLuong.add(kho.getKho_SoLuong());
                        listKey.add(dataSnapshot.getKey());
                    }
                    super.onChildAdded(dataSnapshot, s);
                }
            }
        });
    }

    /**
     * Event click nút Đặt Hàng
     */
    private void clickButtonDatHang() {
        ngayDat = sdf.format(date);

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < HomePage.gioHang.size(); i++) {
                    if(HomePage.gioHang.get(i).getSach_SL() > listSoLuong.get(i)){
                        error = true;
                    }
                }
                final ProgressDialog progressDialog = new ProgressDialog(InvoiceActivity.this);
                progressDialog.setMessage("Chờ trong giây lát !!!");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (error){
                            progressDialog.dismiss();
                            Toast.makeText(InvoiceActivity.this, "Có lỗi xảy ra !! Xin kiểm tra lại giỏ hàng", Toast.LENGTH_SHORT).show();
                        }else{
                            for (int i = 0; i < HomePage.gioHang.size(); i++) {
                                //update số lượng sản phẩm
                                DatabaseReference updateKho = FirebaseDatabase.getInstance().getReference("Kho").child(listKey.get(i));
                                updateKho.setValue(new Kho(HomePage.gioHang.get(i).getSach_Ma(),listSoLuong.get(i) - HomePage.gioHang.get(i).getSach_SL()));
                                //thêm giỏ hàng vào lịch sử mua hàng
                                LichSu lichSu = new LichSu(txtMaHD.getText().toString(),
                                        HomePage.gioHang.get(i).getSach_Ma(),
                                        HomePage.gioHang.get(i).getSach_Ten(),
                                        HomePage.gioHang.get(i).getSach_HinhAnh(),
                                        HomePage.gioHang.get(i).getSach_DonGia(),
                                        HomePage.gioHang.get(i).getSach_SL(),
                                        ngayDat);
                                mDatabase.child("LichSu").child(HomePage.KH_SDT).push().setValue(lichSu);
                            }
                            // xóa giỏ hàng
                            DatabaseReference dataCart = FirebaseDatabase.getInstance().getReference("GioHang").child(HomePage.KH_SDT);
                            dataCart.removeValue();

                            HomePage.gioHang.clear();
                            HomePage.isUserOrder = true;
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            intent.putExtra("MaHD", txtMaHD.getText());
                            intent.putExtra("NgayLap", ngayDat);
                            startActivity(intent);
                        }

                    }
                }, 3000);
            }
        });
    }

    /**
     * Khởi tạo giá trị đầu vào
     */
    private void callControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        handler = new Handler();
        listView = findViewById(R.id.listView_Invoice);
        toolbar = findViewById(R.id.toolbar_Invoice);
        btnDatHang = findViewById(R.id.btnOrder_DatHang);

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

    /**
     * Khởi tạo hóa đơn
     */
    private void initInvoice() {
        txtMaHD.setText("SBS" + calendar.getTimeInMillis());
        txtSDT.setText(HomePage.KH_SDT);
        txtTen.setText(HomePage.khachHang.getKH_HoTen().toUpperCase());
        txtDiaChi.setText(HomePage.khachHang.getKH_DiaChi());
    }

    /**
     * Khởi tạo review giỏ hàng
     */
    private void initCartReview() {
        final InvoiceAdapter _invoiceAdapter;
        _invoiceAdapter = new InvoiceAdapter(HomePage.gioHang, getApplicationContext());
        listView.setAdapter(_invoiceAdapter);
        _invoiceAdapter.notifyDataSetChanged();
        txtTotal.setText(decimalFormat.format(CartActivity.invoice_tongtien) + " đ");
    }
}
