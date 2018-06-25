package com.example.a84965.bookstore.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.activity.Activity_Book_Detail;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.NhaXuatBan;
import com.example.a84965.bookstore.model.TacGia;
import com.example.a84965.bookstore.model.TacGiaChiTiet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ThongTin extends Fragment {
    private DatabaseReference mDatabase;
    public ThongTin() {
    }
    TextView txtTG,txtNhaXB,txtNamXB,txtSoTrang;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_tin,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtTG = view.findViewById(R.id.txt_tt_tacgia);
        txtNhaXB = view.findViewById(R.id.txt_tt_nhaxb);
        txtNamXB = view.findViewById(R.id.txt_tt_namxb);
        txtSoTrang = view.findViewById(R.id.txt_tt_sotrang);

        final Activity_Book_Detail activity = (Activity_Book_Detail)getActivity();

        mDatabase.child("Sach").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Sach sach = dataSnapshot.getValue(Sach.class);
                if(sach.getSach_Ma().equals(activity.getMaSach())){
                    txtNamXB.setText(sach.getSach_NamXB()+"");
                    txtSoTrang.setText(sach.getSach_SoTrang()+"");

                    //Lấy tên nhà xuất bản
                     mDatabase.child("NhaXuatBan").addChildEventListener(new ChildEventListener() {
                         @Override
                         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                             NhaXuatBan nxb = dataSnapshot.getValue(NhaXuatBan.class);
                             if(nxb.getNXB_Ma().equals(sach.getNXB_Ma())){
                                 txtNhaXB.setText(nxb.getNXB_Ten());
                             }
                         }

                         @Override
                         public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                         }

                         @Override
                         public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                         }

                         @Override
                         public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });
                    //Lấy tên tác giả
                    final List<String> listTG = new ArrayList<>();
                    mDatabase.child("TacGiaChiTiet").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            final TacGiaChiTiet tacGiaChiTiet = dataSnapshot.getValue(TacGiaChiTiet.class);
                            if(tacGiaChiTiet.getSach_Ma().equals(activity.getMaSach())){
                                mDatabase.child("TacGia").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        TacGia tacGia = dataSnapshot.getValue(TacGia.class);
                                        if(tacGia.getTG_Ma().equals(tacGiaChiTiet.getTG_Ma())){
                                            // lấy danh sách đồng tác giả
                                            listTG.add(tacGia.getTG_Ten());
                                            String strTG = "";
                                            strTG = listTG.get(0);
                                            for(int i=1;i<listTG.size();i++){
                                                strTG +=" , "+listTG.get(i);
                                            }
                                            txtTG.setText(strTG);
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        txtTG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), txtTG.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }



}
