package com.example.a84965.bookstore.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.a84965.bookstore.adapter.PagerAdapter;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.GioHang;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    TextView txtTen,txtGia;
    private DatabaseReference mDatabase;
    int soluong = 1;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__book__detail);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        sach = (Sach) intent.getSerializableExtra("Sach");
        nhaXB = intent.getStringExtra("NhaXuatBan");
        listTG = (ArrayList<String>) intent.getSerializableExtra("TacGia");

        callControls();
        initToolBar();
        initBookDetail();
        ButtonThemGioHangClicked();




        //Toast.makeText(this, getTenTG(), Toast.LENGTH_SHORT).show();
    }

    private void ButtonThemGioHangClicked(){
        btn_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomePage.KH_Ten == null || HomePage.KH_Ten.equals("")){
                    DialogDangNhap();
                }else{
                    DialogThem();
                }
            }
        });
    }


    public void DialogDangNhap(){
        final Dialog dialog  = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        final TextView txtSDT = dialog.findViewById(R.id.txtLogin_SDT);
        final TextView txtMK = dialog.findViewById(R.id.txtLogin_MK);
        CheckBox cbRemember = dialog.findViewById(R.id.cbLogin);
        Button btnDangNhap = dialog.findViewById(R.id.btnDangNhap);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = false;
                mDatabase.child("KhachHang").addChildEventListener(new GetChildFireBase() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                        if (khachHang.getKH_SDT().equals(txtSDT.getText().toString()) && khachHang.getKH_MK().equals(txtMK.getText().toString())) {
                            isLogin = true;
                            HomePage.KH_Ten = khachHang.getKH_HoTen();
                            HomePage.KH_SDT = khachHang.getKH_SDT();
                            HomePage.updateMenuTitles();
                            initCart(khachHang.getKH_SDT());
                        }
                        super.onChildAdded(dataSnapshot, s);
                    }
                });
                final ProgressDialog progressDialog = new ProgressDialog(BookDetailActivity.this);
                progressDialog.setTitle("Đăng nhập");
                progressDialog.setMessage("Chờ trong giây lát !!!");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isLogin){
                            progressDialog.dismiss();
                            Toast.makeText(BookDetailActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(BookDetailActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                },3000);

            }
        });
        dialog.show();
    }

    private void  initCart(final String sdt){
        if(sdt != null){
            mDatabase.child("GioHang").child(sdt).addChildEventListener(new GetChildFireBase() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(!dataSnapshot.getKey().equals("default")){
                        GioHang gh = dataSnapshot.getValue(GioHang.class);
                        HomePage.gioHang.add(gh);
                    }
                    super.onChildAdded(dataSnapshot, s);
                }
            });
        }
    }


    private void callControls() {
        btn_Them = findViewById(R.id.btnThem_Sach);
        txtTen = findViewById(R.id.txtDetail_Ten);
        txtGia = findViewById(R.id.txtDetail_Gia);
        imgHinh = findViewById(R.id.imgBook_Detail);
        handler = new Handler();
    }

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

    private void DialogThem() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cart);

        final TextView txtSoLuong = dialog.findViewById(R.id.txtNhap_SL);
        txtSoLuong.setText(soluong+"");
        Button btnTru = dialog.findViewById(R.id.btnNhap_Tru);
        Button btnCong = dialog.findViewById(R.id.btnNhap_Cong);
        Button btnXacNhan = dialog.findViewById(R.id.btnNhap_XN);

        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong++;
                txtSoLuong.setText(soluong +"");
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuDetail_giohang:
                if(HomePage.KH_Ten == null || HomePage.KH_Ten.equals("")){
                    DialogDangNhap();
                }else{
                    Intent intentGH = new Intent(this,CartActivity.class);
                    startActivity(intentGH);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Sach getThongTinSach(){
        return sach;
    }

    public String getNhaXB(){
        return nhaXB;
    }

    public List getTacGia(){
        return  listTG;
    }
}
