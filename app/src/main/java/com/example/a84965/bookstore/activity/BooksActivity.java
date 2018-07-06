package com.example.a84965.bookstore.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.BooksAdapter;
import com.example.a84965.bookstore.model.Sach;
import com.example.a84965.bookstore.model.LoaiSach;
import com.example.a84965.bookstore.ultil.GetChildFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__books);
        callControls();
        initToolbar();
        initBookList();
    }

    /**
     * Khởi tạo giá trị đầu vào
     */
    private void callControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.listView_Books);
    }

    /**
     * Khởi tạo danh sách Sách theo từng thể loại
     */
    private void initBookList(){
        Intent intent = getIntent();
        final int TL_Ma = intent.getIntExtra("TL_Ma",-1);
        final ArrayList<Sach> listBooks = new ArrayList<>();
        final BooksAdapter _booksAdapter;
        _booksAdapter = new BooksAdapter(this,listBooks);
        listView.setAdapter(_booksAdapter);

        mDatabase.child("LoaiSach").addChildEventListener(new GetChildFireBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final LoaiSach loaiSach = dataSnapshot.getValue(LoaiSach.class);
                if(loaiSach.getTL_Ma() == TL_Ma){

                    mDatabase.child("Sach").addChildEventListener(new GetChildFireBase() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Sach sach = dataSnapshot.getValue(Sach.class);
                            if(sach.getSach_Ma().equals(loaiSach.getSach_Ma())){
                                listBooks.add(sach);
                                _booksAdapter.notifyDataSetChanged();
                            }
                            super.onChildAdded(dataSnapshot, s);
                        }
                    });
                }
                super.onChildAdded(dataSnapshot, s);
            }
        });

    }

    /**
     * Khởi tạo toolbar
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_Books);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String TL_Ten = intent.getStringExtra("TL_Ten");
        setTitle(TL_Ten);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
