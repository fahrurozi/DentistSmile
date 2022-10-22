package com.gemastik.dentistsmile.data.model.checkup_dentist;

import com.google.gson.annotations.SerializedName;

public class DataCheckupDentist {
    @SerializedName("id")
    private Integer id;

    @SerializedName("id_anak")
    private String id_anak;

    @SerializedName("id_sekolah")
    private String id_sekolah;

    @SerializedName("id_kelas")
    private String id_kelas;

    @SerializedName("waktu_pemeriksaan")
    private String waktu_pemeriksaan;

    @SerializedName("gsoal1")
    private String gsoal1;

    @SerializedName("gsoal2")
    private String gsoal2;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("gambar1")
    private String gambar1;

    @SerializedName("gambar2")
    private String gambar2;

    @SerializedName("gambar3")
    private String gambar3;

    @SerializedName("gambar4")
    private String gambar4;

    @SerializedName("gambar5")
    private String gambar5;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId_anak() {
        return id_anak;
    }

    public void setId_anak(String id_anak) {
        this.id_anak = id_anak;
    }

    public String getId_sekolah() {
        return id_sekolah;
    }

    public void setId_sekolah(String id_sekolah) {
        this.id_sekolah = id_sekolah;
    }

    public String getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(String id_kelas) {
        this.id_kelas = id_kelas;
    }

    public String getWaktu_pemeriksaan() {
        return waktu_pemeriksaan;
    }

    public void setWaktu_pemeriksaan(String waktu_pemeriksaan) {
        this.waktu_pemeriksaan = waktu_pemeriksaan;
    }

    public String getGsoal1() {
        return gsoal1;
    }

    public void setGsoal1(String gsoal1) {
        this.gsoal1 = gsoal1;
    }

    public String getGsoal2() {
        return gsoal2;
    }

    public void setGsoal2(String gsoal2) {
        this.gsoal2 = gsoal2;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGambar1() {
        return gambar1;
    }

    public void setGambar1(String gambar1) {
        this.gambar1 = gambar1;
    }

    public String getGambar2() {
        return gambar2;
    }

    public void setGambar2(String gambar2) {
        this.gambar2 = gambar2;
    }

    public String getGambar3() {
        return gambar3;
    }

    public void setGambar3(String gambar3) {
        this.gambar3 = gambar3;
    }

    public String getGambar4() {
        return gambar4;
    }

    public void setGambar4(String gambar4) {
        this.gambar4 = gambar4;
    }

    public String getGambar5() {
        return gambar5;
    }

    public void setGambar5(String gambar5) {
        this.gambar5 = gambar5;
    }
}
