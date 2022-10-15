package com.gemastik.dentistsmile.data.model.history.eye;

import com.google.gson.annotations.SerializedName;

public class DataHistoryEye {
    @SerializedName("id")
    private Integer id;

    @SerializedName("id_anak")
    private Integer id_anak;

    @SerializedName("id_sekolah")
    private Integer id_sekolah;

    @SerializedName("id_kelas")
    private Integer id_kelas;

    @SerializedName("msoal1")
    private String msoal1;

    @SerializedName("msoal2")
    private String msoal2;

    @SerializedName("msoal3")
    private String msoal3;

    @SerializedName("msoal4")
    private String msoal4;

    @SerializedName("msoal5")
    private String msoal5;

    @SerializedName("msoal6")
    private String msoal6;

    @SerializedName("msoal7")
    private String msoal7;

    @SerializedName("waktu_pemeriksaan")
    private String waktu_pemeriksaan;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("created_at")
    private String created_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_anak() {
        return id_anak;
    }

    public void setId_anak(Integer id_anak) {
        this.id_anak = id_anak;
    }

    public Integer getId_sekolah() {
        return id_sekolah;
    }

    public void setId_sekolah(Integer id_sekolah) {
        this.id_sekolah = id_sekolah;
    }

    public Integer getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(Integer id_kelas) {
        this.id_kelas = id_kelas;
    }

    public String getMsoal1() {
        return msoal1;
    }

    public void setMsoal1(String msoal1) {
        this.msoal1 = msoal1;
    }

    public String getMsoal2() {
        return msoal2;
    }

    public void setMsoal2(String msoal2) {
        this.msoal2 = msoal2;
    }

    public String getMsoal3() {
        return msoal3;
    }

    public void setMsoal3(String msoal3) {
        this.msoal3 = msoal3;
    }

    public String getMsoal4() {
        return msoal4;
    }

    public void setMsoal4(String msoal4) {
        this.msoal4 = msoal4;
    }

    public String getMsoal5() {
        return msoal5;
    }

    public void setMsoal5(String msoal5) {
        this.msoal5 = msoal5;
    }

    public String getMsoal6() {
        return msoal6;
    }

    public void setMsoal6(String msoal6) {
        this.msoal6 = msoal6;
    }

    public String getMsoal7() {
        return msoal7;
    }

    public void setMsoal7(String msoal7) {
        this.msoal7 = msoal7;
    }

    public String getWaktu_pemeriksaan() {
        return waktu_pemeriksaan;
    }

    public void setWaktu_pemeriksaan(String waktu_pemeriksaan) {
        this.waktu_pemeriksaan = waktu_pemeriksaan;
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
}
