package com.example.a84965.bookstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.HashSet;

public class HistoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    HistoryAdapter _historyAdapter;
    ListView listViewHistory;
    TextView txtEmptyHistory;
    LinearLayout linearLayoutHistory;
    Spinner spinnerHistory;
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
        HomePage.listMaDH = removeDuplicates(HomePage.listMaDH);
        if(HomePage.lichSu.size() > 0){
            _historyAdapter = new HistoryAdapter(HomePage.lichSu,getApplicationContext());
            listViewHistory.setAdapter(_historyAdapter);
            _historyAdapter.notifyDataSetChanged();
            txtEmptyHistory.setVisibility(View.INVISIBLE);
            linearLayoutHistory.setVisibility(View.VISIBLE);
            ArrayAdapter arrayAdapter = new ArrayAdapter(HistoryActivity.this,R.layout.layout_spinner,HomePage.listMaDH);
            spinnerHistory.setAdapter(arrayAdapter);
            spinnerHistory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String maDH = parent.getSelectedItem().toString();
                    _historyAdapter.filter(maDH);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    /**
     * xóa các phần tử trùng nhau trong 1 list
     * @param list : Danh sách tất cả sách
     * @return
     */
    static ArrayList<String> removeDuplicates(ArrayList<String> list) {
        // khởi tạo kết quả
        ArrayList<String> result = new ArrayList<>();

        // khợi tạo set đựng các Sách kiểm tra
        HashSet<String> set = new HashSet<>();

        for (String item : list) {
            // đưa các item k có trong set vào result và set
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    /**
     * Khởi tạo giá trị đầu vào
     */
    private void callControls() {
        spinnerHistory = findViewById(R.id.spinnerHistory);
        linearLayoutHistory = findViewById(R.id.linerLayoutHistory);
        linearLayoutHistory.setVisibility(View.INVISIBLE);
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
        txtEmptyHistory = findViewById(R.id.txt_emptyHistory);
    }
}
