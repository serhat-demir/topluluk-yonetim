package com.serhat.toplulukyonetim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.KonuDetayActivity;
import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.Yorum;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.YorumHolder> {
    private Context context;
    private List<Yorum> yorumlar;
    private ApiInterface service;
    private int konuYazarId;
    private String toplulukId, toplulukAd, konuBaslik;

    public YorumAdapter(Context context, List<Yorum> yorumlar, ApiInterface service, int konuYazarId, String toplulukId, String toplulukAd, String konuBaslik) {
        this.context = context;
        this.yorumlar = yorumlar;
        this.service = service;
        this.konuYazarId = konuYazarId;
        this.toplulukId = toplulukId;
        this.toplulukAd = toplulukAd;
        this.konuBaslik = konuBaslik;
    }

    public class YorumHolder extends RecyclerView.ViewHolder {
        public TextView cardYorumTxtYazar, cardYorumTxtYazarYetki, cardYorumTxtIcerik, cardYorumTxtTarih, cardYorumBtnDuzenle, cardYorumBtnSil;

        public YorumHolder(@NonNull View itemView) {
            super(itemView);

            cardYorumTxtYazar = itemView.findViewById(R.id.cardYorumTxtYazar);
            cardYorumTxtYazarYetki = itemView.findViewById(R.id.cardYorumTxtYazarYetki);
            cardYorumTxtIcerik = itemView.findViewById(R.id.cardYorumTxtIcerik);
            cardYorumTxtTarih = itemView.findViewById(R.id.cardYorumTxtTarih);
            cardYorumBtnDuzenle = itemView.findViewById(R.id.cardYorumBtnDuzenle);
            cardYorumBtnSil = itemView.findViewById(R.id.cardYorumBtnSil);
        }
    }

    @NonNull
    @Override
    public YorumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YorumHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_yorum, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull YorumHolder holder, int position) {
        Yorum yorum = yorumlar.get(position);

        String yazarYetki = (yorum.getYorumYazarYetki() == Utils.YETKI_YONETICI) ? (context.getResources().getString(R.string.yetki_yonetici)) : (yorum.getYorumYazarYetki() == Utils.YETKI_MODERATOR) ? (context.getResources().getString(R.string.yetki_moderator)) : (yorum.getYorumYazarYetki() == Utils.YETKI_KULLANICI) ? (context.getResources().getString(R.string.yetki_uye)) : (context.getResources().getString(R.string.yetki_yok));
        if (konuYazarId == yorum.getYorumYazar()) {
            yazarYetki += ", " + context.getResources().getString(R.string.konu_sahibi);
        }

        holder.cardYorumTxtYazar.setText(Utils.stringToTitleCase(yorum.getYorumYazarAd()));
        holder.cardYorumTxtYazarYetki.setText(yazarYetki);
        holder.cardYorumTxtIcerik.setText(yorum.getYorumIcerik());
        holder.cardYorumTxtTarih.setText(yorum.getYorumTarih().substring(0, 16));

        holder.cardYorumBtnDuzenle.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                yorumDuzenle(view, yorum, position);
            }
        });

        holder.cardYorumBtnSil.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                yorumSil(view, yorum);
            }
        });

        if (Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(yorum.getYorumYazar())) || Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI || Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_MODERATOR) {
            holder.cardYorumBtnDuzenle.setVisibility(View.VISIBLE);
            holder.cardYorumBtnSil.setVisibility(View.VISIBLE);
        }

        if (Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YOK) {
            holder.cardYorumBtnDuzenle.setVisibility(View.INVISIBLE);
            holder.cardYorumBtnSil.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return yorumlar.size();
    }

    private void yorumDuzenle(View view, Yorum yorum, int position) {
        View adYorumDuzenleView = ((KonuDetayActivity) context).getLayoutInflater().inflate(R.layout.ad_yorum_at, null);

        EditText adYorumDuzenleEdtYorumIcerik;
        adYorumDuzenleEdtYorumIcerik = adYorumDuzenleView.findViewById(R.id.adYorumAtEdtYorumIcerik);
        adYorumDuzenleEdtYorumIcerik.setText(yorum.getYorumIcerik());

        AlertDialog.Builder adYorumDuzenle = new AlertDialog.Builder(context);
        adYorumDuzenle.setTitle(context.getResources().getString(R.string.adYorumDuzenleTitle));
        adYorumDuzenle.setView(adYorumDuzenleView);
        adYorumDuzenle.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> {});
        adYorumDuzenle.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
            String yorumIcerik;
            yorumIcerik = adYorumDuzenleEdtYorumIcerik.getText().toString().trim();

            if (yorumIcerik.isEmpty()) {
                Snackbar.make(view, context.getResources().getString(R.string.adYorumAtEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            service.yorumDuzenle(String.valueOf(yorum.getYorumId()), yorumIcerik).enqueue(new Callback<CrudResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        if (!Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(yorum.getYorumYazar()))) {
                            Utils.bildirimGonder(context, String.valueOf(yorum.getYorumYazar()), "\"" + Utils.stringToTitleCase(toplulukAd) + "\" topluluğunda, \"" + Utils.stringToTitleCase(konuBaslik) + "\" " + context.getResources().getString(R.string.bildirim_yorumunuz_duzenlendi));
                        }

                        if ((Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI || Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_MODERATOR) && !Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(yorum.getYorumYazar()))) {
                            Utils.toplulukKayitEkle(context, toplulukId, "\"" + Utils.stringToTitleCase(konuBaslik) + context.getResources().getString(R.string.kayit_yorum_duzenlendi_1) + Utils.stringToTitleCase(yorum.getYorumYazarAd()) + context.getResources().getString(R.string.kayit_yorum_duzenlendi_2) + Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + context.getResources().getString(R.string.kayit_yorum_duzenlendi_3));
                        }

                        Toast.makeText(context, context.getResources().getString(R.string.konu_sabitlendi), Toast.LENGTH_SHORT).show();

                        yorumlar.get(position).setYorumIcerik(yorumIcerik);
                        notifyDataSetChanged();
                    } else {
                        Snackbar.make(view, context.getResources().getString(R.string.genel_hata), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CrudResponse> call, Throwable t) {
                    Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        });
        adYorumDuzenle.create().show();
    }

    private void yorumSil(View view, Yorum yorum) {
        AlertDialog.Builder adYorumSil = new AlertDialog.Builder(context);
        adYorumSil.setTitle(context.getResources().getString(R.string.adYorumSilTitle));
        adYorumSil.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> {});
        adYorumSil.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
            service.yorumSil(String.valueOf(yorum.getYorumId())).enqueue(new Callback<CrudResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        if (!Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(yorum.getYorumYazar()))) {
                            Utils.bildirimGonder(context, String.valueOf(yorum.getYorumYazar()), "\"" + Utils.stringToTitleCase(toplulukAd) + "\" topluluğunda, \"" + Utils.stringToTitleCase(konuBaslik) + "\" " + context.getResources().getString(R.string.bildirim_yorumunuz_silindi) + "(" + yorum.getYorumIcerik() + ")");
                        }

                        if ((Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI || Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_MODERATOR) && !Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(yorum.getYorumYazar()))) {
                            Utils.toplulukKayitEkle(context, toplulukId, "\"" + Utils.stringToTitleCase(konuBaslik) + context.getResources().getString(R.string.kayit_yorum_silindi_1) + Utils.stringToTitleCase(yorum.getYorumYazarAd()) + context.getResources().getString(R.string.kayit_yorum_silindi_2) + Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + context.getResources().getString(R.string.kayit_yorum_silindi_3));
                        }

                        Toast.makeText(context, context.getResources().getString(R.string.konu_sabitlendi), Toast.LENGTH_SHORT).show();

                        yorumlar.remove(yorum);
                        notifyDataSetChanged();
                    } else {
                        Snackbar.make(view, context.getResources().getString(R.string.genel_hata), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CrudResponse> call, Throwable t) {
                    Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        });
        adYorumSil.create().show();
    }
}
