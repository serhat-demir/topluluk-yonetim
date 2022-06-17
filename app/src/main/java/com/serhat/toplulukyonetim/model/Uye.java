package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Uye {
    @SerializedName("kullanici_id")
    @Expose
    private Integer kullaniciId;

    @SerializedName("kullanici_ad")
    @Expose
    private String kullaniciAd;

    @SerializedName("kullanici_sifre")
    @Expose
    private String kullaniciSifre;

    @SerializedName("kullanici_yetki")
    @Expose
    private Integer kullaniciYetki;

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

    public Integer getKullaniciYetki() {
        return kullaniciYetki;
    }

    public void setKullaniciYetki(Integer kullaniciYetki) {
        this.kullaniciYetki = kullaniciYetki;
    }
}