package com.example.a84965.bookstore.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.PagerAdapter;
import com.example.a84965.bookstore.model.Kho;
import com.example.a84965.bookstore.model.LichSu;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.GioHang;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.ultil.DrawableClickListener;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.example.a84965.bookstore.ultil.OnTextChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.readystatesoftware.viewbadger.BadgeView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.a84965.bookstore.R.color.green;
import static com.example.a84965.bookstore.activity.HomePage.isNewUser;

public class BookDetailActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    private Handler handler;
    private Sach sach = null;
    private String nhaXB = "";
    private List<String> listTG = new ArrayList<>();
    private String sach_ma = "";
    private String sach_hinh = "";
    private String sach_ten = "";
    private int sach_gia = 0 ;
    Button btn_Them;
    ImageView imgHinh;
    TextView txtTen,txtGia,txtTrangThai;
    private DatabaseReference mDatabase;
    int soluong = 1;
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
        //Toast.makeText(this, getTenTG(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Event khi click vào nút THÊM VÀO GIỎ HÀNG
     */
    private void ButtonThemGioHangClicked(){
        btn_Them.setOnClickListener(new View.OnClickListener() {
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
        btn_Them = findViewById(R.id.btnThem_Sach);
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
                    if(kho.getKho_SoLuong() >= 5){
                        txtTrangThai.setText(R.string.con_hang);
                        txtTrangThai.setTextColor(Color.GREEN);
                        btn_Them.setEnabled(true);
                    }else if (kho.getKho_SoLuong() >= 1){
                        txtTrangThai.setText(R.string.sap_het_hang);
                        txtTrangThai.setTextColor(Color.YELLOW);
                        btn_Them.setEnabled(true);
                    }else{
                        txtTrangThai.setText(R.string.het_hang);
                        txtTrangThai.setTextColor(Color.RED);
                        btn_Them.setEnabled(false);
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

        sach_ma = sach.getSach_Ma();
        sach_ten = sach.getSach_Ten();
        sach_gia = sach.getSach_DonGia();
        sach_hinh = sach.getSach_HinhAnh();
    }


    /**
     * Hiển thị Dialog Thêm số lượng sách vào giỏ hàng
     */
    private void DialogThem() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cart);

        final TextView txtSoLuong = dialog.findViewById(R.id.txtNhap_SL);
        txtSoLuong.setText(soluong+"");
        Button btnTru = dialog.findViewById(R.id.btnNhap_Tru);
        Button btnCong = dialog.findViewById(R.id.btnNhap_Cong);
        Button btnXacNhan = dialog.findViewById(R.id.btnNhap_XN);

        // event click nút +
        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong++;
                txtSoLuong.setText(soluong +"");
            }
        });
        //event click nút -
        btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong--;
                if(soluong <= 0){
                    soluong=1;
                }
                txtSoLuong.setText(soluong +"");
            }
        });
        //event click nút Xác nhận
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomePage.gioHang.size() == 0){
                    HomePage.gioHang.add(new GioHang(sach_ma,sach_ten,sach_hinh,sach_gia,soluong));
                }else{
                    boolean exist = false;
                    for(int i=0;i<HomePage.gioHang.size();i++){
                        if(sach_ma.equals(HomePage.gioHang.get(i).getSach_Ma())){
                            int sl = HomePage.gioHang.get(i).getSach_SL();
                            sl+=soluong;
                            HomePage.gioHang.get(i).setSach_SL(sl);
                            exist = true;
                        }
                    }
                    if(!exist){
                        HomePage.gioHang.add(new GioHang(sach_ma,sach_ten,sach_hinh,sach_gia,soluong));
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
        PagerAdapter adapter  = new PagerAdapter(manager);
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
