package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Konu {
    @SerializedName("konu_id")
    @Expose
    private Integer konuId;

    @SerializedName("konu_baslik")
    @Expose
    private String konuBaslik;

    @SerializedName("konu_icerik")
    @Expose
    private String konuIcerik;

    @SerializedName("konu_yazar")
    @Expose
    private Integer konuYazar;

    @SerializedName("konu_topluluk")
    @Expose
    private Integer konuTopluluk;

    @SerializedName("konu_tarih")
    @Expose
    private String konuTarih;

    @SerializedName("konu_sabit")
    @Expose
    private Integer konuSabit;

    @SerializedName("konu_yorum_sayisi")
    @Expose
    private Integer konuYorumSayisi;

    @SerializedName("konu_yazar_ad")
    @Expose
    private String konuYazarAd;

    @SerializedName("konu_yazar_yetki")
    @Expose
    private Integer konuYazarYetki;

    public Integer getKonuId() {
        return konuId;
    }

    public void setKonuId(Integer konuId) {
        this.konuId = konuId;
    }

    public String getKonuBaslik() {
        return konuBaslik;
    }

    public void setKonuBaslik(String konuBaslik) {
        this.konuBaslik = konuBaslik;
    }

    public String getKonuIcerik() {
        return konuIcerik;
    }

    public void setKonuIcerik(String konuIcerik) {
        this.konuIcerik = konuIcerik;
    }

    public Integer getKonuYazar() {
        return konuYazar;
    }

    public void setKonuYazar(Integer konuYazar) {
        this.konuYazar = konuYazar;
    }

    public Integer getKonuTopluluk() {
        return konuTopluluk;
    }

    public void setKonuTopluluk(Integer konuTopluluk) {
        this.konuTopluluk = konuTopluluk;
    }

    public String getKonuTarih() {
        return konuTarih;
    }

    public void setKonuTarih(String konuTarih) {
        this.konuTarih = konuTarih;
    }

    public Integer getKonuSabit() {
        return konuSabit;
    }

    public void setKonuSabit(Integer konuSabit) {
        this.konuSabit = konuSabit;
    }

    public Integer getKonuYorumSayisi() {
        return konuYorumSayisi;
    }

    public void setKonuYorumSayisi(Integer konuYorumSayisi) {
        this.konuYorumSayisi = konuYorumSayisi;
    }

    public String getKonuYazarAd() {
        return konuYazarAd;
    }

    public void setKonuYazarAd(String konuYazarAd) {
        this.konuYazarAd = konuYazarAd;
    }

    public Integer getKonuYazarYetki() {
        return konuYazarYetki;
    }

    public void setKonuYazarYetki(Integer konuYazarYetki) {
        this.konuYazarYetki = konuYazarYetki;
    }
}