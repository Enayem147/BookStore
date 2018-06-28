package com.example.a84965.bookstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.BookListAdapter;
import com.example.a84965.bookstore.model.TatCaSach;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class BookListActivity extends AppCompatActivity {
    Toolbar toolbar;
    BookListAdapter _bookListAdapter;
    ListView listViewBookList;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        callControls();
        initBookList();
        searchBook();


    }

    private void searchBook(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _bookListAdapter.filter(removeAccent(newText.trim()));
                return true;
            }
        });
    }

    private void initBookList() {
        if(HomePage.tatCaSach.size() > 0 ){
            _bookListAdapter = new BookListAdapter(this,HomePage.tatCaSach);
            listViewBookList.setAdapter(_bookListAdapter);
            _bookListAdapter.notifyDataSetChanged();
        }
    }

    private void callControls() {
        searchView = findViewById(R.id.txtSearch_BookList);
        listViewBookList = findViewById(R.id.listView_BookList);
        toolbar = findViewById(R.id.toolbar_BookList);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        setTitle(R.string.tat_ca_sp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
}
