package com.example.a84965.bookstore.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.FragmentPagerAdapter;
import com.example.a84965.bookstore.model.Kho;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.GioHang;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.ultil.DrawableClickListener;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.example.a84965.bookstore.ultil.OnTextChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookDetailActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    private Handler handler;
    private Sach sach = null;
    private String nhaXB = "";
    private List<String> listTG = new ArrayList<>();
    private String sachMa = "";
    private String sachHinh = "";
    private String sachTen = "";
    private int sachGia = 0 ;
    private int soLuongKho = 1;
    Button btnThem;
    ImageView imgHinh;
    TextView txtTen,txtGia,txtTrangThai;
    private DatabaseReference mDatabase;
    int soLuong = 1;
    private boolean isLogin = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__book__detail);
        callControls();
        initToolBar();
        initBookDetail();
        ButtonThemGioHangClicked();
        initBookStatus();
    }

    /**
     * Event khi click vào nút THÊM VÀO GIỎ HÀNG
     */
    private void ButtonThemGioHangClicked(){
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomePage.KH_SDT == null || HomePage.KH_SDT.equals("")){
                    DialogDangNhap();
                }else{
                    DialogThem();
                }
            }
        });
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
                final Dialog dialogRes = new Dialog(BookDetailActivity.this);
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

                        final ProgressDialog progressDialog = new ProgressDialog(BookDetailActivity.this);
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
                                    Toast.makeText(BookDetailActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                                } else if (error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(BookDetailActivity.this, strErr, Toast.LENGTH_SHORT).show();
                                }else if(exist[0]){
                                    progressDialog.dismiss();
                                    Toast.makeText(BookDetailActivity.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(BookDetailActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
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
                final ProgressDialog progressDialog = new ProgressDialog(BookDetailActivity.this);
                progressDialog.setMessage("Chờ trong giây lát !!!");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLogin) {
                            HomePage.getKhachHangProfile();
                            progressDialog.dismiss();
                            Toast.makeText(BookDetailActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(BookDetailActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
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

    /**
     * Khởi tạo các giá trị đầu vào
     */
    private void callControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        Intent intent = getIntent();
        sach = (Sach) intent.getSerializableExtra("Sach");
        nhaXB = intent.getStringExtra("NhaXuatBan");
        listTG = (ArrayList<String>) intent.getSerializableExtra("TacGia");

        txtTrangThai = findViewById(R.id.txtDetail_TrangThai);
        btnThem = findViewById(R.id.btnThem_Sach);
        txtTen = findViewById(R.id.txtDetail_Ten);
        txtGia = findViewById(R.id.txtDetail_Gia);
        imgHinh = findViewById(R.id.imgBook_Detail);
        handler = new Handler();
    }


    /**
     * Khởi tạo trạng thái số lượng trong kho của Sách
     */
    public void initBookStatus(){
        mDatabase.child("Kho").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Kho kho = dataSnapshot.getValue(Kho.class);
                if(kho.getSach_Ma().equals(sach.getSach_Ma())){
                    soLuongKho = kho.getKho_SoLuong();
                    if(kho.getKho_SoLuong() >= 5){
                        txtTrangThai.setText(R.string.con_hang);
                        txtTrangThai.setTextColor(Color.rgb(0,238,0));
                        btnThem.setEnabled(true);
                    }else if (kho.getKho_SoLuong() >= 1){
                        txtTrangThai.setText(R.string.sap_het_hang);
                        txtTrangThai.setTextColor(Color.rgb(220,216,0));
                        btnThem.setEnabled(true);
                    }else{
                        txtTrangThai.setText(R.string.het_hang);
                        txtTrangThai.setTextColor(Color.GRAY);
                        btnThem.setEnabled(false);
                    }
                }
                super.onChildAdded(dataSnapshot, s);
            }
        });
    }

    /**
     * Khởi tạo chi tiết Sách
     */
    public void initBookDetail(){
        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTen.setText(sach.getSach_Ten());
        txtGia.setText(decimalFormat.format(sach.getSach_DonGia()) +" đ");
        Picasso.get()
                .load(sach.getSach_HinhAnh())
                .into(imgHinh);

        sachMa = sach.getSach_Ma();
        sachTen = sach.getSach_Ten();
        sachGia = sach.getSach_DonGia();
        sachHinh = sach.getSach_HinhAnh();
    }


    /**
     * Hiển thị Dialog Thêm số lượng sách vào giỏ hàng
     */
    private void DialogThem() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cart);

        final TextView txtSoLuong = dialog.findViewById(R.id.txtNhap_SL);
        txtSoLuong.setText(soLuong +"");
        Button btnTru = dialog.findViewById(R.id.btnNhap_Tru);
        Button btnCong = dialog.findViewById(R.id.btnNhap_Cong);
        Button btnXacNhan = dialog.findViewById(R.id.btnNhap_XN);

        // event click nút +
        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soLuong++;
                if(soLuong > soLuongKho){
                    soLuong = soLuongKho;
                }
                txtSoLuong.setText(soLuong +"");
            }
        });
        //event click nút -
        btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soLuong--;
                if(soLuong <= 0){
                    soLuong =1;
                }
                txtSoLuong.setText(soLuong +"");
            }
        });
        //event click nút Xác nhận
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomePage.gioHang.size() == 0){
                    HomePage.gioHang.add(new GioHang(sachMa, sachTen, sachHinh, sachGia, soLuong));
                    HomePage.listSoLuongKho.add(soLuongKho);
                }else{
                    boolean exist = false;
                    for(int i=0;i<HomePage.gioHang.size();i++){
                        if(sachMa.equals(HomePage.gioHang.get(i).getSach_Ma())){
                            int sl = HomePage.gioHang.get(i).getSach_SL();
                            sl+= soLuong;
                            HomePage.gioHang.get(i).setSach_SL(sl);
                            exist = true;
                        }
                    }
                    if(!exist){
                        HomePage.gioHang.add(new GioHang(sachMa, sachTen, sachHinh, sachGia, soLuong));
                        HomePage.listSoLuongKho.add(soLuongKho);
                    }
                }
                Toast.makeText(BookDetailActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Tạo menu trên toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return true;
    }

    /**
     * Event click menu trên toolbar
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuDetail_Giohang:
                if(HomePage.KH_SDT == null || HomePage.KH_SDT.equals("")){
                    DialogDangNhap();
                }else{
                    Intent intentGH = new Intent(this,CartActivity.class);
                    startActivity(intentGH);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Khởi tạo toolbar
     */
    private void initToolBar() {
        toolbar = findViewById(R.id.toolBar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        FragmentManager manager = getSupportFragmentManager();
        FragmentPagerAdapter adapter  = new FragmentPagerAdapter(manager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Hàm public để cho fragment gọi lấy thông tin Sách - nhà xuất bản - danh sách tác giả
     * @return
     */
    public Sach getThongTinSach(){
        return sach;
    }

    public String getNhaXB(){
        return nhaXB;
    }

    public List getTacGia(){
        return  listTG;
    }

    /**
     * Restart activity sẽ khởi tạo lại trạng thái số lượng Sách còn lại trong kho
     */
    @Override
    protected void onRestart() {
        initBookStatus();
        super.onRestart();
    }
}
