package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bildirim {
    @SerializedName("bildirim_id")
    @Expose
    private String bildirimId;

    @SerializedName("bildirim_alici")
    @Expose
    private String bildirimAlici;

    @SerializedName("bildirim_icerik")
    @Expose
    private String bildirimIcerik;

    @SerializedName("bildirim_konu")
    @Expose
    private String bildirimKonu;

    @SerializedName("bildirim_okundu")
    @Expose
    private String bildirimOkundu;

    public String getBildirimId() {
        return bildirimId;
    }

    public void setBildirimId(String bildirimId) {
        this.bildirimId = bildirimId;
    }

    public String getBildirimAlici() {
        return bildirimAlici;
    }

    public void setBildirimAlici(String bildirimAlici) {
        this.bildirimAlici = bildirimAlici;
    }

    public String getBildirimIcerik() {
        return bildirimIcerik;
    }

    public void setBildirimIcerik(String bildirimIcerik) {
        this.bildirimIcerik = bildirimIcerik;
    }

    public String getBildirimKonu() {
        return bildirimKonu;
    }

    public void setBildirimKonu(String bildirimKonu) {
        this.bildirimKonu = bildirimKonu;
    }

    public String getBildirimOkundu() {
        return bildirimOkundu;
    }

    public void setBildirimOkundu(String bildirimOkundu) {
        this.bildirimOkundu = bildirimOkundu;
    }
}