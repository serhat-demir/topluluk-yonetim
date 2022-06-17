package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kayit {
    @SerializedName("kayit_id")
    @Expose
    private Integer kayitId;

    @SerializedName("kayit_topluluk")
    @Expose
    private Integer kayitTopluluk;

    @SerializedName("kayit_icerik")
    @Expose
    private String kayitIcerik;

    @SerializedName("kayit_tarih")
    @Expose
    private String kayitTarih;

    public Integer getKayitId() {
        return kayitId;
    }

    public void setKayitId(Integer kayitId) {
        this.kayitId = kayitId;
    }

    public Integer getKayitTopluluk() {
        return kayitTopluluk;
    }

    public void setKayitTopluluk(Integer kayitTopluluk) {
        this.kayitTopluluk = kayitTopluluk;
    }

    public String getKayitIcerik() {
        return kayitIcerik;
    }

    public void setKayitIcerik(String kayitIcerik) {
        this.kayitIcerik = kayitIcerik;
    }

    public String getKayitTarih() {
        return kayitTarih;
    }

    public void setKayitTarih(String kayitTarih) {
        this.kayitTarih = kayitTarih;
    }
}