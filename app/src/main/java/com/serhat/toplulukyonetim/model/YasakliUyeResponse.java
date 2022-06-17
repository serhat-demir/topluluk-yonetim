package com.serhat.toplulukyonetim.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YasakliUyeResponse {
    @SerializedName("yasakli_uyeler")
    @Expose
    private List<YasakliUye> yasakliUyeler = null;

    public List<YasakliUye> getYasakliUyeler() {
        return yasakliUyeler;
    }

    public void setYasakliUyeler(List<YasakliUye> yasakliUyeler) {
        this.yasakliUyeler = yasakliUyeler;
    }
}