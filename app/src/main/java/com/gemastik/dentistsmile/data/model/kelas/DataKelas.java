package com.gemastik.dentistsmile.data.model.kelas;

import com.google.gson.annotations.SerializedName;

public class DataKelas {

    @SerializedName("id")
    private Integer id;
    @SerializedName("id_sekolah")
    private Integer id_sekolah;
    @SerializedName("kelas")
    private String kelas;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_sekolah() {
        return id_sekolah;
    }

    public void setId_sekolah(Integer id_sekolah) {
        this.id_sekolah = id_sekolah;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
