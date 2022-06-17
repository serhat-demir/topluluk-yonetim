package com.serhat.toplulukyonetim.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KayitResponse {
    @SerializedName("kayitlar")
    @Expose
    private List<Kayit> kayitlar = null;

    public List<Kayit> getKayitlar() {
        return kayitlar;
    }

    public void setKayitlar(List<Kayit> kayitlar) {
        this.kayitlar = kayitlar;
    }
}