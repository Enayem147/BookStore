package com.example.a84965.bookstore.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.Adapter_Books;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.LoaiSach;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Activity_Books extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__books);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initActionBar();
        initBookList();
        clickBookListEvent();

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



    private void initBookList(){
        Intent intent = getIntent();
        final int TL_Ma = intent.getIntExtra("TL_Ma",-1);
        listView = findViewById(R.id.listView_Books);
        final ArrayList<Sach> listBooks = new ArrayList<>();

        final Adapter_Books adapter_books;
        adapter_books = new Adapter_Books(getApplicationContext(),listBooks);
        listView.setAdapter(adapter_books);

        mDatabase.child("LoaiSach").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final LoaiSach loaiSach = dataSnapshot.getValue(LoaiSach.class);
                if(loaiSach.getTL_Ma() == TL_Ma){

                    mDatabase.child("Sach").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Sach sach = dataSnapshot.getValue(Sach.class);
                            if(sach.getSach_Ma().equals(loaiSach.getSach_Ma())){
                                listBooks.add(sach);
                                adapter_books.notifyDataSetChanged();
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

    private  void clickBookListEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sach sach = (Sach)parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),Activity_Book_Detail.class);
                intent.putExtra("Sach_Ma",sach.getSach_Ma());
                startActivity(intent);
            }
        });
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.toolbar_Books);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String TL_Ten = intent.getStringExtra("TL_Ten");
        setTitle(TL_Ten);
    }
}
