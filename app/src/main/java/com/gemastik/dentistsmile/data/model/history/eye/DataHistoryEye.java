package com.gemastik.dentistsmile.data.model.history.eye;

import com.google.gson.annotations.SerializedName;

public class DataHistoryEye {
    @SerializedName("id")
    private Integer id;

    @SerializedName("id_anak")
    private Integer id_anak;

    @SerializedName("hasil")
    private String hasil;

    @SerializedName("waktu_pemeriksaan")
    private String waktu_pemeriksaan;

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

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public String getWaktu_pemeriksaan() {
        return waktu_pemeriksaan;
    }

    public void setWaktu_pemeriksaan(String waktu_pemeriksaan) {
        this.waktu_pemeriksaan = waktu_pemeriksaan;
    }
}
