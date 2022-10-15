package com.gemastik.dentistsmile.data.model.children.edit;

import com.google.gson.annotations.SerializedName;

public class DataEditChild {
    @SerializedName("id")
    private Integer id;

    @SerializedName("id_orangtua")
    private Integer id_orangtua;

    @SerializedName("nama")
    private String nama;

    @SerializedName("jenis_kelamin")
    private String jenis_kelamin;

    @SerializedName("tempat_lahir")
    private String tempat_lahir;

    @SerializedName("tanggal_lahir")
    private String tanggal_lahir;

    @SerializedName("update_at")
    private String update_at;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("deleted_at")
    private String delete_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_orangtua() {
        return id_orangtua;
    }

    public void setId_orangtua(Integer id_orangtua) {
        this.id_orangtua = id_orangtua;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(String delete_at) {
        this.delete_at = delete_at;
    }
}
