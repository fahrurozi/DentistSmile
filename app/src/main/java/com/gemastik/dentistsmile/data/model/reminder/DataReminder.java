package com.gemastik.dentistsmile.data.model.reminder;

import com.google.gson.annotations.SerializedName;

public class DataReminder {
    @SerializedName("id_orangtua")
    private Integer id_orangtua;
    @SerializedName("anak")
    private String anak;
    @SerializedName("puskesmas")
    private String puskesmas;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("deskripsi")
    private String deskripsi;

    public Integer getId_orangtua() {
        return id_orangtua;
    }

    public void setId_orangtua(Integer id_orangtua) {
        this.id_orangtua = id_orangtua;
    }

    public String getAnak() {
        return anak;
    }

    public void setAnak(String anak) {
        this.anak = anak;
    }

    public String getPuskesmas() {
        return puskesmas;
    }

    public void setPuskesmas(String puskesmas) {
        this.puskesmas = puskesmas;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
