package com.example.a84965.bookstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.adapter.CustomerOrderAdapter;
import com.example.a84965.bookstore.model.DonHang;

import java.util.ArrayList;
import java.util.HashSet;

public class CustomerOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    LinearLayout linearLayoutCusOrder;
    TextView txtEmpty;
    ArrayList<DonHang> donHangCusOrder = new ArrayList<>();
    private CustomerOrderAdapter _cusOderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        callControls();
        initCusOrder();
    }

    private void initCusOrder() {
        if(HomePage.donHang == null)
            HomePage.donHang = new ArrayList<>();
        if(HomePage.donHang.size() > 0 ){
            donHangCusOrder = removeDuplicates(HomePage.donHang);
            _cusOderAdapter = new CustomerOrderAdapter(donHangCusOrder,this);
            txtEmpty.setVisibility(View.INVISIBLE);
            linearLayoutCusOrder.setVisibility(View.VISIBLE);
            listView.setAdapter(_cusOderAdapter);
            _cusOderAdapter.notifyDataSetChanged();
        }
    }

    private void callControls() {
        toolbar = findViewById(R.id.toolbar_CustomerOrder);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);
        setTitle(R.string.tinh_trang_don_hang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        linearLayoutCusOrder = findViewById(R.id.linerLayoutCusOrder);
        linearLayoutCusOrder.setVisibility(View.INVISIBLE);
        listView = findViewById(R.id.listView_CusOrder);
        txtEmpty = findViewById(R.id.txt_emptyCusOrder);
    }

    static ArrayList<DonHang> removeDuplicates(ArrayList<DonHang> list) {
        // khởi tạo kết quả
        ArrayList<String> checkResult = new ArrayList<>();
        ArrayList<DonHang> result = new ArrayList<>();
        // khợi tạo set đựng mã sách để kiểm tra
        HashSet<String> set = new HashSet<>();

        //loại bỏ các mã sách trùng nhau
        for (int i=0 ; i<list.size();i++){
            if(!set.contains(list.get(i).getDH_Ma())){
                checkResult.add(list.get(i).getDH_Ma());
                result.add(list.get(i));
                set.add(list.get(i).getDH_Ma());
            }
        }
        return result;
    }
}
