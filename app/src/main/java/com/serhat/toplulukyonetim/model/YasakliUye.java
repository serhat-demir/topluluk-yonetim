package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YasakliUye {
    @SerializedName("kullanici_id")
    @Expose
    private Integer kullaniciId;

    @SerializedName("kullanici_ad")
    @Expose
    private String kullaniciAd;

    @SerializedName("kullanici_sifre")
    @Expose
    private String kullaniciSifre;

    @SerializedName("yasaklanma_sebebi")
    @Expose
    private String yasaklanmaSebebi;

    @SerializedName("yasaklanma_tarihi")
    @Expose
    private String yasaklanmaTarihi;

    public Integer getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(Integer kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciAd() {
        return kullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        this.kullaniciAd = kullaniciAd;
    }

    public String getKullaniciSifre() {
        return kullaniciSifre;
    }

    public void setKullaniciSifre(String kullaniciSifre) {
        this.kullaniciSifre = kullaniciSifre;
    }

    public String getYasaklanmaSebebi() {
        return yasaklanmaSebebi;
    }

    public void setYasaklanmaSebebi(String yasaklanmaSebebi) {
        this.yasaklanmaSebebi = yasaklanmaSebebi;
    }

    public String getYasaklanmaTarihi() {
        return yasaklanmaTarihi;
    }

    public void setYasaklanmaTarihi(String yasaklanmaTarihi) {
        this.yasaklanmaTarihi = yasaklanmaTarihi;
    }
}