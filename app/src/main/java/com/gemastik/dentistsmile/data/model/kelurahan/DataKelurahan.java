package com.gemastik.dentistsmile.data.model.kelurahan;

import com.google.gson.annotations.SerializedName;

public class DataKelurahan {
    @SerializedName("id")
    private Integer id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("id_kecamatan")
    private Integer id_kecamatan;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getId_kecamatan() {
        return id_kecamatan;
    }

    public void setId_kecamatan(Integer id_kecamatan) {
        this.id_kecamatan = id_kecamatan;
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
