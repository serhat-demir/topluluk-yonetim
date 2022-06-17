package com.serhat.toplulukyonetim.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KonuResponse {
    @SerializedName("konular")
    @Expose
    private List<Konu> konular = null;

    public List<Konu> getKonular() {
        return konular;
    }

    public void setKonular(List<Konu> konular) {
        this.konular = konular;
    }
}