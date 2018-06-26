package com.example.a84965.bookstore.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.KhachHang;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_Register extends AppCompatActivity {
    private DatabaseReference mDatabase;

    String sdt = "";
    String mk = "";
    String ten = "";
    String diachi = "";
    EditText txtSDT , txtTen , txtMK , txtDiaChi ;
    Button btnDangKy;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        callControls();
        register();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void register() {


        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdt = txtSDT.getText().toString();
                mk = txtMK.getText().toString();
                ten = txtTen.getText().toString();
                diachi = txtDiaChi.getText().toString();
                boolean error = false;
                if(sdt.length() == 0 ){
                    txtSDT.setError("Số điện thoại không được rỗng");
                    error = true;
                }else if (!checkPhone(sdt)){
                    txtSDT.setError("Số điện thoại không hợp lệ");
                    error = true;
                }else{
                    txtSDT.setError(null);
                }
                if(mk.length() == 0){
                    txtMK.setError("Mật khẩu không được rỗng");
                    error = true;
                }else if (mk.length() < 6){
                    txtMK.setError("Độ dài mật khẩu phải trên 6 ký tự");
                    error = true;
                }else{
                    txtMK.setError(null);
                }

                if(ten.length() == 0){
                    txtTen.setError("Họ tên không được rỗng");
                    error = true;
                }else{
                    txtTen.setError(null);
                }

                if(diachi.length() == 0){
                    txtDiaChi.setError("Địa chỉ không được rỗng");
                    error = true;
                }else{
                    txtDiaChi.setError(null);
                }

                if(!error){
                    Toast.makeText(Activity_Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    KhachHang khachHang = new KhachHang(sdt,mk,ten,diachi);
                    mDatabase.child("KhachHang").push().setValue(khachHang);
                    HomePage.new_SDT = sdt;
                    HomePage.new_MK = mk;
                    HomePage.isNewUser = true;
                    finish();
                }
            }
        });
    }

    private boolean checkPhone(String number){
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return false;
        } else
        if (number.length() == 10 || number.length() == 11) {
            if (number.length() == 10) {
                if (number.substring(0, 2).equals("09")) {
                    return true;
                } else {
                    return false;
                }
            } else
            if (number.substring(0, 2).equals("01")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void callControls() {
        txtSDT = findViewById(R.id.txtRes_SDT);
        txtMK = findViewById(R.id.txtRes_MK);
        txtTen = findViewById(R.id.txtRes_HoTen);
        txtDiaChi = findViewById(R.id.txtRes_DiaChi);
        btnDangKy = findViewById(R.id.btnRegister);
        toolbar = findViewById(R.id.toolbar_Register);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
    }
}
