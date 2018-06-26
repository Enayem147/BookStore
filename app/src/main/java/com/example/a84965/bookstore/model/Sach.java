package com.example.a84965.bookstore.model;

import java.io.Serializable;

public class Sach implements Serializable {
    private String Sach_Ma;
    private String Sach_Ten;
    private String Sach_HinhAnh;
    private int Sach_SoTrang;
    private int Sach_NamXB;
    private int Sach_DonGia;
    private String Sach_GioiThieu;
    private String NXB_Ma;

    public Sach() {
    }

    public Sach(String sach_Ma, String sach_Ten, String sach_HinhAnh, int sach_SoTrang, int sach_NamXB, int sach_DonGia, String sach_GioiThieu, String NXB_Ma) {
        Sach_Ma = sach_Ma;
        Sach_Ten = sach_Ten;
        Sach_HinhAnh = sach_HinhAnh;
        Sach_SoTrang = sach_SoTrang;
        Sach_NamXB = sach_NamXB;
        Sach_DonGia = sach_DonGia;
        Sach_GioiThieu = sach_GioiThieu;
        this.NXB_Ma = NXB_Ma;
    }
    // Constructor cho new books
    public Sach(String sach_Ma, String sach_Ten, String sach_HinhAnh) {
        Sach_Ma = sach_Ma;
        Sach_Ten = sach_Ten;
        Sach_HinhAnh = sach_HinhAnh;
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

    public int getSach_SoTrang() {
        return Sach_SoTrang;
    }

    public void setSach_SoTrang(int sach_SoTrang) {
        Sach_SoTrang = sach_SoTrang;
    }

    public int getSach_NamXB() {
        return Sach_NamXB;
    }

    public void setSach_NamXB(int sach_NamXB) {
        Sach_NamXB = sach_NamXB;
    }

    public int getSach_DonGia() {
        return Sach_DonGia;
    }

    public void setSach_DonGia(int sach_DonGia) {
        Sach_DonGia = sach_DonGia;
    }

    public String getSach_GioiThieu() {
        return Sach_GioiThieu;
    }

    public void setSach_GioiThieu(String sach_GioiThieu) {
        Sach_GioiThieu = sach_GioiThieu;
    }

    public String getNXB_Ma() {
        return NXB_Ma;
    }

    public void setNXB_Ma(String NXB_Ma) {
        this.NXB_Ma = NXB_Ma;
    }
}
