package com.serhat.toplulukyonetim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.ToplulukActivity;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.fragment.ToplulukDetayFragment;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.Topluluk;
import com.serhat.toplulukyonetim.model.Uye;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UyeAdapter extends RecyclerView.Adapter<UyeAdapter.UyeHolder> {
    private Context context;
    private List<Uye> uyeler;
    private ApiInterface service;
    private String toplulukId, toplulukAd;
    private ToplulukDetayFragment frg;
    private String yasaklanmaSebebi;

    public UyeAdapter(Context context, List<Uye> uyeler, ApiInterface service, String toplulukId, ToplulukDetayFragment frg) {
        this.context = context;
        this.uyeler = uyeler;
        this.service = service;
        this.toplulukId = toplulukId;
        this.frg = frg;

        service.toplulukDetay(toplulukId).enqueue(new Callback<Topluluk>() {
            @Override
            public void onResponse(Call<Topluluk> call, Response<Topluluk> response) {
                toplulukAd = response.body().getToplulukAd();
            }

            @Override
            public void onFailure(Call<Topluluk> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    public class UyeHolder extends RecyclerView.ViewHolder {
        public ImageView cardUyeBtnUyeAyarlari;
        public TextView cardUyeTxtUyeAd, cardUyeTxtUyeYetki;

        public UyeHolder(@NonNull View itemView) {
            super(itemView);

            cardUyeBtnUyeAyarlari = itemView.findViewById(R.id.cardUyeBtnUyeAyarlari);
            cardUyeTxtUyeAd = itemView.findViewById(R.id.cardUyeTxtUyeAd);
            cardUyeTxtUyeYetki = itemView.findViewById(R.id.cardUyeTxtUyeYetki);
        }
    }

    @NonNull
    @Override
    public UyeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UyeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_uye, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UyeHolder holder, int position) {
        Uye uye = uyeler.get(position);

        holder.cardUyeTxtUyeAd.setText(Utils.stringToTitleCase(uye.getKullaniciAd()));

        if (uye.getKullaniciYetki().equals(Utils.YETKI_YONETICI)) {
            holder.cardUyeTxtUyeYetki.setText(context.getResources().getString(R.string.yetki_yonetici));
        } else if (uye.getKullaniciYetki().equals(Utils.YETKI_MODERATOR)) {
            holder.cardUyeTxtUyeYetki.setText(context.getResources().getString(R.string.yetki_moderator));
        } else {
            holder.cardUyeTxtUyeYetki.setText(context.getResources().getString(R.string.yetki_uye));
        }

        holder.cardUyeBtnUyeAyarlari.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                View adUyeDuzenleView = ((ToplulukActivity) context).getLayoutInflater().inflate(R.layout.ad_uye_duzenle, null);

                EditText adUyeDuzenleEdtYasakSebebi;
                RadioButton adUyeDuzenleRdUye, adUyeDuzenleRdModerator;

                adUyeDuzenleEdtYasakSebebi = adUyeDuzenleView.findViewById(R.id.adUyeDuzenleEdtYasakSebebi);
                adUyeDuzenleRdUye = adUyeDuzenleView.findViewById(R.id.adUyeDuzenleRdUye);
                adUyeDuzenleRdModerator = adUyeDuzenleView.findViewById(R.id.adUyeDuzenleRdModerator);

                if (uye.getKullaniciYetki() == Utils.YETKI_MODERATOR) {
                    adUyeDuzenleRdModerator.setChecked(true);
                    adUyeDuzenleRdUye.setChecked(false);
                }

                AlertDialog.Builder adUyeDuzenle = new AlertDialog.Builder(context);
                adUyeDuzenle.setTitle(context.getResources().getString(R.string.adUyeDuzenleTitle) + " " + Utils.stringToTitleCase(uye.getKullaniciAd()));
                adUyeDuzenle.setView(adUyeDuzenleView);
                adUyeDuzenle.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> { });
                adUyeDuzenle.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
                    String yeniYetki;

                    if (adUyeDuzenleRdUye.isChecked()) {
                        yeniYetki = String.valueOf(Utils.YETKI_KULLANICI);
                    } else {
                        yeniYetki = String.valueOf(Utils.YETKI_MODERATOR);
                    }

                    service.uyeDuzenle(toplulukId, String.valueOf(uye.getKullaniciId()), yeniYetki).enqueue(new Callback<CrudResponse>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                            String durum = response.body().getDurum();
                            String mesaj = response.body().getMesaj();

                            if(durum.equals(Utils.DURUM_TAMAM)) {
                                Toast.makeText(context, mesaj, Toast.LENGTH_SHORT).show();
                                frg.toplulukUyelerGetir();
                                
                                Utils.toplulukKayitEkle(context, toplulukId, Utils.stringToTitleCase(uye.getKullaniciAd()) + " " + context.getResources().getString(R.string.kayit_yetki_guncellendi_1) + (adUyeDuzenleRdUye.isChecked() ? context.getResources().getString(R.string.yetki_uye) : context.getResources().getString(R.string.yetki_moderator)) + context.getResources().getString(R.string.kayit_yetki_guncellendi_2));
                            } else {
                                Snackbar.make(view, mesaj, Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CrudResponse> call, Throwable t) {
                            Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
                        }
                    });
                });
                adUyeDuzenle.setNeutralButton((context.getResources().getString(R.string.btn_yasakla)), (dialogInterface, i) -> {
                    yasaklanmaSebebi = adUyeDuzenleEdtYasakSebebi.getText().toString().trim();

                    if (yasaklanmaSebebi.isEmpty()) {
                        yasaklanmaSebebi = Utils.YASAKLANMA_SEBEBI_YOK;
                    }

                    service.uyeYasakla(toplulukId, String.valueOf(uye.getKullaniciId()), yasaklanmaSebebi).enqueue(new Callback<CrudResponse>() {
                        @Override
                        public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                            String durum = response.body().getDurum();
                            String mesaj = response.body().getMesaj();

                            if(durum.equals(Utils.DURUM_TAMAM)) {
                                Toast.makeText(context, mesaj, Toast.LENGTH_SHORT).show();
                                frg.toplulukUyelerGetir();

                                Utils.toplulukKayitEkle(context, toplulukId, "\"" + Utils.stringToTitleCase(uye.getKullaniciAd()) + "\" " + context.getResources().getString(R.string.kayit_uye_yasaklandi) + " " + yasaklanmaSebebi);
                                Utils.bildirimGonder(context, String.valueOf(uye.getKullaniciId()), "\"" + Utils.stringToTitleCase(toplulukAd) + "\" " + context.getResources().getString(R.string.bildirim_yasaklandiniz) + " " + yasaklanmaSebebi);
                            } else {
                                Snackbar.make(view, mesaj, Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CrudResponse> call, Throwable t) {
                            Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
                        }
                    });
                });

                AlertDialog ad = adUyeDuzenle.create();
                ad.show();

                ad.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor(context.getResources().getString(R.string.renk_btn_yasak)));
            }
        });

        if (position != 0 && Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI) {
            holder.cardUyeBtnUyeAyarlari.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return uyeler.size();
    }
}
