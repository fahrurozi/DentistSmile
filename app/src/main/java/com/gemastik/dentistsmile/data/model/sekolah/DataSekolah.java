package com.gemastik.dentistsmile.data.model.sekolah;

import com.google.gson.annotations.SerializedName;

public class DataSekolah {
    @SerializedName("id")
    private Integer id;
    @SerializedName("id_kelurahan")
    private Integer id_kelurahan;
    @SerializedName("type")
    private String type;
    @SerializedName("nama")
    private String nama;
    @SerializedName("alamat")
    private String alamat;
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

    public Integer getId_kelurahan() {
        return id_kelurahan;
    }

    public void setId_kelurahan(Integer id_kelurahan) {
        this.id_kelurahan = id_kelurahan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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
