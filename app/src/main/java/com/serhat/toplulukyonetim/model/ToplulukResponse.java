package com.serhat.toplulukyonetim.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToplulukResponse {
    @SerializedName("topluluklar")
    @Expose
    private List<Topluluk> topluluklar;

    public List<Topluluk> getTopluluklar() {
        return topluluklar;
    }

    public void setTopluluklar(List<Topluluk> topluluklar) {
        this.topluluklar = topluluklar;
    }
}
