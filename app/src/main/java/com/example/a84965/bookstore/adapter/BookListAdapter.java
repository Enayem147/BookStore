package com.example.a84965.bookstore.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.activity.BookDetailActivity;
import com.example.a84965.bookstore.model.NhaXuatBan;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.TatCaSach;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class BookListAdapter extends BaseAdapter {

    ArrayList<TatCaSach> list;
    ArrayList<TatCaSach> arrayListSearch;
    Activity context;
    private DatabaseReference mDatabase;
    String nhaXB = "";

    public BookListAdapter(Activity context, ArrayList<TatCaSach> list) {
        this.list = list;
        this.context = context;
        this.arrayListSearch = new ArrayList<TatCaSach>();
        this.arrayListSearch.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        convertView = inflater.inflate(R.layout.list_view_book_list, null);

        TextView txtTen = convertView.findViewById(R.id.txtBookList_Ten);
        TextView txtTG = convertView.findViewById(R.id.txtBookList_TG);
        TextView txtTL = convertView.findViewById(R.id.txtBookList_TL);
        TextView txtGia = convertView.findViewById(R.id.txtBookList_Gia);
        ImageView imgHinhAnh = convertView.findViewById(R.id.imgBookList);

        final TatCaSach tatCaSach = (TatCaSach) getItem(position);

        txtTen.setText(tatCaSach.getSach_Ten());
        txtGia.setText(decimalFormat.format(tatCaSach.getSach_DonGia()) + " đ");

        //setText Tác giả
        final ArrayList<String> listTG = tatCaSach.getListTG();
        String strTG = "";
        strTG = listTG.get(0);
        for (int i = 1; i < listTG.size(); i++) {
            strTG += " , " + listTG.get(i);
        }
        txtTG.setText(strTG);

        //setText Thể loại
        ArrayList<String> listTL = tatCaSach.getListTL();
        String strTL = "";
        strTL = listTL.get(0);
        for (int i = 1; i < listTL.size(); i++) {
            strTL += " , " + listTL.get(i);
        }
        txtTL.setText(strTL);
        //set hình ảnh
        Picasso.get()
                .load(tatCaSach.getSach_HinhAnh())
                .into(imgHinhAnh);

        //Lấy tên nhà xuất bản
        mDatabase.child("NhaXuatBan").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NhaXuatBan nxb = dataSnapshot.getValue(NhaXuatBan.class);
                if (nxb.getNXB_Ma().equals(tatCaSach.getNXB_Ma())) {
                    nhaXB = nxb.getNXB_Ten();
                }
                super.onChildAdded(dataSnapshot, s);
            }
        });

        //event click
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("Sach", new Sach(tatCaSach.getSach_Ma(),
                        tatCaSach.getSach_Ten(),
                        tatCaSach.getSach_HinhAnh(),
                        tatCaSach.getSach_SoTrang(),
                        tatCaSach.getSach_NamXB(),
                        tatCaSach.getSach_DonGia(),
                        tatCaSach.getSach_GioiThieu(),
                        tatCaSach.getNXB_Ma()));
                intent.putExtra("NhaXuatBan", nhaXB);
                intent.putExtra("TacGia", listTG);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    //Filter Class


    public void filter(String charText) {
        charText =charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arrayListSearch);
        } else {
            for (int j=0;j<arrayListSearch.size();j++) {
                TatCaSach all = arrayListSearch.get(j);

                // tìm kiếm theo tên
                if (removeAccent(all.getSach_Ten().toLowerCase(Locale.getDefault())).contains(charText)) {
                        list.add(all);
                }

//                // tìm kiếm theo tên Tác Giả
//                for (int i = 0; i < all.getListTG().size(); i++) {
//                    if(removeAccent(all.getListTG().get(i).toLowerCase(Locale.getDefault())).contains(charText)){
//                            list.add(all);
//                    }
//                }
//
//                // tìm kiếm theo Thể Loại
//                for (int i = 0; i < all.getListTL().size(); i++) {
//                    if(removeAccent(all.getListTL().get(i).toLowerCase(Locale.getDefault())).contains(charText)){
//                            list.add(all);
//                    }
//                }

            }
        }
        notifyDataSetChanged();
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
}
