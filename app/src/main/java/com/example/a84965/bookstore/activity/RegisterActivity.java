package com.example.a84965.bookstore.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
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
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.example.a84965.bookstore.ultil.OnTextChangeListener;
import com.google.firebase.database.DataSnapshot;
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
    EditText txtSDT, txtTen, txtMK, txtDiaChi;
    Button btnDangKy;
    Toolbar toolbar;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);
        callControls();
        register();
        allTextEvent();

    }

    /**
     * Tất cả event drawable click ở tất cả EditText
     */
    @SuppressLint("ClickableViewAccessibility")
    private void allTextEvent() {
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
                if (s.length() == 0) {
                    txtSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    txtSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);
                }
            }
        });


        //txtMK
        txtMK.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtMK) {
            @Override
            public boolean onDrawableClick() {
                if (txtMK.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    txtMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
                    txtMK.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                } else {
                    txtMK.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_visible, 0);
                }
                return true;
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
                if (s.length() == 0) {
                    txtTen.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    txtTen.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);
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
                if (s.length() == 0) {
                    txtDiaChi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    txtDiaChi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);
                }
            }
        });


        //init drawable invisible
        txtSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txtTen.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txtDiaChi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }

    /**
     * Event click nút Đăng ký
     */
    private void register() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdt = txtSDT.getText().toString();
                mk = txtMK.getText().toString();
                ten = txtTen.getText().toString();
                diachi = txtDiaChi.getText().toString();

                //bắt lỗi tồn tại sdt
                final boolean[] exist = {false};
                mDatabase.child("KhachHang").addChildEventListener(new GetChildFireBase() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                        if(khachHang.getKH_SDT().equals(sdt)){
                            exist[0] = true;
                        }
                        super.onChildAdded(dataSnapshot, s);
                    }
                });

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Chờ trong giây lát !!!");
                progressDialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean blank = false;
                        boolean error = false;
                        // bắt lỗi rỗng , hợp lệ
                        String strErr = "";
                        if (sdt.length() == 0 || mk.length() == 0 || ten.length() == 0 || diachi.length() == 0) {
                            blank = true;
                        }

                        int i = 0;
                        if (!checkPhone(sdt)) {
                            if(++i == 1){
                                strErr+="Số điện thoại không hợp lệ";
                            }else{
                                strErr+="\nSố điện thoại không hợp lệ";
                            }
                            error = true;
                        }
                        if (mk.length() < 6) {
                            if(++i == 1){
                                strErr+="Độ dài mật khẩu phải trên 6 ký tự";
                            }else{
                                strErr+="\nĐộ dài mật khẩu phải trên 6 ký tự";
                            }

                            error = true;
                        }

                        // kiểm tra
                        if (blank) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else if (error) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, strErr, Toast.LENGTH_SHORT).show();
                        }else if(exist[0]){
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            KhachHang khachHang = new KhachHang(sdt, mk, ten, diachi);
                            mDatabase.child("KhachHang").push().setValue(khachHang);
                            HomePage.new_SDT = sdt;
                            HomePage.isNewUser = true;
                            finish();
                        }
                    }
                },1500);

            }
        });
    }

    /**
     * Kiểm tra số điện thoại ( 09 - 10 số , 01 - 11 số , chỉ có thể là số )
     * @param number : Số điện thoại
     * @return
     */
    private boolean checkPhone(String number) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return false;
        } else if (number.length() == 10 || number.length() == 11) {
            if (number.length() == 10) {
                if (number.substring(0, 2).equals("09")) {
                    return true;
                } else {
                    return false;
                }
            } else if (number.substring(0, 2).equals("01")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void callControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        handler = new Handler();
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
