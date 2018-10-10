package com.example.a84965.bookstore.activity;

import android.app.ProgressDialog;
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
import com.example.a84965.bookstore.model.DonHang;
import com.example.a84965.bookstore.model.Kho;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderActivity extends AppCompatActivity {
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
    boolean blank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__order);
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
                final String sdt = txtSDT.getText().toString();
                String diachi = txtDiaChi.getText().toString();
                String hoten = txtTen.getText().toString();
                for (int i = 0; i < HomePage.gioHang.size(); i++) {
                    if (HomePage.gioHang.get(i).getSach_SL() > listSoLuong.get(i)) {
                        error = true;
                    }
                }

                if (sdt.length() == 0 || hoten.length() == 0 || diachi.length() == 0) {
                    blank = true;
                }


                final ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this);
                progressDialog.setMessage("Chờ trong giây lát !!!");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (error) {
                            progressDialog.dismiss();
                            Toast.makeText(OrderActivity.this, "Có sản phẩm vượt quá số lượng trong kho!", Toast.LENGTH_SHORT).show();
                        } else if (blank) {
                            progressDialog.dismiss();
                            Toast.makeText(OrderActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else if (!checkPhone(sdt)) {
                            progressDialog.dismiss();
                            Toast.makeText(OrderActivity.this, "Số điện thoại không phù hợp", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < HomePage.gioHang.size(); i++) {
                                //update số lượng sản phẩm
                                DatabaseReference updateKho = FirebaseDatabase.getInstance().getReference("Kho").child(listKey.get(i));
                                updateKho.setValue(new Kho(HomePage.gioHang.get(i).getSach_Ma(), listSoLuong.get(i) - HomePage.gioHang.get(i).getSach_SL()));
                                //thêm giỏ hàng vào lịch sử mua hàng
                                DonHang donHang = new DonHang(txtMaHD.getText().toString(),
                                        HomePage.gioHang.get(i).getSach_Ma(),
                                        HomePage.gioHang.get(i).getSach_Ten(),
                                        HomePage.gioHang.get(i).getSach_HinhAnh(),
                                        HomePage.gioHang.get(i).getSach_DonGia(),
                                        HomePage.gioHang.get(i).getSach_SL(),
                                        ngayDat,1);
                                // trạng thái đơn hàng 1 : chưa giao , 2 : đang giao , 3 : đã giao , 4: tạm hoãn
                                mDatabase.child("DonHang").child(HomePage.KH_SDT).push().setValue(donHang);
                            }
                            // xóa giỏ hàng
                            DatabaseReference dataCart = FirebaseDatabase.getInstance().getReference("GioHang").child(HomePage.KH_SDT);
                            dataCart.removeValue();
                            HomePage.gioHang.clear();
                            // cập nhật lịch sử mua hàng
                            HomePage.initHistory(HomePage.KH_SDT);


                            CartActivity.maHD = txtMaHD.getText().toString();
                            CartActivity.ngayLapHD = ngayDat;
                            CartActivity.isUserOrder = true;
                            progressDialog.dismiss();
                            finish();
                        }

                    }
                }, 3000);
            }
        });
    }

    /**
     * Kiểm tra số điện thoại ( 10 số, chỉ có thể là số )
     *
     * @param number : Số điện thoại
     * @return
     */
    private boolean checkPhone(String number) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return false;
        } else if (number.length() == 10) {
            if (number.length() == 10) {
                if (number.substring(0, 2).equals("03") ||
                        number.substring(0, 2).equals("05") ||
                        number.substring(0, 2).equals("07") ||
                        number.substring(0, 2).equals("08") ||
                        number.substring(0, 2).equals("09")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Khởi tạo giá trị đầu vào
     */
    private void callControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        handler = new Handler();
        listView = findViewById(R.id.listView_Order);
        toolbar = findViewById(R.id.toolbarOrder);
        btnDatHang = findViewById(R.id.btnOrder_DatHang);

        txtTotal = findViewById(R.id.txtInvoice_Total);
        txtMaHD = findViewById(R.id.txtOrder_MaHD);
        txtSDT = findViewById(R.id.txtOrder_SDT);
        txtTen = findViewById(R.id.txtOrder_Ten);
        txtDiaChi = findViewById(R.id.txtOrder_DiaChi);


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
