package com.serhat.toplulukyonetim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.YasakliUyelerActivity;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.YasakliUye;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YasakliUyeAdapter extends RecyclerView.Adapter<YasakliUyeAdapter.YasakliUyeHolder> {
    private Context context;
    private List<YasakliUye> yasakliUyeler;
    private ApiInterface service;
    private String toplulukId;

    public YasakliUyeAdapter(Context context, List<YasakliUye> yasakliUyeler, ApiInterface service, String toplulukId) {
        this.context = context;
        this.yasakliUyeler = yasakliUyeler;
        this.service = service;
        this.toplulukId = toplulukId;
    }

    public class YasakliUyeHolder extends RecyclerView.ViewHolder {
        public ImageView cardYasakliUyeBtnYasakKaldir;
        public TextView cardYasakliUyeTxtUyeAd, cardYasakliUyeTxtYasakBilgi;

        public YasakliUyeHolder(@NonNull View itemView) {
            super(itemView);

            cardYasakliUyeBtnYasakKaldir = itemView.findViewById(R.id.cardYasakliUyeBtnYasakKaldir);
            cardYasakliUyeTxtUyeAd = itemView.findViewById(R.id.cardYasakliUyeTxtUyeAd);
            cardYasakliUyeTxtYasakBilgi = itemView.findViewById(R.id.cardYasakliUyeTxtYasakBilgi);
        }
    }

    @NonNull
    @Override
    public YasakliUyeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YasakliUyeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_yasakli_uye, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull YasakliUyeHolder holder, int position) {
        YasakliUye yasakliUye = yasakliUyeler.get(position);

        holder.cardYasakliUyeTxtUyeAd.setText(Utils.stringToTitleCase(yasakliUye.getKullaniciAd()));
        holder.cardYasakliUyeTxtYasakBilgi.setText("Yasaklanma tarihi: " + yasakliUye.getYasaklanmaTarihi().substring(0, 10) + " \nYasaklanma sebebi: " + yasakliUye.getYasaklanmaSebebi());

        holder.cardYasakliUyeBtnYasakKaldir.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                service.uyeYasakKaldir(toplulukId, String.valueOf(yasakliUye.getKullaniciId())).enqueue(new Callback<CrudResponse>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                        String durum = response.body().getDurum();
                        String mesaj = response.body().getMesaj();

                        if(durum.equals(Utils.DURUM_TAMAM)) {
                            Toast.makeText(context, mesaj, Toast.LENGTH_SHORT).show();

                            yasakliUyeler.remove(yasakliUye);
                            notifyDataSetChanged();
                            
                            Utils.toplulukKayitEkle(context, toplulukId, Utils.stringToTitleCase(yasakliUye.getKullaniciAd()) + " " + context.getResources().getString(R.string.kayit_yasak_kaldirildi));

                            if (yasakliUyeler.size() == 0) {
                                ((YasakliUyelerActivity) context).txtUyeYok.setVisibility(View.VISIBLE);
                                ((YasakliUyelerActivity) context).rvUyeler.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Snackbar.make(view, mesaj, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CrudResponse> call, Throwable t) {
                        Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return yasakliUyeler.size();
    }
}
