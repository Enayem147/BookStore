package com.example.a84965.bookstore.model;

public class QuangCao {
    private int QC_Ma;
    private String QC_HinhAnh;

    public QuangCao(){ }

    public QuangCao(int QC_Ma, String QC_HinhAnh) {
        this.QC_Ma = QC_Ma;
        this.QC_HinhAnh = QC_HinhAnh;
    }

    public int getQC_Ma() {
        return QC_Ma;
    }

    public void setQC_Ma(int QC_Ma) {
        this.QC_Ma = QC_Ma;
    }

    public String getQC_HinhAnh() {
        return QC_HinhAnh;
    }

    public void setQC_HinhAnh(String QC_HinhAnh) {
        this.QC_HinhAnh = QC_HinhAnh;
    }
}
