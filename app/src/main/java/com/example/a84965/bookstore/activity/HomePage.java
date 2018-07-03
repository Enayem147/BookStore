package com.example.a84965.bookstore.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.example.a84965.bookstore.model.LoaiSach;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.GioHang;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.model.LichSu;
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

public class HomePage extends AppCompatActivity {

    public static boolean isNewUser = false;
    public static boolean isMainPage = true;
    public static boolean isUserOrder = false;
    public static boolean isFirst = true;
    private long backPressedTime;
    private Toast backToast;
    private Handler handler;
    Toolbar toolbar;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    ViewFlipper viewFlipper;
    LinearLayout linearLayoutMenuRes, linearLayoutMenuAll;
    static TextView txtMenuRes;
    static ImageView imgMenuRes;

    private static Menu menu;
    private DatabaseReference mDatabase;
    static public String KH_Ten = "";
    static public KhachHang khachHang = new KhachHang();
    static public String KH_SDT = "";
    static public String KH_Key = "";
    static public String new_SDT = "";
    static public String new_MK = "";
    static public ArrayList<GioHang> gioHang;
    static public ArrayList<LichSu> lichSu;
    static public ArrayList<TatCaSach> tatCaSach;
    public boolean isLogin = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        KH_SDT = sharedPreferences.getString("SDT", "");
        KH_Ten = sharedPreferences.getString("Ten", "");
        setContentView(R.layout.activity_home_page);
        callControls();
        initView();
        ActionBar();
        initMenu();
        clickMenu();
        getHinhAnhQuangCao();
        MenuRegisterClick();
        MenuAllBookClick();
        drawerLayoutChange();
        initAllBookList();
        loginKhachHangRemembered();

