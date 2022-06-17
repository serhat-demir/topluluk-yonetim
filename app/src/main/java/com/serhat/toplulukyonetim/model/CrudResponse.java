package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrudResponse {
    @SerializedName("durum")
    @Expose
    private String durum;

    @SerializedName("mesaj")
    @Expose
    private String mesaj;

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}