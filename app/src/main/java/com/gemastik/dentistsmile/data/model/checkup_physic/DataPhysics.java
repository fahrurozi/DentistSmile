package com.gemastik.dentistsmile.data.model.checkup_physic;

import com.google.gson.annotations.SerializedName;

public class DataPhysics {
    @SerializedName("id")
    private Integer id;

    @SerializedName("id_anak")
    private String id_anak;

    @SerializedName("id_sekolah")
    private String id_sekolah;

    @SerializedName("id_kelas")
    private String id_kelas;

    @SerializedName("tinggi_badan")
    private String tinggi_badan;

    @SerializedName("berat_badan")
    private String berat_badan;

    @SerializedName("imt")
    private Double imt;

    @SerializedName("sistole")
    private String sistole;

    @SerializedName("diastole")
    private String diastole;

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

    public String getTinggi_badan() {
        return tinggi_badan;
    }

    public void setTinggi_badan(String tinggi_badan) {
        this.tinggi_badan = tinggi_badan;
    }

    public String getBerat_badan() {
        return berat_badan;
    }

    public void setBerat_badan(String berat_badan) {
        this.berat_badan = berat_badan;
    }

    public Double getImt() {
        return imt;
    }

    public void setImt(Double imt) {
        this.imt = imt;
    }

    public String getSistole() {
        return sistole;
    }

    public void setSistole(String sistole) {
        this.sistole = sistole;
    }

    public String getDiastole() {
        return diastole;
    }

    public void setDiastole(String diastole) {
        this.diastole = diastole;
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
