package com.example.a84965.bookstore.model;

public class KhachHang {

    private String KH_SDT;
    private String KH_MK;
    private String KH_HoTen;
    private String KH_DiaChi;

    public KhachHang() {
    }



    public KhachHang(String KH_SDT, String KH_MK, String KH_HoTen, String KH_DiaChi) {
        this.KH_SDT = KH_SDT;
        this.KH_MK = KH_MK;
        this.KH_HoTen = KH_HoTen;
        this.KH_DiaChi = KH_DiaChi;
    }

    public String getKH_SDT() {
        return KH_SDT;
    }

    public void setKH_SDT(String KH_SDT) {
        this.KH_SDT = KH_SDT;
    }

    public String getKH_MK() {
        return KH_MK;
    }

    public void setKH_MK(String KH_MK) {
        this.KH_MK = KH_MK;
    }

    public String getKH_HoTen() {
        return KH_HoTen;
    }

    public void setKH_HoTen(String KH_HoTen) {
        this.KH_HoTen = KH_HoTen;
    }

    public String getKH_DiaChi() {
        return KH_DiaChi;
    }

    public void setKH_DiaChi(String KH_DiaChi) {
        this.KH_DiaChi = KH_DiaChi;
    }
}
