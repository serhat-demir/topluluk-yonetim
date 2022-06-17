package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BildirimResponse {
    @SerializedName("bildirimler")
    @Expose
    private List<Bildirim> bildirimler;

    public List<Bildirim> getBildirimler() {
        return bildirimler;
    }

    public void setBildirimler(List<Bildirim> bildirimler) {
        this.bildirimler = bildirimler;
    }
}
