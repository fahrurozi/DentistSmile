package com.gemastik.dentistsmile.data.model.review;

import com.google.gson.annotations.SerializedName;

public class DataPerRating {
    @SerializedName("1")
    private Integer satu;

    @SerializedName("2")
    private Integer dua;

    @SerializedName("3")
    private Integer tiga;

    @SerializedName("4")
    private Integer empat;

    @SerializedName("5")
    private Integer lima;

    public DataPerRating(Integer satu, Integer dua, Integer tiga, Integer empat, Integer lima) {
        this.satu = satu;
        this.dua = dua;
        this.tiga = tiga;
        this.empat = empat;
        this.lima = lima;
    }

    public Integer getSatu() {
        return satu;
    }

    public void setSatu(Integer satu) {
        this.satu = satu;
    }

    public Integer getDua() {
        return dua;
    }

    public void setDua(Integer dua) {
        this.dua = dua;
    }

    public Integer getTiga() {
        return tiga;
    }

    public void setTiga(Integer tiga) {
        this.tiga = tiga;
    }

    public Integer getEmpat() {
        return empat;
    }

    public void setEmpat(Integer empat) {
        this.empat = empat;
    }

    public Integer getLima() {
        return lima;
    }

    public void setLima(Integer lima) {
        this.lima = lima;
    }
}
