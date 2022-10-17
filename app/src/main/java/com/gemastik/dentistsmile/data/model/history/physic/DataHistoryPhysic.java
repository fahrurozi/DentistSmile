package com.gemastik.dentistsmile.data.model.history.physic;

import com.google.gson.annotations.SerializedName;

public class DataHistoryPhysic {
    @SerializedName("id")
    private Integer id;

    @SerializedName("id_anak")
    private Integer id_anak;

    @SerializedName("id_sekolah")
    private Integer id_sekolah;

    @SerializedName("id_kelas")
    private Integer id_kelas;

    @SerializedName("tinggi_badan")
    private Integer tinggi_badan;

    @SerializedName("berat_badan")
    private Integer berat_badan;

    @SerializedName("imt")
    private Double imt;

    @SerializedName("sistole")
    private Integer sistole;

    @SerializedName("diastole")
    private Integer diastole;

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

    public Integer getTinggi_badan() {
        return tinggi_badan;
    }

    public void setTinggi_badan(Integer tinggi_badan) {
        this.tinggi_badan = tinggi_badan;
    }

    public Integer getBerat_badan() {
        return berat_badan;
    }

    public void setBerat_badan(Integer berat_badan) {
        this.berat_badan = berat_badan;
    }

    public Double getImt() {
        return imt;
    }

    public void setImt(Double imt) {
        this.imt = imt;
    }

    public Integer getSistole() {
        return sistole;
    }

    public void setSistole(Integer sistole) {
        this.sistole = sistole;
    }

    public Integer getDiastole() {
        return diastole;
    }

    public void setDiastole(Integer diastole) {
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
