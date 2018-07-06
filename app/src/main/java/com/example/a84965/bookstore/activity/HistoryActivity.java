package com.example.a84965.bookstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.HistoryAdapter;

import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    HistoryAdapter _historyAdapter;
    ListView listViewHistory;
    TextView txtEmptyHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        callControls();
        initHistory();
    }

    /**
     * Khởi tạo lịch sử mua hàng
     */
    private void initHistory() {
        if(HomePage.lichSu.size() > 0){
            Collections.reverse(HomePage.lichSu);
            _historyAdapter = new HistoryAdapter(HomePage.lichSu,getApplicationContext());
            listViewHistory.setAdapter(_historyAdapter);
            _historyAdapter.notifyDataSetChanged();
            txtEmptyHistory.setVisibility(View.INVISIBLE);
            listViewHistory.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Khởi tạo giá trị đầu vào
     */
    private void callControls() {
        toolbar = findViewById(R.id.toolbar_History);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        setTitle(R.string.lich_su_mua_hang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listViewHistory = findViewById(R.id.listView_History);
        listViewHistory.setVisibility(View.INVISIBLE);
        txtEmptyHistory = findViewById(R.id.txt_emptyHistory);
    }
}
