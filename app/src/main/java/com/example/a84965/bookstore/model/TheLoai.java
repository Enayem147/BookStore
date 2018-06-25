package com.example.a84965.bookstore.model;

public class TheLoai {
    private int TL_Ma;
    private String TL_Ten;
    private String TL_Anh;

    public TheLoai() {
    }

    public TheLoai(int TL_Ma, String TL_Ten , String TL_Anh) {
        this.TL_Ma = TL_Ma;
        this.TL_Ten = TL_Ten;
        this.TL_Anh = TL_Anh;
    }

    public int getTL_Ma() {
        return TL_Ma;
    }

    public void setTL_Ma(int TL_Ma) {
        this.TL_Ma = TL_Ma;
    }

    public String getTL_Ten() {
        return TL_Ten;
    }

    public void setTL_Ten(String TL_Ten) {
        this.TL_Ten = TL_Ten;
    }

    public String getTL_Anh() {
        return TL_Anh;
    }

    public void setTL_Anh(String TL_Anh) {
        this.TL_Anh = TL_Anh;
    }
}
