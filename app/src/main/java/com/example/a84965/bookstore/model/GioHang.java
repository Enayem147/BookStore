package com.example.a84965.bookstore.model;

public class GioHang {
    private String Sach_Ma;
    private String Sach_Ten;
    private String Sach_HinhAnh;
    private int Sach_DonGia;
    private int Sach_SL;

    public GioHang() {
    }

    public GioHang(String sach_Ma, String sach_Ten, String sach_HinhAnh, int sach_DonGia, int sach_SL) {
        Sach_Ma = sach_Ma;
        Sach_Ten = sach_Ten;
        Sach_HinhAnh = sach_HinhAnh;
        Sach_DonGia = sach_DonGia;
        Sach_SL = sach_SL;
    }

    public String getSach_Ma() {
        return Sach_Ma;
    }

    public void setSach_Ma(String sach_Ma) {
        Sach_Ma = sach_Ma;
    }

    public String getSach_Ten() {
        return Sach_Ten;
    }

    public void setSach_Ten(String sach_Ten) {
        Sach_Ten = sach_Ten;
    }

    public String getSach_HinhAnh() {
        return Sach_HinhAnh;
    }

    public void setSach_HinhAnh(String sach_HinhAnh) {
        Sach_HinhAnh = sach_HinhAnh;
    }

    public int getSach_DonGia() {
        return Sach_DonGia;
    }

    public void setSach_DonGia(int sach_DonGia) {
        Sach_DonGia = sach_DonGia;
    }

    public int getSach_SL() {
        return Sach_SL;
    }

    public void setSach_SL(int sach_SL) {
        Sach_SL = sach_SL;
    }
}
