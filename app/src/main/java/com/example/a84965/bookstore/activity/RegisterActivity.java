package com.example.a84965.bookstore.activity;


import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.ultil.DrawableClickListener;
import com.example.a84965.bookstore.ultil.OnTextChangeListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
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
        allTextViewEvent();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void allTextViewEvent() {
        //txtSDT

        txtSDT.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtSDT) {
            @Override
            public boolean onDrawableClick() {
                txtSDT.setText("");
                return true;
            }
        });

        txtSDT.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    txtSDT.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }else{
                    txtSDT.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_cancel,0);
                }
            }
        });


        //txtMK
        txtMK.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtMK) {
            @Override
            public boolean onDrawableClick() {
                txtMK.setText("");
                return true;
            }
        });

        txtMK.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    Toast.makeText(RegisterActivity.this, "Rỗng", Toast.LENGTH_SHORT).show();
                    txtMK.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }else{
                    txtMK.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_cancel,0);
                }
            }
        });

        //txtTen
        txtTen.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtTen) {
            @Override
            public boolean onDrawableClick() {
                txtTen.setText("");
                return true;
            }
        });

        txtTen.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    Toast.makeText(RegisterActivity.this, "Rỗng", Toast.LENGTH_SHORT).show();
                    txtTen.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }else{
                    txtTen.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_cancel,0);
                }
            }
        });

        //txtDiaChi
        txtDiaChi.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtDiaChi) {
            @Override
            public boolean onDrawableClick() {
                txtDiaChi.setText("");
                return true;
            }
        });

        txtDiaChi.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    Toast.makeText(RegisterActivity.this, "Rỗng", Toast.LENGTH_SHORT).show();
                    txtDiaChi.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }else{
                    txtDiaChi.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_cancel,0);
                }
            }
        });




        //init drawable invisible
        txtSDT.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        txtTen.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        txtMK.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        txtDiaChi.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

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
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
