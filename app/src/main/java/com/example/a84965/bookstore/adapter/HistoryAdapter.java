package com.example.a84965.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a84965.bookstore.R;
import com.example.a84965.bookstore.model.DonHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    ArrayList<DonHang> list ;
    ArrayList<DonHang> arrayListSearch;
    Context context;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    public HistoryAdapter(ArrayList<DonHang> list, Context context) {
        this.list = list;
        this.context = context;
        arrayListSearch = new ArrayList<>();
        arrayListSearch.addAll(list);
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
        final TextView txtTenSach, txtSoLuong, txtGia , txtNgayMua;
        ImageView imgHinhAnh;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_view_history, null);
        txtTenSach = convertView.findViewById(R.id.txtHistory_TenSach);
        txtSoLuong = convertView.findViewById(R.id.txtHistory_SL);
        txtGia = convertView.findViewById(R.id.txtHistory_Gia);
        txtNgayMua = convertView.findViewById(R.id.txtHistory_NgayMua);
        imgHinhAnh = convertView.findViewById(R.id.imgHistory_HinhSach);

        // khởi tạo các giá trị cho listGioHang view Lịch Sử
        DonHang donHang = (DonHang) getItem(position);
        txtSoLuong.setText(donHang.getSach_SL() + "");
        txtTenSach.setText(donHang.getSach_Ten());
        txtGia.setText(decimalFormat.format(donHang.getSach_DonGia())+ " đ");
        txtNgayMua.setText(donHang.getDH_NgayDat());

        Picasso.get()
                .load(donHang.getSach_HinhAnh())
                .into(imgHinhAnh);

        return convertView;
    }

    /**
     * Hàm lọc - tìm kiếm
     * @param charText : chuỗi cần tìm
     */
    public void filter(String charText) {
        list.clear();
        if (charText.equals("Mã đơn hàng")) {
            list.addAll(0,arrayListSearch);
        } else {
            for (int j=0;j<arrayListSearch.size();j++) {
                DonHang donHang = arrayListSearch.get(j);
                // tìm kiếm theo mã hóa đơn
                if ((donHang.getDH_Ma().equals(charText))) {
                    list.add(donHang);
                }
            }
        }
        notifyDataSetChanged();
    }
}
