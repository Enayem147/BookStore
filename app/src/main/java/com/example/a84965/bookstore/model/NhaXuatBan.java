package com.example.a84965.bookstore.model;

public class NhaXuatBan {
    private String NXB_Ma;
    private String NXB_Ten;

    public NhaXuatBan() {
    }

    public NhaXuatBan(String NXB_Ma, String NXB_Ten) {
        this.NXB_Ma = NXB_Ma;
        this.NXB_Ten = NXB_Ten;
    }

    public String getNXB_Ma() {
        return NXB_Ma;
    }

    public void setNXB_Ma(String NXB_Ma) {
        this.NXB_Ma = NXB_Ma;
    }

    public String getNXB_Ten() {
        return NXB_Ten;
    }

    public void setNXB_Ten(String NXB_Ten) {
        this.NXB_Ten = NXB_Ten;
    }
}