        if (isUserOrder) {
            initHistory(KH_SDT);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogCompleteOrder();
                }
            }, 500);
        }
        loadingScreen();


        //startActivity(new Intent(this,Activity_FireBase.class));
    }

    private void loginKhachHangRemembered() {
        //set text share ref
        if (!KH_SDT.equals("")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initHistory(KH_SDT);
                    initCart(KH_SDT);
                    updateMenuTitles();
                    getKhachHangProfile();
                }
            }, 1000);

        }
    }


    private void loadingScreen() {
        if (isFirst) {
            startActivity(new Intent(HomePage.this, LoadingScreen.class));
        }
    }

    public void getKhachHangProfile() {
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

    private void MenuRegisterClick() {
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
    }

    private void MenuAllBookClick() {
        linearLayoutMenuAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBookList = new Intent(HomePage.this, BookListActivity.class);
                startActivity(intentBookList);
            }
        });
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        this.menu = menu;
        updateMenuTitles();
        return super.onCreateOptionsMenu(menu);
    }

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
                if (KH_Ten == null || KH_Ten.equals("")) {
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

    private static String tachTen(String kh_ten) {
        int i = kh_ten.lastIndexOf(" ");
        int j = kh_ten.lastIndexOf(" ", i - 1);
        String ten = "";
        if (j > 0) {
            ten = kh_ten.substring(j + 1);
        } else {
            ten = kh_ten;
        }
        return ten;
    }

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
                KH_SDT = "";
                KH_Ten = "";
                // cap nhat lich su
                lichSu.clear();
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

    @SuppressLint("ClickableViewAccessibility")
    public void DialogDangNhap() {
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
        final boolean[] visible = {false};
        txtMK.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(txtMK) {
            @Override
            public boolean onDrawableClick() {
                if (visible[0]) {
                    txtMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_invisible, 0);
                    txtMK.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    visible[0] = false;
                } else {
                    txtMK.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtMK.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pass_visible, 0);
                    visible[0] = true;
                }
                return true;
            }
        });

        if (isNewUser) {
            isNewUser = false;
            txtSDT.setText(new_SDT);
            txtMK.setText(new_MK);
        }

        final CheckBox cbRemember = dialog.findViewById(R.id.cbLogin);
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
                                editor.putString("MK", kh.getKH_MK());
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
                        boolean error = false;
                        if (!txtMK_Cu.getText().toString().equals(khachHang.getKH_MK())) {
                            txtMK_Cu.setError("Mật khẩu cũ không đúng");
                            txtMK_Cu.requestFocus();
                            error = true;
                        } else {
                            txtMK_Cu.setError(null);
                        }

                        if (txtMK_Moi.getText().toString().equals("")) {
                            txtMK_Moi.setError("Mật khẩu mới không được rỗng");
                            txtMK_Moi.requestFocus();
                            error = true;
                        } else if (txtMK_Moi.length() < 6) {
                            txtMK_Moi.setError("Mật khẩu mới phải dài hơn 6 ký tự");
                            txtMK_Moi.requestFocus();
                            error = true;
                        } else {
                            txtMK_Moi.setError(null);
                        }

                        if (!txtMK_Moi.getText().toString().equals(txtMK_Moi_XN.getText().toString())) {
                            txtMK_Moi_XN.setError("Mật khẩu không trùng khớp");
                            txtMK_Moi_XN.requestFocus();
                            error = true;
                        } else {
                            txtMK_Moi_XN.setError(null);
                        }

                        if (!error) {
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

    private void ActionBar() {
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


    private void initCart(final String sdt) {
        gioHang = new ArrayList<>();
        if (sdt != null && !sdt.equals("") && gioHang.size() == 0) {
            mDatabase.child("GioHang").child(sdt).addChildEventListener(new GetChildFireBase() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (!dataSnapshot.getKey().equals("default")) {
                        GioHang gh = dataSnapshot.getValue(GioHang.class);
                        gioHang.add(gh);
                    }
                    super.onChildAdded(dataSnapshot, s);
                }
            });
        }
    }

    public void initHistory(final String sdt) {
        lichSu = new ArrayList<>();
        if (sdt != null && !sdt.equals("") && lichSu.size() == 0) {
            mDatabase.child("LichSu").child(sdt).addChildEventListener(new GetChildFireBase() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    LichSu ls = dataSnapshot.getValue(LichSu.class);
                    lichSu.add(ls);
                    super.onChildAdded(dataSnapshot, s);
                }
            });
        }
    }

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

    private void clickMenu() {
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

    private void callControls() {
        txtMenuRes = findViewById(R.id.txtMenuRes);
        imgMenuRes = findViewById(R.id.imgMenuRes);
        linearLayoutMenuRes = findViewById(R.id.linerLayoutMenuRes);
        linearLayoutMenuAll = findViewById(R.id.linerLayoutMenuAll);

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

        if (lichSu == null) {
            lichSu = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        if (isMainPage) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast = Toast.makeText(this, "Nhấn nút BACK lần nữa để THOÁT ! ", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
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


    private void DialogCompleteOrder() {

        Intent intent = getIntent();
        String maHD = intent.getStringExtra("MaHD");
        String ngayLapHD = intent.getStringExtra("NgayLap");

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_complete_invoice);

        TextView txtMaHD = dialog.findViewById(R.id.txtInvoiceComp_MaHD);
        TextView txtNgayLap = dialog.findViewById(R.id.txtInvoiceComp_NgayLap);
        txtMaHD.setText(maHD);
        txtNgayLap.setText(ngayLapHD);
        dialog.show();
        isUserOrder = false;
    }


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
        if (isNewUser) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogDangNhap();
                    drawerLayout.closeDrawers();
                }
            }, 1000);
        }

        super.onRestart();
    }

    private void updateCart() {
        if (KH_SDT != null && !KH_SDT.equals("")) {
            DatabaseReference dataCart = FirebaseDatabase.getInstance().getReference("GioHang").child(HomePage.KH_SDT);
            dataCart.removeValue();

            for (int i = 0; i < gioHang.size(); i++) {
                mDatabase.child("GioHang").child(KH_SDT).push().setValue(gioHang.get(i));
            }
            gioHang.clear();
        }
    }

    @Override
    protected void onDestroy() {
        updateCart();
        lichSu.clear();
        super.onDestroy();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 2000);
    }
}
