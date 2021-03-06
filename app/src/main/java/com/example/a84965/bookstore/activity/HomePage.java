package com.example.a84965.bookstore.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.MenuAdapter;
import com.example.a84965.bookstore.adapter.NewBooksAdapter;
import com.example.a84965.bookstore.model.DonHang;
import com.example.a84965.bookstore.model.Kho;
import com.example.a84965.bookstore.model.LoaiSach;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.GioHang;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.model.QuangCao;
import com.example.a84965.bookstore.model.TacGia;
import com.example.a84965.bookstore.model.TacGiaChiTiet;
import com.example.a84965.bookstore.model.TatCaSach;
import com.example.a84965.bookstore.model.TheLoai;
import com.example.a84965.bookstore.ultil.DrawableClickListener;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.example.a84965.bookstore.ultil.OnTextChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomePage extends AppCompatActivity {

    public static boolean isNewUser = false;
    public static boolean isMainPage = true;
    public static boolean isFirst = true;
    private Handler handler;
    Toolbar toolbar;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    ViewFlipper viewFlipper;
    LinearLayout linearLayoutMenuRes, linearLayoutMenuAll , linearLayoutMenuContact ;
    public static LinearLayout linearLayoutMenuCusOrder;
    static TextView txtMenuRes;
    static ImageView imgMenuRes;

    private static Menu menu;
    private static DatabaseReference mDatabase;
    static public String KH_Ten = "";
    static public KhachHang khachHang = new KhachHang();
    static public String KH_SDT = "";
    static public String KH_Key = "";
    static public String new_SDT = "";
    static public ArrayList<GioHang> gioHang;
    static public ArrayList<Integer> listSoLuongKho;
    static public ArrayList<DonHang> donHang;
    static public ArrayList<DonHang> lichSu;
    static public ArrayList<TatCaSach> tatCaSach;
    static public ArrayList<String> listMaDH;
    public boolean isLogin = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        callControls();
        initView();
        initToolbar();
        initMenu();
        clickEventMenuLeft();
        getHinhAnhQuangCao();
        drawerLayoutChange();
        initAllBookList();
        loginKhachHangRemembered();
        loadingScreen();
    }

    /**
     * Tự login khi mà lần trước khách hàng login check vào checkbox Ghi nhớ đăng nhập
     */
    private void loginKhachHangRemembered() {
        //set text share ref
        if (!KH_SDT.equals("")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linearLayoutMenuCusOrder.setEnabled(true);
                    initHistory(KH_SDT);
                    initCart(KH_SDT);
                    updateMenuTitles();
                    getKhachHangProfile();
                }
            }, 1000);
        }
    }

    /**
     * Khi bắt đầu apps chuyển sang trang loading
     */
    private void loadingScreen() {
        if (isFirst) {
            startActivity(new Intent(HomePage.this, LoadingScreen.class));
        }
    }

    /**
     * Khởi tạo profile của khách hàng
     */
    public static void getKhachHangProfile() {
        if (!KH_SDT.equals("") && KH_SDT != null) {
            mDatabase.child("KhachHang").addChildEventListener(new GetChildFireBase() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                    if (kh.getKH_SDT().equals(KH_SDT)) {
                        khachHang = kh;
                        KH_Key = dataSnapshot.getKey();
                    }
                    super.onChildAdded(dataSnapshot, s);
                }
            });
        }

    }

    /**
     * Khởi tạo danh sách tất cả Sách ( để qua trang tất cả sản phẩm )
     */
    private void initAllBookList() {
        tatCaSach = new ArrayList<>();
        mDatabase.child("Sach").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Sach sach = dataSnapshot.getValue(Sach.class);
                final ArrayList<String> listTG = new ArrayList<>();
                final ArrayList<String> listTL = new ArrayList<>();
                //lấy danh sách các tác giả
                mDatabase.child("TacGiaChiTiet").addChildEventListener(new GetChildFireBase() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        final TacGiaChiTiet tacGiaChiTiet = dataSnapshot.getValue(TacGiaChiTiet.class);
                        if (tacGiaChiTiet.getSach_Ma().equals(sach.getSach_Ma())) {
                            mDatabase.child("TacGia").addChildEventListener(new GetChildFireBase() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    TacGia tacGia = dataSnapshot.getValue(TacGia.class);
                                    if (tacGia.getTG_Ma().equals(tacGiaChiTiet.getTG_Ma())) {
                                        // lấy danh sách đồng tác giả
                                        listTG.add(tacGia.getTG_Ten());
                                    }
                                    super.onChildAdded(dataSnapshot, s);
                                }
                            });
                        }
                        super.onChildAdded(dataSnapshot, s);
                    }
                });

                //lấy danh sách các thể loại
                mDatabase.child("LoaiSach").addChildEventListener(new GetChildFireBase() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        final LoaiSach loaiSach = dataSnapshot.getValue(LoaiSach.class);
                        if (loaiSach.getSach_Ma().equals(sach.getSach_Ma())) {
                            mDatabase.child("TheLoai").addChildEventListener(new GetChildFireBase() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                                    if (theLoai.getTL_Ma() == loaiSach.getTL_Ma()) {
                                        listTL.add(theLoai.getTL_Ten());
                                    }
                                    super.onChildAdded(dataSnapshot, s);
                                }
                            });
                        }
                        super.onChildAdded(dataSnapshot, s);
                    }
                });

                // add vào danh sách tất cả sách
                tatCaSach.add(new TatCaSach(sach.getSach_Ma(),
                        sach.getSach_Ten(),
                        sach.getSach_HinhAnh(),
                        sach.getSach_SoTrang(),
                        sach.getSach_NamXB(),
                        sach.getSach_DonGia(),
                        sach.getSach_GioiThieu(),
                        sach.getNXB_Ma(),
                        listTG,
                        listTL));

                super.onChildAdded(dataSnapshot, s);
            }
        });
    }

    /**
     * Event click của menu left
     */
    private void clickEventMenuLeft(){
        //menu tất cả sản phẩm
        linearLayoutMenuAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBookList = new Intent(HomePage.this, BookListActivity.class);
                startActivity(intentBookList);
            }
        });

        //menu tình trạng đơn hàng

        linearLayoutMenuCusOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCosOrder = new Intent(HomePage.this, CustomerOrderActivity.class);
                startActivity(intentCosOrder);
            }
        });

        //menu liên hệ
        linearLayoutMenuContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(HomePage.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_contact);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSile;
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView txtThoat = dialog.findViewById(R.id.txtContact_Thoat);
                txtThoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        // menu đăng ký / profile
        linearLayoutMenuRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KH_SDT == null || KH_SDT.equals("")) {
                    Intent intentRes = new Intent(HomePage.this, RegisterActivity.class);
                    startActivity(intentRes);
                } else {
                    DialogProfile();
                }
            }
        });

        //menu từng thể loại
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TheLoai theLoai = (TheLoai) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), BooksActivity.class);
                intent.putExtra("TL_Ma", theLoai.getTL_Ma());
                intent.putExtra("TL_Ten", theLoai.getTL_Ten());
                startActivity(intent);
            }
        });

    }

    /**
     * Event khi mà menu left đóng hoặc mở
     */
    private void drawerLayoutChange() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isMainPage = false;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isMainPage = true;
                    }
                }, 500);

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * update các icon - trạng thái của Đăng nhập / Đăng xuất và Đăng ký / Profile
     */
    public static void updateMenuTitles() {
        MenuItem loginMenuItem = menu.findItem(R.id.menu_dangnhap);
        MenuItem historyMenuItem = menu.findItem(R.id.menu_history);
        if (KH_SDT == null || KH_SDT.equals("")) {
            txtMenuRes.setText(R.string.dang_ky);
            imgMenuRes.setImageResource(R.drawable.icon_menu_register);
            loginMenuItem.setIcon(R.drawable.ic_menu_login);
            historyMenuItem.setVisible(false);
        } else {
            txtMenuRes.setText(tachTen(KH_Ten));
            imgMenuRes.setImageResource(R.drawable.icon_menu_user);
            loginMenuItem.setIcon(R.drawable.ic_menu_logout);
            historyMenuItem.setVisible(true);
        }
    }
    /**
     * Tạo menu đăng nhập
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        this.menu = menu;
        updateMenuTitles();
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Event click nút đăng nhập / đăng xuất
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_dangnhap:
                if (KH_SDT == null || KH_SDT.equals("")) {
                    DialogDangNhap();
                    break;
                } else {
                    DialogDangXuat();
                    break;
                }
            case R.id.menu_giohang:
                if (KH_SDT == null || KH_SDT.equals("")) {
                    DialogDangNhap();
                } else {
                    Intent intent3 = new Intent(this, CartActivity.class);
                    startActivity(intent3);
                }
                break;
            case R.id.menu_history:
                Intent intHistory = new Intent(this, HistoryActivity.class);
                startActivity(intHistory);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Tách tên ( chỉ lấy tên và chữ lót kề tên ) cho menu left
     * @param kh_ten : Tên khách hàng
     * @return
     */
    private static String tachTen(String kh_ten) {
        int i = kh_ten.lastIndexOf(" ");
        int j = kh_ten.lastIndexOf(" ", i - 1);
        String ten = "";
        if(kh_ten.equals("")){
            ten = "Người dùng mới";
        }else if (j > 0) {
            ten = kh_ten.substring(j + 1);
        } else {
            ten = kh_ten;
        }
        return ten;
    }


    /**
     * Hiển thị dialog khi nhấn vào icon Đăng xuất
     */
    private void DialogDangXuat() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Bạn có muốn đăng xuất không");
        alertDialog.setTitle("Đăng xuất");
        alertDialog.setIcon(R.drawable.ic_dialog_logout);
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cap nhật giỏ hàng
                updateCart();
                linearLayoutMenuCusOrder.setEnabled(false);
                KH_SDT = "";
                KH_Ten = "";
                // cap nhat lich su
                donHang.clear();
                khachHang = new KhachHang();
                //remove shareRef
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("SDT");
                editor.remove("MK");
                editor.remove("Ten");
                editor.commit();
                Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                updateMenuTitles();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    /**
     * Khởi tạo các hình ảnh quảng cáo
     */
    private void getHinhAnhQuangCao() {
        viewFlipper = findViewById(R.id.imgQuangCao_TrangChinh);
        mDatabase.child("QuangCao").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                QuangCao qc = dataSnapshot.getValue(QuangCao.class);
                ImageView imageView = new ImageView(getApplicationContext());
                Picasso.get()
                        .load(qc.getQC_HinhAnh())
                        .into(imageView);
                viewFlipper.addView(imageView);
                super.onChildAdded(dataSnapshot, s);
            }
        });

        viewFlipper.setFlipInterval(4500);
        viewFlipper.setAutoStart(true);

        Animation animation_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

        viewFlipper.setInAnimation(animation_in);
        viewFlipper.setOutAnimation(animation_out);
    }

    /**
     * Hiển thị dialog Đăng Nhập
     */
    @SuppressLint("ClickableViewAccessibility")
    public void DialogDangNhap() {
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
                final Dialog dialogRes = new Dialog(HomePage.this);
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

                        final ProgressDialog progressDialog = new ProgressDialog(HomePage.this);
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
                                    Toast.makeText(HomePage.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                                } else if (error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(HomePage.this, strErr, Toast.LENGTH_SHORT).show();
                                }else if(exist[0]){
                                    progressDialog.dismiss();
                                    Toast.makeText(HomePage.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(HomePage.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    KhachHang khachHang = new KhachHang(sdt, mk, "", "");
                                    mDatabase.child("KhachHang").push().setValue(khachHang);
                                    new_SDT = sdt;
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

        if (isNewUser) {
            isNewUser = false;
            txtSDT.setText(new_SDT);
        }



        final CheckBox cbRemember = dialog.findViewById(R.id.cbLogin);
        cbRemember.setChecked(true);
        Button btnDangNhap = dialog.findViewById(R.id.btnDangNhap);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = false;
                mDatabase.child("KhachHang").addChildEventListener(new GetChildFireBase() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                        if (kh.getKH_SDT().equals(txtSDT.getText().toString()) && kh.getKH_MK().equals(txtMK.getText().toString())) {
                            isLogin = true;
                            linearLayoutMenuCusOrder.setEnabled(true);
                            KH_Ten = kh.getKH_HoTen();
                            KH_SDT = kh.getKH_SDT();
                            initCart(kh.getKH_SDT());
                            initHistory(kh.getKH_SDT());
                            updateMenuTitles();
                            if (cbRemember.isChecked()) {
                                // lưu sdt và mk lại cho lần sau không cần đăng nhập
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("SDT", kh.getKH_SDT());
                                editor.putString("Ten", kh.getKH_HoTen());
                                editor.commit();

                            }
                        }
                        super.onChildAdded(dataSnapshot, s);
                    }
                });
                final ProgressDialog progressDialog = new ProgressDialog(HomePage.this);
                progressDialog.setMessage("Chờ trong giây lát !!!");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLogin) {
                            getKhachHangProfile();
                            progressDialog.dismiss();
                            Toast.makeText(HomePage.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(HomePage.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3000);

            }
        });
        dialog.show();
    }

    /**
     * Kiểm tra số điện thoại ( 10 số  , chỉ có thể là số )
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
     * Hiển thị dialog Profile khách hàng
     */
    private void DialogProfile() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_user_profile);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSile;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtThoat = dialog.findViewById(R.id.txtProf_Thoat);
        final TextView txtHoTen = dialog.findViewById(R.id.txtProf_HoTen);
        TextView txtSDT = dialog.findViewById(R.id.txtProf_SDT);
        final TextView txtDiaChi = dialog.findViewById(R.id.txtProf_DiaChi);

        if (khachHang != null) {
            txtHoTen.setText(khachHang.getKH_HoTen().toUpperCase());
            txtSDT.setText(khachHang.getKH_SDT());
            txtDiaChi.setText(khachHang.getKH_DiaChi());
        }
        LinearLayout btnChangePass = dialog.findViewById(R.id.btnProf_ChangePass);
        LinearLayout btnChangeProf = dialog.findViewById(R.id.btnProf_ChangeProf);

        txtThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                final Dialog dialogChangePass = new Dialog(dialog.getContext());
                dialogChangePass.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogChangePass.setContentView(R.layout.dialog_user_profile_chpass);
                dialogChangePass.getWindow().getAttributes().windowAnimations = R.style.DialogSile;
                dialogChangePass.setCanceledOnTouchOutside(false);
                dialogChangePass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView txtThoat = dialogChangePass.findViewById(R.id.txtProf_Thoat);
                final TextView txtMK_Cu = dialogChangePass.findViewById(R.id.txtProfEdit_OldPass);
                final TextView txtMK_Moi = dialogChangePass.findViewById(R.id.txtProfEdit_NewPass);
                final TextView txtMK_Moi_XN = dialogChangePass.findViewById(R.id.txtProfEdit_NewPassConfirm);

                //event Drawable click
                txtMK_Cu.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtMK_Cu) {
                    @Override
                    public boolean onDrawableClick() {
                        if (txtMK_Cu.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            txtMK_Cu.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
                            txtMK_Cu.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        } else {
                            txtMK_Cu.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            txtMK_Cu.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_visible, 0);
                        }
                        return true;
                    }
                });

                txtMK_Moi.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtMK_Moi) {
                    @Override
                    public boolean onDrawableClick() {
                        if (txtMK_Moi.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            txtMK_Moi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
                            txtMK_Moi.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        } else {
                            txtMK_Moi.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            txtMK_Moi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_visible, 0);
                        }
                        return true;
                    }
                });

                txtMK_Moi_XN.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtMK_Moi_XN) {
                    @Override
                    public boolean onDrawableClick() {
                        if (txtMK_Moi_XN.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            txtMK_Moi_XN.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
                            txtMK_Moi_XN.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        } else {
                            txtMK_Moi_XN.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            txtMK_Moi_XN.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_visible, 0);
                        }
                        return true;
                    }
                });


                Button btnXN = dialogChangePass.findViewById(R.id.btnProf_XN);
                txtThoat.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        dialogChangePass.dismiss();
                    }
                });

                btnXN.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        String mk_cu = txtMK_Cu.getText().toString();
                        String mk_moi = txtMK_Moi.getText().toString();
                        String mk_moi_xn = txtMK_Moi_XN.getText().toString();

                        boolean error = false;
                        boolean blank = false;
                        String strErr = "";

                        if (mk_cu.length() == 0 || mk_moi.length() == 0 || mk_moi_xn.length() == 0) {
                            blank = true;
                        }

                        int i = 0;
                        if (!mk_cu.equals(khachHang.getKH_MK())) {
                            if (++i == 1) {
                                strErr += "Mật khẩu cũ không đúng";
                            } else {
                                strErr += "\nMật khẩu cũ không đúng";
                            }
                            error = true;
                        }

                        if (mk_moi.length() < 6) {
                            if (++i == 1) {
                                strErr += "Mật khẩu mới phải dài hơn 6 ký tự";
                            } else {
                                strErr += "\nMật khẩu mới phải dài hơn 6 ký tự";
                            }
                            error = true;
                        }

                        if (!mk_moi.equals(mk_moi_xn)) {
                            if (++i == 1) {
                                strErr += "Mật khẩu xác nhận không trùng khớp";
                            } else {
                                strErr += "\nMật khẩu xác nhận không trùng khớp";
                            }
                            error = true;
                        }

                        if (blank) {
                            Toast.makeText(HomePage.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else if (error) {
                            Toast.makeText(HomePage.this, strErr, Toast.LENGTH_SHORT).show();
                        } else {
                            final ProgressDialog progressDialog = new ProgressDialog(HomePage.this);
                            progressDialog.setMessage("Chờ trong giây lát !!!");
                            progressDialog.show();
                            DatabaseReference updateKH = FirebaseDatabase.getInstance().getReference("KhachHang").child(KH_Key);
                            updateKH.setValue(new KhachHang(khachHang.getKH_SDT(), txtMK_Moi.getText().toString(), khachHang.getKH_HoTen(), khachHang.getKH_DiaChi()));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    dialogChangePass.dismiss();
                                    Toast.makeText(HomePage.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                }
                            }, 1000);
                        }

                    }
                });
                dialogChangePass.show();
            }
        });

        btnChangeProf.setOnClickListener(new View.OnClickListener()

        {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                final Dialog dialogChangeProf = new Dialog(dialog.getContext());
                dialogChangeProf.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogChangeProf.setContentView(R.layout.dialog_user_profile_chprof);
                dialogChangeProf.getWindow().getAttributes().windowAnimations = R.style.DialogSile;
                dialogChangeProf.setCanceledOnTouchOutside(false);
                dialogChangeProf.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView txtThoat = dialogChangeProf.findViewById(R.id.txtProf_Thoat);
                final TextView txtEditTen = dialogChangeProf.findViewById(R.id.txtProfEdit_HoTen);
                final TextView txtEditDiaChi = dialogChangeProf.findViewById(R.id.txtProfEdit_DiaChi);

                //Drawable click txtEditTen
                txtEditTen.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtEditTen) {
                    @Override
                    public boolean onDrawableClick() {
                        txtEditTen.setText("");
                        txtEditTen.requestFocus();
                        return true;
                    }
                });

                txtEditTen.addTextChangedListener(new OnTextChangeListener() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            txtEditTen.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        } else {
                            txtEditTen.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);
                        }
                    }
                });

                //Drawable click txtEditDiaChi
                txtEditDiaChi.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtEditDiaChi) {
                    @Override
                    public boolean onDrawableClick() {
                        txtEditDiaChi.setText("");
                        txtEditDiaChi.requestFocus();
                        return true;
                    }
                });

                txtEditDiaChi.addTextChangedListener(new OnTextChangeListener() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            txtEditDiaChi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        } else {
                            txtEditDiaChi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_cancel, 0);
                        }
                    }
                });


                Button btnXN = dialogChangeProf.findViewById(R.id.btnProf_XN);

                txtEditTen.setText(HomePage.khachHang.getKH_HoTen());
                txtEditDiaChi.setText(HomePage.khachHang.getKH_DiaChi());

                txtThoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogChangeProf.dismiss();
                    }
                });

                btnXN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(HomePage.this);
                        progressDialog.setMessage("Chờ trong giây lát !!!");
                        progressDialog.show();
                        if (txtEditDiaChi.getText().toString().equals("") || txtEditTen.getText().toString().equals("")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(HomePage.this, "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }, 1000);

                        } else {
                            if (txtEditDiaChi.getText().toString().equals(khachHang.getKH_DiaChi()) && txtEditTen.getText().toString().equals(khachHang.getKH_HoTen())) {
                                dialogChangeProf.dismiss();
                                progressDialog.dismiss();
                            } else {
                                // update thong tin
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Ten", txtEditTen.getText().toString());
                                editor.commit();
                                DatabaseReference updateKH = FirebaseDatabase.getInstance().getReference("KhachHang").child(KH_Key);
                                updateKH.setValue(new KhachHang(khachHang.getKH_SDT(), khachHang.getKH_MK(), txtEditTen.getText().toString(), txtEditDiaChi.getText().toString()));
                                //get lại thông tin
                                getKhachHangProfile();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //set text
                                        progressDialog.dismiss();
                                        dialogChangeProf.dismiss();
                                        txtHoTen.setText(khachHang.getKH_HoTen());
                                        txtDiaChi.setText(khachHang.getKH_DiaChi());
                                        txtMenuRes.setText(tachTen(khachHang.getKH_HoTen()));
                                        Toast.makeText(HomePage.this, "Thay đổi thông tin thành công", Toast.LENGTH_LONG).show();
                                    }
                                }, 1000);
                            }
                        }
                    }
                });
                dialogChangeProf.show();
            }
        });

        dialog.show();
    }

    /**
     * khởi tạo toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_format_list_bulleted_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * khởi tạo 6 quyển sách mới nhất
     */
    private void initView() {
        final ArrayList<Sach> listBook = new ArrayList<>();
        final NewBooksAdapter _new_booksAdapter;
        _new_booksAdapter = new NewBooksAdapter(listBook, this);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, gridLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(_new_booksAdapter);

        final NewBooksAdapter final_new_booksAdapter = _new_booksAdapter;
        mDatabase.child("Sach").limitToLast(6).addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Sach sach = dataSnapshot.getValue(Sach.class);
                listBook.add(sach);
                _new_booksAdapter.notifyDataSetChanged();
                super.onChildAdded(dataSnapshot, s);
            }
        });

    }

    /**
     * Khởi tạo giỏ hàng
     * @param sdt : Số điện thoại của khách hàng
     */
    public static void initCart(final String sdt) {
        listSoLuongKho = new ArrayList<>();
        gioHang = new ArrayList<>();
        if (sdt != null && !sdt.equals("") && gioHang.size() == 0) {
            mDatabase.child("GioHang").child(sdt).addChildEventListener(new GetChildFireBase() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (!dataSnapshot.getKey().equals("default")) {
                        final GioHang gh = dataSnapshot.getValue(GioHang.class);
                        gioHang.add(gh);
                        mDatabase.child("Kho").addChildEventListener(new GetChildFireBase() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Kho kho = dataSnapshot.getValue(Kho.class);
                                if(gh.getSach_Ma().equals(kho.getSach_Ma())){
                                    listSoLuongKho.add(kho.getKho_SoLuong());
                                }
                                super.onChildAdded(dataSnapshot, s);
                            }
                        });
                    }
                    super.onChildAdded(dataSnapshot, s);
                }
            });
        }
    }

    /**
     * khởi tạo Lịch sử mua hàng
     * @param sdt : Số điện thoại của khách hàng
     */

    public static void initHistory(final String sdt) {
        donHang = new ArrayList<>();
        listMaDH = new ArrayList<>();
        lichSu = new ArrayList<>();
        listMaDH.addAll(Collections.singleton("Mã đơn hàng"));
        if (sdt != null && !sdt.equals("") && donHang.size() == 0) {
            mDatabase.child("DonHang").child(sdt).addChildEventListener(new GetChildFireBase() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DonHang dh = dataSnapshot.getValue(DonHang.class);
                    if(dh.getDH_TrangThai() == 1 || dh.getDH_TrangThai()==2){
                        donHang.add(0,dh);
                    }
                    if(dh.getDH_TrangThai() == 3){
                        lichSu.add(0,dh);
                        listMaDH.add(1,dh.getDH_Ma());
                    }

                    super.onChildAdded(dataSnapshot, s);
                }
            });
        }
    }



    /**
     * khởi tạo menu các thể loại Sách trong menu left
     */

    private void initMenu() {
        final MenuAdapter _menuAdapter;
        final ArrayList<TheLoai> list = new ArrayList<>();
        _menuAdapter = new MenuAdapter(getApplicationContext(), list);
        listView.setAdapter(_menuAdapter);
        mDatabase.child("TheLoai").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                list.add(theLoai);
                _menuAdapter.notifyDataSetChanged();
                super.onChildAdded(dataSnapshot, s);
            }
        });

    }

    /**
     * Khởi tạo giá trị đầu vào
     */

    private void callControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        KH_SDT = sharedPreferences.getString("SDT", "");
        KH_Ten = sharedPreferences.getString("Ten", "");
        txtMenuRes = findViewById(R.id.txtMenuRes);
        imgMenuRes = findViewById(R.id.imgMenuRes);
        linearLayoutMenuRes = findViewById(R.id.linerLayoutMenuRes);
        linearLayoutMenuAll = findViewById(R.id.linerLayoutMenuAll);
        linearLayoutMenuContact = findViewById(R.id.linerLayoutMenuContact);
        linearLayoutMenuCusOrder = findViewById(R.id.linerLayoutMenuCusOrder);
        linearLayoutMenuCusOrder.setEnabled(false);


        handler = new Handler();
        toolbar = findViewById(R.id.toolBar_TrangChinh);
        setTitle("Book Store");
        navigationView = findViewById(R.id.navigationView);
        listView = findViewById(R.id.listView);
        drawerLayout = findViewById(R.id.drawerLayout_TrangChinh);
        recyclerView = findViewById(R.id.recyclerView);
        if (gioHang == null) {
            gioHang = new ArrayList<>();
        }

        if(listSoLuongKho == null){
            listSoLuongKho = new ArrayList<>();
        }

        if (donHang == null) {
            donHang = new ArrayList<>();
        }

        if(listMaDH == null){
            listMaDH = new ArrayList<>();
        }
    }

    /**
     * Click BACK để thoát khỏi ứng dụng
     */
    @Override
    public void onBackPressed() {
        if (isMainPage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn có chắc chắn muốn thoát khỏi ứng dụng không ? ");
            builder.setCancelable(false);
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateCart();
                    donHang.clear();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.exit(1);
                        }
                    }, 2000);
                }
            });

            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            drawerLayout.closeDrawers();
            isMainPage = true;
        }
    }


    @Override
    protected void onPause() {
        isMainPage = false;
        super.onPause();
    }

    /**
     * Hiển thị dialog chi tiết Order của khách hàng khi họ vừa đặt hàng xong
     */



    @Override
    protected void onRestart() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isMainPage = false;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isMainPage = true;
                    }
                }, 1000);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // Khi khách hàng vừa đăng ký thì hiển thị dialog đăng nhập ( tự điền SDT + MK )
        if (isNewUser) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogDangNhap();
                    drawerLayout.closeDrawers();
                }
            }, 300);
        }

        // init lại History và BookList ( tránh trường hợp search xong quay lại HomePage làm thay đổi History và BookList )
        if(!KH_SDT.equals("") && KH_SDT != null){
            initHistory(KH_SDT);
            initAllBookList();
        }
        super.onRestart();
    }


    /**
     * Update lại giỏ hàng ( xóa cái cũ , thêm cái mới )
     */
    private void updateCart() {
        if (KH_SDT != null && !KH_SDT.equals("")) {
            DatabaseReference dataCart = FirebaseDatabase.getInstance().getReference("GioHang").child(HomePage.KH_SDT);
            dataCart.removeValue();

            for (int i = 0; i < gioHang.size(); i++) {
                mDatabase.child("GioHang").child(KH_SDT).push().setValue(gioHang.get(i));
            }
            gioHang.clear();
            listSoLuongKho.clear();
        }
    }
}
