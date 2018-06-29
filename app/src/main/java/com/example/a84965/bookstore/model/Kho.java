package com.example.a84965.bookstore.model;

public class Kho {
    private String Sach_Ma;
    private int Kho_SoLuong;

    public Kho() {
    }

    public Kho(String sach_Ma, int kho_SoLuong) {
        Sach_Ma = sach_Ma;
        Kho_SoLuong = kho_SoLuong;
    }

    public String getSach_Ma() {
        return Sach_Ma;
    }

    public void setSach_Ma(String sach_Ma) {
        Sach_Ma = sach_Ma;
    }

    public int getKho_SoLuong() {
        return Kho_SoLuong;
    }

    public void setKho_SoLuong(int kho_SoLuong) {
        Kho_SoLuong = kho_SoLuong;
    }
}
