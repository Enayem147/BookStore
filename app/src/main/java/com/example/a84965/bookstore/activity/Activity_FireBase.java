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
import com.example.a84965.bookstore.model.Kho;
import com.example.a84965.bookstore.model.LoaiSach;
import com.example.a84965.bookstore.model.NhaXuatBan;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.TacGia;
import com.example.a84965.bookstore.model.TacGiaChiTiet;
import com.example.a84965.bookstore.model.TheLoai;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_FireBase extends AppCompatActivity {
    private Handler handler;
    private long time;
    Button btnQuangCao, btnTacGia, btnNhaXB, btnKhachHang, btnSach, btnTheLoai;
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


        final ArrayList<String> listKey = new ArrayList<>();
        final ArrayList<Integer> listSoLuong = new ArrayList<>();
        final ArrayList<GioHang> listKho = new ArrayList<>();
        listKho.add(new GioHang("TV003",null,null,200,2));
        //listKho.add(new GioHang("TV002",null,null,200,1));

        mDatabase.child("Kho").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Kho kho = dataSnapshot.getValue(Kho.class);
                for (int i = 0; i < listKho.size(); i++) {
                    if (listKho.get(i).getSach_Ma().equals(kho.getSach_Ma())) {
                        listKey.add(dataSnapshot.getKey());
                        //Toast.makeText(Activity_FireBase.this, kho.getKho_SoLuong()+"", Toast.LENGTH_SHORT).show();
                        listSoLuong.add(kho.getKho_SoLuong());
                    }
                }
                super.onChildAdded(dataSnapshot, s);
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listKey.size(); i++) {
                    //Toast.makeText(Activity_FireBase.this, listSoLuong.get(i)+"", Toast.LENGTH_SHORT).show();
                     DatabaseReference updateKho = FirebaseDatabase.getInstance().getReference("Kho").child(listKey.get(i));
                     updateKho.setValue(new Kho(listKho.get(i).getSach_Ma(),listSoLuong.get(i) - listKho.get(i).getSach_SL()));
                }
            }
        },1500);




