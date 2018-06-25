package com.example.a84965.bookstore.model;

public class TacGia {
    private String TG_Ma;
    private String TG_Ten;

    public TacGia(){}

    public TacGia(String TG_Ma, String TG_Ten) {
        this.TG_Ma = TG_Ma;
        this.TG_Ten = TG_Ten;
    }

    public String getTG_Ma() {
        return TG_Ma;
    }

    public void setTG_Ma(String TG_Ma) {
        this.TG_Ma = TG_Ma;
    }

    public String getTG_Ten() {
        return TG_Ten;
    }

    public void setTG_Ten(String TG_Ten) {
        this.TG_Ten = TG_Ten;
    }
}
