package com.example.a84965.bookstore.model;

public class DonHang {
    private String DH_Ma;
    private String Sach_Ma;
    private String Sach_Ten;
    private String Sach_HinhAnh;
    private int Sach_DonGia;
    private int Sach_SL;
    private String DH_NgayDat;
    private int DH_TrangThai;

    public DonHang() {
    }

    public DonHang(String DH_Ma, String sach_Ma, String sach_Ten, String sach_HinhAnh, int sach_DonGia, int sach_SL, String DH_NgayDat, int DH_TrangThai) {
        this.DH_Ma = DH_Ma;
        Sach_Ma = sach_Ma;
        Sach_Ten = sach_Ten;
        Sach_HinhAnh = sach_HinhAnh;
        Sach_DonGia = sach_DonGia;
        Sach_SL = sach_SL;
        this.DH_NgayDat = DH_NgayDat;
        this.DH_TrangThai = DH_TrangThai;
    }


    public String getDH_Ma() {
        return DH_Ma;
    }

    public void setDH_Ma(String DH_Ma) {
        this.DH_Ma = DH_Ma;
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

    public String getDH_NgayDat() {
        return DH_NgayDat;
    }

    public void setDH_NgayDat(String DH_NgayDat) {
        this.DH_NgayDat = DH_NgayDat;
    }

    public int getDH_TrangThai() {
        return DH_TrangThai;
    }

    public void setDH_TrangThai(int DH_TrangThai) {
        this.DH_TrangThai = DH_TrangThai;
    }
}
