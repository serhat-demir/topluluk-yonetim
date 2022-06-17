package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Topluluk {
    @SerializedName("topluluk_id")
    @Expose
    private String toplulukId;

    @SerializedName("topluluk_ad")
    @Expose
    private String toplulukAd;

    @SerializedName("topluluk_aciklama")
    @Expose
    private String toplulukAciklama;

    @SerializedName("topluluk_gizlilik")
    @Expose
    private String toplulukGizlilik;

    @SerializedName("topluluk_uye_sayisi")
    @Expose
    private String toplulukUyeSayisi;

    public String getToplulukId() {
        return toplulukId;
    }

    public void setToplulukId(String toplulukId) {
        this.toplulukId = toplulukId;
    }

    public String getToplulukAd() {
        return toplulukAd;
    }

    public void setToplulukAd(String toplulukAd) {
        this.toplulukAd = toplulukAd;
    }

    public String getToplulukAciklama() {
        return toplulukAciklama;
    }

    public void setToplulukAciklama(String toplulukAciklama) {
        this.toplulukAciklama = toplulukAciklama;
    }

    public String getToplulukGizlilik() {
        return toplulukGizlilik;
    }

    public void setToplulukGizlilik(String toplulukGizlilik) {
        this.toplulukGizlilik = toplulukGizlilik;
    }

    public String getToplulukUyeSayisi() {
        return toplulukUyeSayisi;
    }

    public void setToplulukUyeSayisi(String toplulukUyeSayisi) {
        this.toplulukUyeSayisi = toplulukUyeSayisi;
    }
}