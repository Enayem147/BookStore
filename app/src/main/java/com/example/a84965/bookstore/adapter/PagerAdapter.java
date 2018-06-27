package com.example.a84965.bookstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.a84965.bookstore.fragment.GioiThieu;
import com.example.a84965.bookstore.fragment.ThongTin;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position){
            case 0:
                frag = new ThongTin();
                break;
            case 1:
                frag = new GioiThieu();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch (position){
            case 0:
                title = "Thông tin";
                break;
            case 1:
                title = "Giới thiệu";
                break;
        }
        return title;
    }
}
