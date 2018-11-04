package com.example.a84965.bookstore.model;

public class DanhGia {
    private String KH_SDT;
    private String DG_ChuDe;
    private String DG_NoiDung;
    private float DG_XepHang;

    public DanhGia() {
    }

    public DanhGia(String KH_SDT, String DG_ChuDe, String DG_NoiDung, float DG_XepHang) {
        this.KH_SDT = KH_SDT;
        this.DG_ChuDe = DG_ChuDe;
        this.DG_NoiDung = DG_NoiDung;
        this.DG_XepHang = DG_XepHang;
    }

    public String getKH_SDT() {
        return KH_SDT;
    }

    public void setKH_SDT(String KH_SDT) {
        this.KH_SDT = KH_SDT;
    }

    public String getDG_ChuDe() {
        return DG_ChuDe;
    }

    public void setDG_ChuDe(String DG_ChuDe) {
        this.DG_ChuDe = DG_ChuDe;
    }

    public String getDG_NoiDung() {
        return DG_NoiDung;
    }

    public void setDG_NoiDung(String DG_NoiDung) {
        this.DG_NoiDung = DG_NoiDung;
    }

    public float getDG_XepHang() {
        return DG_XepHang;
    }

    public void setDG_XepHang(float DG_XepHang) {
        this.DG_XepHang = DG_XepHang;
    }
}
