package com.example.a84965.bookstore.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.FeedbackAdapter;
import com.example.a84965.bookstore.model.DanhGia;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.ultil.DrawableClickListener;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.example.a84965.bookstore.ultil.OnTextChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackActivity extends AppCompatActivity {
    private boolean isLogin = false;
    SharedPreferences sharedPreferences;
    Handler handler;
    Button btnFeedback;
    Toolbar toolbar;
    FeedbackAdapter feedbackAdapter;
    ArrayList<DanhGia> listDanhGia;
    ImageView imgSach;
    TextView txtTen;
    ListView listViewFeedback;
    String tieuDe= "";
    private String sachMa = "";
    private static DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        callControls();
        initToolBar();
        initBook();
        initFeedBackList();
        eventButtonsClick();
    }

    private void eventButtonsClick() {
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomePage.KH_SDT == null || HomePage.KH_SDT.equals("")){
                    DialogDangNhap();
                }else{
                    getDialogDanhGia();
                }
            }
        });
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbar_Feedback);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Đánh giá sản phẩm");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initFeedBackList() {
        listDanhGia = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(this,listDanhGia);
        listViewFeedback.setAdapter(feedbackAdapter);
        mDatabase.child("DanhGia").child(sachMa).addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final DanhGia danhGia = dataSnapshot.getValue(DanhGia.class);
                listDanhGia.add(danhGia);
                feedbackAdapter.notifyDataSetChanged();
                super.onChildAdded(dataSnapshot, s);
            }
        });
    }

    private void initBook() {
        Intent intent = getIntent();
        String ten = intent.getStringExtra("sach_Ten");
        String hinhAnh = intent.getStringExtra("sach_HinhAnh");
        sachMa = intent.getStringExtra("sach_Ma");
        txtTen.setText(ten);
        Picasso.get()
                .load(hinhAnh)
                .into(imgSach);
}

    private void callControls() {
        sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        handler = new Handler();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgSach = findViewById(R.id.imgFeedback);
        txtTen = findViewById(R.id.txtFeedBack_Ten);
        listViewFeedback = findViewById(R.id.listView_Feedback);
        btnFeedback = findViewById(R.id.btnFeedback_Send);
    }

    private void getDialogDanhGia() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reviews);
        final TextView txtRating = dialog.findViewById(R.id.txtDanhGia_Rating);
        final EditText txtTieuDe = dialog.findViewById(R.id.txtDanhGia_TieuDe);
        txtTieuDe.setText("");
        final EditText txtNoiDung = dialog.findViewById(R.id.txtDanhGia_NoiDung);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBarReview);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating){
                    case 1:
                        txtRating.setText("Cực kì không hài lòng");
                        break;
                    case 2:
                        txtRating.setText("Không hài lòng");
                        break;
                    case 3:
                        txtRating.setText("Bình thường");
                        break;
                    case 4:
                        txtRating.setText("Hài lòng");
                        break;
                    case 5:
                        txtRating.setText("Rất hài lòng");
                        break;
                }
            }
        });
        Button btnGuiDanhGia = dialog.findViewById(R.id.btnGuiDanhGia);
        btnGuiDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float xepHang = ratingBar.getRating();
                tieuDe = txtTieuDe.getText().toString();
                if(tieuDe.equals("")){
                    tieuDe = txtRating.getText().toString();
                }
                if(xepHang == 0.0){
                    Toast.makeText(FeedbackActivity.this, "Vui lòng đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
                }else if (txtNoiDung.getText().toString().equals("") || txtNoiDung.getText().toString() == null){
                    Toast.makeText(FeedbackActivity.this, "Nội dụng đánh giá không được rỗng", Toast.LENGTH_SHORT).show();
                }else{
                    DanhGia danhGia = new DanhGia(HomePage.KH_SDT,tieuDe,txtNoiDung.getText().toString(),xepHang);
                    mDatabase.child("DanhGia").child(sachMa).push().setValue(danhGia);
                    Toast.makeText(FeedbackActivity.this, "Cảm ơn quý khách đã đánh giá sản phẩm của chúng tôi", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    /**
     * Hiển thị Dialog Đăng nhập
     */
    @SuppressLint("ClickableViewAccessibility")
    public void DialogDangNhap(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        final TextView txtSDT = dialog.findViewById(R.id.txtLogin_SDT);
        final TextView txtMK = dialog.findViewById(R.id.txtLogin_MK);
        final TextView txtDangKy = dialog.findViewById(R.id.txtLogin_Register);
        txtDangKy.setPaintFlags(txtDangKy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //event Drawable click txtSDT
        txtSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);

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

        txtSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        txtMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
        //event Drawable click txtMK
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

        //event click nút đăng ký nhanh
        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogRes = new Dialog(FeedbackActivity.this);
                dialogRes.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogRes.setContentView(R.layout.dialog_register);
                final TextView txtResSDT = dialogRes.findViewById(R.id.txtDialogRegister_SDT);
                final TextView txtResMK = dialogRes.findViewById(R.id.txtDialogRegister_MK);
                Button btnRegister = dialogRes.findViewById(R.id.txtDialogRegister);

                //event Drawable click txtResSDT
                txtResSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);
                txtResSDT.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtResSDT) {
                    @Override
                    public boolean onDrawableClick() {
                        txtResSDT.setText("");
                        return true;
                    }
                });

                txtResSDT.addTextChangedListener(new OnTextChangeListener() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            txtResSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        } else {
                            txtResSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);
                        }
                    }
                });

                txtResSDT.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                txtResMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
                //event Drawable click txtResMK
                txtResMK.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtMK) {
                    @Override
                    public boolean onDrawableClick() {
                        if (txtResMK.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            txtResMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
                            txtResMK.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        } else {
                            txtResMK.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            txtResMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_visible, 0);
                        }
                        return true;
                    }
                });

                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sdt = txtResSDT.getText().toString();
                        final String mk = txtResMK.getText().toString();

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

                        final ProgressDialog progressDialog = new ProgressDialog(FeedbackActivity.this);
                        progressDialog.setMessage("Chờ trong giây lát !!!");
                        progressDialog.show();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                boolean blank = false;
                                boolean error = false;
                                // bắt lỗi rỗng , hợp lệ
                                String strErr = "";
                                if (sdt.length() == 0 || mk.length() == 0) {
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
                                    Toast.makeText(FeedbackActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                                } else if (error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(FeedbackActivity.this, strErr, Toast.LENGTH_SHORT).show();
                                }else if(exist[0]){
                                    progressDialog.dismiss();
                                    Toast.makeText(FeedbackActivity.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(FeedbackActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    KhachHang khachHang = new KhachHang(sdt, mk, "", "");
                                    mDatabase.child("KhachHang").push().setValue(khachHang);
                                    HomePage.new_SDT = sdt;
                                    txtSDT.setText(sdt);
                                    progressDialog.dismiss();
                                    dialogRes.dismiss();
                                }
                            }
                        },1500);
                    }
                });
                dialogRes.show();
            }
        });
        final CheckBox cbRemember = dialog.findViewById(R.id.cbLogin);
        cbRemember.setChecked(true);
        Button btnDangNhap = dialog.findViewById(R.id.btnDangNhap);
        // click vào nút Đăng Nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = false;
                //Kiểm tra SDT - MK khách hàng từ Firebase
                mDatabase.child("KhachHang").addChildEventListener(new GetChildFireBase() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                        if (kh.getKH_SDT().equals(txtSDT.getText().toString()) && kh.getKH_MK().equals(txtMK.getText().toString())) {
                            isLogin = true;
                            HomePage.linearLayoutMenuCusOrder.setEnabled(true);
                            HomePage.KH_Ten = kh.getKH_HoTen();
                            HomePage.KH_SDT = kh.getKH_SDT();
                            HomePage.initCart(kh.getKH_SDT());
                            HomePage.initHistory(kh.getKH_SDT());
                            HomePage.updateMenuTitles();
                            if (cbRemember.isChecked()) {
                                // lưu sdt và mk lại cho lần sau không cần đăng nhập
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("SDT", kh.getKH_SDT());
                                editor.putString("Ten", kh.getKH_HoTen());
                                editor.putString("MK", kh.getKH_MK());
                                editor.commit();
                            }
                        }
                        super.onChildAdded(dataSnapshot, s);
                    }
                });
                final ProgressDialog progressDialog = new ProgressDialog(FeedbackActivity.this);
                progressDialog.setMessage("Chờ trong giây lát !!!");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLogin) {
                            HomePage.getKhachHangProfile();
                            progressDialog.dismiss();
                            Toast.makeText(FeedbackActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(FeedbackActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3000);

            }
        });
        dialog.show();
    }

    /**
     * Kiểm tra số điện thoại ( 10 số ,  chỉ có thể là số )
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

}
