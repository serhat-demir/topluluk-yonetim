package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Yorum {
    @SerializedName("yorum_id")
    @Expose
    private Integer yorumId;

    @SerializedName("yorum_icerik")
    @Expose
    private String yorumIcerik;

    @SerializedName("yorum_yazar")
    @Expose
    private Integer yorumYazar;

    @SerializedName("yorum_konu")
    @Expose
    private Integer yorumKonu;

    @SerializedName("yorum_tarih")
    @Expose
    private String yorumTarih;

    @SerializedName("yorum_yazar_ad")
    @Expose
    private String yorumYazarAd;

    @SerializedName("yorum_yazar_yetki")
    @Expose
    private Integer yorumYazarYetki;

    public Integer getYorumId() {
        return yorumId;
    }

    public void setYorumId(Integer yorumId) {
        this.yorumId = yorumId;
    }

    public String getYorumIcerik() {
        return yorumIcerik;
    }

    public void setYorumIcerik(String yorumIcerik) {
        this.yorumIcerik = yorumIcerik;
    }

    public Integer getYorumYazar() {
        return yorumYazar;
    }

    public void setYorumYazar(Integer yorumYazar) {
        this.yorumYazar = yorumYazar;
    }

    public Integer getYorumKonu() {
        return yorumKonu;
    }

    public void setYorumKonu(Integer yorumKonu) {
        this.yorumKonu = yorumKonu;
    }

    public String getYorumTarih() {
        return yorumTarih;
    }

    public void setYorumTarih(String yorumTarih) {
        this.yorumTarih = yorumTarih;
    }

    public String getYorumYazarAd() {
        return yorumYazarAd;
    }

    public void setYorumYazarAd(String yorumYazarAd) {
        this.yorumYazarAd = yorumYazarAd;
    }

    public Integer getYorumYazarYetki() {
        return yorumYazarYetki;
    }

    public void setYorumYazarYetki(Integer yorumYazarYetki) {
        this.yorumYazarYetki = yorumYazarYetki;
    }
}