package com.serhat.toplulukyonetim.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YorumResponse {
    @SerializedName("yorumlar")
    @Expose
    private List<Yorum> yorumlar = null;

    public List<Yorum> getYorumlar() {
        return yorumlar;
    }

    public void setYorumlar(List<Yorum> yorumlar) {
        this.yorumlar = yorumlar;
    }
}