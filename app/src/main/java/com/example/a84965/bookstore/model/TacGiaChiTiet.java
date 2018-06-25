package com.example.a84965.bookstore.model;

public class TacGiaChiTiet {
    private String Sach_Ma;
    private String TG_Ma;

    public TacGiaChiTiet(String sach_Ma, String TG_Ma) {
        Sach_Ma = sach_Ma;
        this.TG_Ma = TG_Ma;
    }

    public TacGiaChiTiet() {
    }

    public String getSach_Ma() {
        return Sach_Ma;
    }

    public void setSach_Ma(String sach_Ma) {
        Sach_Ma = sach_Ma;
    }

    public String getTG_Ma() {
        return TG_Ma;
    }

    public void setTG_Ma(String TG_Ma) {
        this.TG_Ma = TG_Ma;
    }
}
