package com.example.a84965.bookstore.activity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.GioHang;
import com.example.a84965.bookstore.model.KhachHang;
import com.example.a84965.bookstore.model.TheLoai;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_FireBase extends AppCompatActivity {
    private Handler handler;
    private  long time;
    Button btnQuangCao,btnTacGia,btnNhaXB,btnKhachHang,btnSach,btnTheLoai;
    boolean isLogined = false;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__fire_base);
        callControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnQuangCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetQuangCao();
            }
        });

        btnTacGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTacGia();
            }
        });

        btnTheLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTheLoai();
            }
        });

        btnNhaXB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNXB();
            }
        });

        btnSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSach();
            }
        });

        btnKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetKhachHang();
            }


        });
    }




    private void GetKhachHang() {




      GioHang gioHang = new GioHang(
              "TT001",
              "Nếu chỉ còn một ngày để sống",
              "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book1522423242.png?alt=media&token=e8829922-5871-4be7-bcab-0e7028ea7e1a",
              100000,
              1);
      mDatabase.child("GioHang").child("0965707573").push().setValue(gioHang);



//      if(HomePage.KH_SDT == null){
//          HomePage.KH_SDT = "0965707573";
//      }
//      mDatabase.child("GioHang").child(HomePage.KH_SDT).child("default").setValue(new GioHang(
//              "default","default","default",1,1));
//        mDatabase.child("GioHang").child("default").setValue(new GioHang(
//                "default","default","default",1,1));

        //Thêm khách hàng
//        KhachHang khachHang = new KhachHang("0965707573",
//                "123456",
//                "Hoàng Nguyễn Trường Nam",
//                "269 - Phú Lợi - Sóc Trăng");
//        mDatabase.child("KhachHang").push().setValue(khachHang);
//        KhachHang khachHang1 = new KhachHang("01267427716",
//                "q123456",
//                "Trần Hoàng Nhi",
//                "Cần Thơ");
//        mDatabase.child("KhachHang").push().setValue(khachHang1);
    }

    private void callControls() {
        btnQuangCao = findViewById(R.id.btnFB_QuangCao);
        btnTacGia = findViewById(R.id.btnFB_TacGia);
        btnSach = findViewById(R.id.btnFB_Sach);
        btnKhachHang = findViewById(R.id.btnFB_KhachHang);
        btnNhaXB = findViewById(R.id.btnFB_NhaXB);
        btnTheLoai = findViewById(R.id.btnFB_TheLoai);
    }

    private void GetQuangCao(){
//        QuangCao quangCao = new QuangCao(2,
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/sach_qc.png?alt=media&token=58e714bd-4be5-4a0f-9326-ce15c456c38d");
//
//        mDatabase.child("QuangCao").push().setValue(quangCao, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                if(databaseError == null){
//                    Toast.makeText(Activity_FireBase.this, "Thêm hình thành công", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(Activity_FireBase.this, "Lỗiiiii", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void GetTacGia(){
       /* TacGia tacGia = new TacGia("NY","Nocola Yoon");
        mDatabase.child("TacGia").push().setValue(tacGia);
        TacGia tacGia1 = new TacGia("NKL","Nguyễn Khánh Linh");
        mDatabase.child("TacGia").push().setValue(tacGia1);
        TacGia tacGia2 = new TacGia("DP","Du Phong");
        mDatabase.child("TacGia").push().setValue(tacGia2);
        TacGia tacGia3 = new TacGia("MM","Mèo Mốc");
        mDatabase.child("TacGia").push().setValue(tacGia3);
        TacGia tacGia4 = new TacGia("HLT","Hamlet Trương");
        mDatabase.child("TacGia").push().setValue(tacGia4);
        TacGia tacGia5 = new TacGia("HV","Hàn Vĩ");
        mDatabase.child("TacGia").push().setValue(tacGia5);*/
    }

    private void GetSach(){
       /* Sach sach = new Sach("TT001",
                "Nếu chỉ còn một ngày để sống",
                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book1522423242.png?alt=media&token=e8829922-5871-4be7-bcab-0e7028ea7e1a",
                300,
                2014,
                100000,
                "“Nếu chỉ còn một ngày để sống” tên sách gốc Everything, Everything là cuốn tiểu thuyết bán chạy số 1 của New York Times – đồng thời được chuyển thể thành phim điện ảnh với sự góp mặt của hai diễn viên nổi tiếng là Amandla Stenberg trong vai Maddy và Nick Robinson trong vai Olly. Ngay từ khi công chiếu, bộ phim đã gây bão tại các phòng vé trên toàn thế giới kéo theo cơn sốt tìm đọc cuốn sách đặc biệt này đến từ các fan yêu thích bộ phim. ",
                "VH");
        mDatabase.child("Sach").push().setValue(sach);

        Sach sach1 = new Sach("TV002",
                "Đừng gọi anh là người yêu cũ",
                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book1522423243.jpg?alt=media&token=fe31b76f-6d97-4947-b1e6-8a28190faf18",
                285,
                2011,
                150000,
                "“Một số người coi tình cũ như vết thương, một số khác coi như chiến tích, số khác nữa lại coi như nỗi ân hận suốt đời. \n" +
                        "Còn tôi không có tình cũ, vì càng giữ nó càng mới!” ",
                "VH");
        mDatabase.child("Sach").push().setValue(sach1);

        Sach sach2 = new Sach("TV003",
                "Mèo mốc : Hành trình đến Singapore",
                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book1522423244.jpg?alt=media&token=bd7abad3-521f-4022-bef5-999b29f1e75d",
                310,
                2015,
                250000,
                "Cuốn thứ ba trong bộ sách “đen nhất vịnh Bắc Bộ”!!!!\n" +
                        "\n" +
                        "Cuốn sách sẽ đưa bạn đi qua đủ các cung bậc của cảm xúc, trên nền tảng chính là sự hư cấu không biên giới, không điểm dừng!!!!\n" +
                        "Cuốn sách có thể giết bạn vì cười vỡ bụng!!!\n" +
                        "Cuốn sách đến từ tác giả “hư cấu” nhất, đen đủi nhất, và ‘hi sinh vì nghệ thuật' nhất Skycomics!!!",
                "DT");
        mDatabase.child("Sach").push().setValue(sach2);

        Sach sach3 = new Sach("TV004",
                "Ai cũng có một khoảng trời giấu kín",
                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book1522423241.jpg?alt=media&token=182560b5-538a-4ee5-8ab8-27ae70ffa38a",
                280,
                2013,
                200000,
                "“Dù thế nào, em  hãy cứ hướng về phía trước, chầm chậm, chậm như hơi thở của em. Chỉ cần việc em thở, cũng là một bước tiến về phía trước rồi. Em không cần phải tiến về phía mặt trời, em chỉ cần tiến về phía trước mà thôi.”",
                "VH");
        mDatabase.child("Sach").push().setValue(sach3);
*/

    }

    private void GetTheLoai(){
//        TheLoai theLoai = new TheLoai(1,"Tản Văn",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/tanvan.jpg?alt=media&token=ef03314f-3834-4e0c-8027-4d10a78e0cf3");
//        mDatabase.child("TheLoai").push().setValue(theLoai);
//        TheLoai theLoai1 = new TheLoai(2,"Thơ",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/tho.jpg?alt=media&token=a4f5493e-db81-492b-9fee-a608921f9921");
//        mDatabase.child("TheLoai").push().setValue(theLoai1);
//        TheLoai theLoai2 = new TheLoai(3,"Tiểu Thuyết",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/tieuthuyet.jpg?alt=media&token=a9b717ab-b020-44c1-858b-23661aab5d7b");
//        mDatabase.child("TheLoai").push().setValue(theLoai2);
    }

    private void GetNXB(){
//        NhaXuatBan nxb = new NhaXuatBan("VH","Văn học");
//        mDatabase.child("NhaXuatBan").push().setValue(nxb);
//        NhaXuatBan nxb1 = new NhaXuatBan("T","Trẻ");
//        mDatabase.child("NhaXuatBan").push().setValue(nxb1);
//        NhaXuatBan nxb2 = new NhaXuatBan("DT","Dân trí");
//        mDatabase.child("NhaXuatBan").push().setValue(nxb2);
    }
}
