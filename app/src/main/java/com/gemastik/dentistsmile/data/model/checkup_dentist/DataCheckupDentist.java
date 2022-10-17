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
}
