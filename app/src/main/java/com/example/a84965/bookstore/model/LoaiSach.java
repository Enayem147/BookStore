package com.example.a84965.bookstore.model;

public class LoaiSach {
    private String Sach_Ma;
    private int TL_Ma;

    public LoaiSach(){}

    public LoaiSach(String sach_Ma, int TL_Ma) {
        Sach_Ma = sach_Ma;
        this.TL_Ma = TL_Ma;
    }

    public String getSach_Ma() {
        return Sach_Ma;
    }

    public void setSach_Ma(String sach_Ma) {
        Sach_Ma = sach_Ma;
    }

    public int getTL_Ma() {
        return TL_Ma;
    }

    public void setTL_Ma(int TL_Ma) {
        this.TL_Ma = TL_Ma;
    }
}