//        final ArrayList<String> test = new ArrayList<String>();
//        test.add("TV001");
//        test.add("TV002");
//        mDatabase.child("Kho").addChildEventListener(new GetChildFireBase() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Kho kho = dataSnapshot.getValue(Kho.class);
//                for(int i=0 ; i< test.size() ; i++){
//                    if(kho.getSach_Ma().equals(test.get(i))){
//                        mDatabase.child("Kho").setValue(new Kho(test.get(i),9));
//                    }
//                }
//
//                super.onChildAdded(dataSnapshot, s);
//            }
//        });

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

    private void GetQuangCao() {
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

    private void GetTacGia() {
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

    private void GetSach() {

//        mDatabase.child("Kho").push().setValue(new Kho("TV001", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("TV002", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("TV003", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("TV004", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("TN001", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("TT001", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("TT002", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("TT003", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("T001", 10));
//        mDatabase.child("Kho").push().setValue(new Kho("T002", 10));
//
//
//        mDatabase.child("TacGia").push().setValue(new TacGia("BA", "Bông An"));
//        mDatabase.child("NhaXuatBan").push().setValue(new NhaXuatBan("HNV", "Hội nhà văn"));
//        mDatabase.child("TacGiaChiTiet").push().setValue(new TacGiaChiTiet("T001", "BA"));
//        Sach sach = new Sach("T001",
//                "Cô gái trên trăng",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book112.jpg?alt=media&token=01824fa7-2bac-462d-8d63-f65bfc37f9c0",
//                70,
//                2016,
//                70000,
//                " “Cô gái trên trăng là tập thơ đầu tay của cô bé gốc Việt 8 tuổi đáng yêu Bông An. Cô bé nói chuyện với con mèo, với cái cây, cầu vồng… rồi với cả người ngoài hành tinh trốn dưới gầm giường. Cô bé chăm sóc cô em gái nhỏ và cả con lừa Molly thích ăn cỏ tháng Năm.\n" +
//                        "\n" +
//                        "Đôi lúc cô bé phấn khích như mùi thơm của trứng bác trên bánh mì mới nướng, lúc lại buồn thiu như cơm nát. Thơ của Bông An trong sáng nhưng không hề ngây ngô. Vốn từ của bé phong phú, ngữ pháp chuẩn xác. Tập thơ có minh họa của họa sĩ Đặng Hồng Quân và phần dịch thơ tiếng Việt của nhà thơ Nhã Thuyên.",
//                "HNV");
//        mDatabase.child("Sach").push().setValue(sach);
//
//
//        mDatabase.child("TacGia").push().setValue(new TacGia("VL", "Vi Lam"));
//        mDatabase.child("TacGiaChiTiet").push().setValue(new TacGiaChiTiet("T002", "VL"));
//        Sach sach1 = new Sach("T002",
//                "Giữa Trời và Đất là Tình Yêu",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book113.jpg?alt=media&token=3a54d019-02b0-40a7-91b7-ca648703e389",
//                160,
//                2016,
//                120000,
//                "Đây là một cuốn sách về cuộc đời, về tình yêu và tuổi trẻ thật sự đặc biệt! Cái đặc biệt nhất ở đây là từng trang sách bạn đang cầm trên tay đều được tác giả trau chuốt đến từng câu chữ và chi tiết minh họa.\n" +
//                        "\n" +
//                        "Tuyển tập thơ Giữa trời và đất là tình yêu là sự kết hợp độc đáo giữa thơ (poem) với nghệ thuật đồ họa chữ (typography hay viết tắt là typo). Lần đầu tiên có một tập thơ độc đáo đến lạ lùng như vậy.\n" +
//                        "\n" +
//                        "“Đời mình chỉ là cuộc rong chơi, mỏi chân thì nghỉ hết mệt lại đi.”",
//                "VH");
//        mDatabase.child("Sach").push().setValue(sach1);
//
//
//        mDatabase.child("TacGia").push().setValue(new TacGia("THV", "Tô Hải Vân"));
//        mDatabase.child("TacGiaChiTiet").push().setValue(new TacGiaChiTiet("TT002", "THV"));
//        Sach sach2 = new Sach("TT002",
//                "6 Ngày",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book114.jpg?alt=media&token=e9d1c44f-8524-4a9a-8c66-11838a279d77",
//                388,
//                2017,
//                200000,
//                "6 ngày của một tuần với các buổi sáng trưa chiều tối đêm. Nhân vật công chức sống thế nào trong một ngày ấy trong thế giới của mình, nghĩ thế nào cho mỗi công việc, vui buồn thế nào cho mổi chuyện anh chứng kiến và tham dự. Cuộc sống thì đẹp, đời người thì hữu hạn, vui vẻ hay chán chường, giàu hay nghèo không phải là việc quan trọng nữa, mà quan trọng là được sống bình thường.",
//                "T");
//        mDatabase.child("Sach").push().setValue(sach2);
//
//
//        mDatabase.child("TacGia").push().setValue(new TacGia("NL", "Nhật Lan"));
//        mDatabase.child("TacGiaChiTiet").push().setValue(new TacGiaChiTiet("TT003", "NL"));
//        Sach sach3 = new Sach("TT003",
//                "Đừng ghét tuyết rơi",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book115.jpg?alt=media&token=1885d889-6910-4065-8feb-725999e42a03",
//                232,
//                2017,
//                220000,
//                "Đừng ghét tuyết rơi là một cuốn sách lạ lùng nhưng không kém phần lãng mạn, sẽ đẩy trí tưởng tượng và cảm xúc của bạn lên tới tận cùng. Cuốn sách viết về tình yêu của hai con người trẻ tuổi, họ vô tình gặp gỡ nhau giữa ngày đầu tiên chàng trai bước vào cổng trường Đại học và từ đó, một câu chuyện tình lặng lẽ bắt đầu. Tất cả tựa như sắp xếp ngẫu nhiên của định mệnh, và càng lạ lùng hơn khi nó được gắn với một lời nguyền về chuỗi-tử-tình.  ",
//                "VH");
//        mDatabase.child("Sach").push().setValue(sach3);
//
//        mDatabase.child("TacGiaChiTiet").push().setValue(new TacGiaChiTiet("TV001", "HLT"));
//        mDatabase.child("TacGiaChiTiet").push().setValue(new TacGiaChiTiet("TV001", "HV"));
//        Sach sach4 = new Sach("TV001",
//                "Yêu đi rồi khóc",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book116.jpg?alt=media&token=2267047f-9978-41b0-b0a1-1e534d0cacba",
//                336,
//                2014,
//                170000,
//                "Yêu đi rồi khóc cũng là lần đầu chúng tôi cùng nhau kết hợp, dùng truyện ngắn để mang đến cho các bạn xúc cảm dạt dào của thứ quà tặng lung linh kỳ ảo nhất cuộc đời: Tình yêu. “Khóc” trong Yêu đi rồi khóc rất đa nghĩa nhé! Có thể ta khóc vì buồn, vì bị lừa dối, nhưng cũng có khi ta khóc vì ta quá hạnh phúc, hay một phút giây nào đó nhận ra mình đã yêu thương một người hơn chính bản thân mình!",
//                "VH");
//        mDatabase.child("Sach").push().setValue(sach4);
//
//
//        TheLoai theLoai = new TheLoai(4, "Truyện ngắn",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book117.jpg?alt=media&token=3ae754eb-c55b-4cb4-b26e-88f4331f4528");
//        mDatabase.child("TheLoai").push().setValue(theLoai);
//        mDatabase.child("TacGia").push().setValue(new TacGia("NhieuTG", "Nhiều Tác Giả"));
//        mDatabase.child("NhaXuatBan").push().setValue(new NhaXuatBan("VHTT", "Văn hóa thông tin"));
//        mDatabase.child("TacGiaChiTiet").push().setValue(new TacGiaChiTiet("TN001", "NhieuTG"));
//        Sach sach5 = new Sach("TN001",
//                "Người ta dễ buồn vì những truyện đã cũ",
//                "https://firebasestorage.googleapis.com/v0/b/bookstore-13e40.appspot.com/o/Book117.jpg?alt=media&token=3ae754eb-c55b-4cb4-b26e-88f4331f4528",
//                220,
//                2017,
//                220000,
//                "Gió mùa gõ nhẹ vào bầu trời xám xịt, từng giọt long lanh rơi. Lại một năm trôi. Tháng tháng năm qua đi khiến ta đôi lúc vấn vương ngoảnh lại, những ngày tuổi trẻ rực rỡ, trong trẻo đã qua đi, đã vẽ vào kí ức những mảng màu khó hiểu, độc đáo, vừa tươi vui, sôi nổi, vừa buồn bã, thê lương. Tuổi trẻ là thế, có ai hiểu được đâu, có ai nắm bắt được đâu. Chỉ khi đã lẩn mình trong những ấm êm, ta mới nhìn lại những gì ta đã trải nghiệm, mới thấy tiếc, thấy buồn." +
//                        "\n" +
//                        "Người ta dễ buồn vì những điều đã cũ gửi gắm vào tim người đọc những gì tươi đẹp nhất, quý giá nhất của đời người, để ta mãi mãi trân trọng nó, để xoa dịu những nỗi buồn kí ức, để sống với đời tươi vui và xinh đẹp như đóa hoa nở muộn. Cuốn sách có lẽ là món quà ta tự thưởng cho mình sau một năm đầy những buồn thương dai dẳng hay mệt nhoài kiệt sức.",
//                "VHTT");
//        mDatabase.child("Sach").push().setValue(sach5);
//
//        mDatabase.child("LoaiSach").push().setValue(new LoaiSach("TV001", 1));
//        mDatabase.child("LoaiSach").push().setValue(new LoaiSach("TN001", 4));
//        mDatabase.child("LoaiSach").push().setValue(new LoaiSach("TT002", 3));
//        mDatabase.child("LoaiSach").push().setValue(new LoaiSach("TT003", 3));
//        mDatabase.child("LoaiSach").push().setValue(new LoaiSach("T001", 2));
//        mDatabase.child("LoaiSach").push().setValue(new LoaiSach("T002", 2));

    }

    private void GetTheLoai() {
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

    private void GetNXB() {
//        NhaXuatBan nxb = new NhaXuatBan("VH","Văn học");
//        mDatabase.child("NhaXuatBan").push().setValue(nxb);
//        NhaXuatBan nxb1 = new NhaXuatBan("T","Trẻ");
//        mDatabase.child("NhaXuatBan").push().setValue(nxb1);
//        NhaXuatBan nxb2 = new NhaXuatBan("DT","Dân trí");
//        mDatabase.child("NhaXuatBan").push().setValue(nxb2);
    }
}
