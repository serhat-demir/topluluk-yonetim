package com.serhat.toplulukyonetim.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UyeResponse {
    @SerializedName("uyeler")
    @Expose
    private List<Uye> uyeler = null;

    public List<Uye> getUyeler() {
        return uyeler;
    }

    public void setUyeler(List<Uye> uyeler) {
        this.uyeler = uyeler;
    }
}