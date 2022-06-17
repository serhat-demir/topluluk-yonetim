package com.serhat.toplulukyonetim.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.ToplulukActivity;
import com.serhat.toplulukyonetim.YasakliUyelerActivity;
import com.serhat.toplulukyonetim.adapter.UyeAdapter;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.Topluluk;
import com.serhat.toplulukyonetim.model.Uye;
import com.serhat.toplulukyonetim.model.UyeResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToplulukDetayFragment extends Fragment {
    private Context context;
    private ApiInterface service;

    private String toplulukId;
    private Topluluk topluluk;

    private TextView txtToplulukAd, txtToplulukAciklama, txtToplulukUyeler;
    private Button btnToplulukAyarlari, btnToplulukKatil, btnToplulukAyril, btnYasakliUyeler;
    private RecyclerView rvUyeler;

    private List<Uye> uyeler;
    private UyeAdapter uyeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tasarim = inflater.inflate(R.layout.fragment_topluluk_detay, container, false);

        context = getActivity();
        service = ApiClient.getClient().create(ApiInterface.class);

        toplulukId = ((ToplulukActivity) getActivity()).toplulukId;
        toplulukDetayGetir();

        txtToplulukAd = tasarim.findViewById(R.id.frgToplulukDetayTxtToplulukAd);
        txtToplulukAciklama = tasarim.findViewById(R.id.frgToplulukDetayTxtToplulukAciklama);
        txtToplulukUyeler = tasarim.findViewById(R.id.frgToplulukDetayTxtUyeler);

        btnToplulukAyarlari = tasarim.findViewById(R.id.frgToplulukDetayBtnToplulukAyarlari);
        btnToplulukKatil = tasarim.findViewById(R.id.frgToplulukDetayBtnToplulukKatil);
        btnToplulukAyril = tasarim.findViewById(R.id.frgToplulukDetayBtnToplulukAyril);
        btnYasakliUyeler = tasarim.findViewById(R.id.frgToplulukDetayBtnYasakliUyeler);

        rvUyeler = tasarim.findViewById(R.id.frgToplulukDetayRvUyeler);

        rvUyeler.setHasFixedSize(true);
        rvUyeler.setLayoutManager(new LinearLayoutManager(context));

        toplulukUyelerGetir();

        btnToplulukKatil.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                toplulugaKatil(view);
            }
        });

        btnToplulukAyril.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                topluluktanAyril(view);
            }
        });

        btnToplulukAyarlari.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
               toplulukAyarlari(view);
            }
        });

        btnYasakliUyeler.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                yasakliUyeleriGetir();
            }
        });

        return tasarim;
    }

    private void toplulukDetayGetir() {
        service.toplulukDetay(toplulukId).enqueue(new Callback<Topluluk>() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onResponse(Call<Topluluk> call, Response<Topluluk> response) {
                if (response.body() != null && response.body().getToplulukId() != null) {
                    topluluk = response.body();

                    txtToplulukAd.setText(Utils.stringToTitleCase(topluluk.getToplulukAd()));
                    txtToplulukAciklama.setText(topluluk.getToplulukAciklama());
                    txtToplulukUyeler.setText("Üyeler (" + topluluk.getToplulukUyeSayisi() + " Üye)");

                    if (!topluluk.getToplulukGizlilik().equals(Utils.TOPLULUK_ACIK)) {
                        btnToplulukKatil.setEnabled(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Topluluk> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    public void toplulukUyelerGetir() {
        service.toplulukUyeler(toplulukId).enqueue(new Callback<UyeResponse>() {
            @Override
            public void onResponse(Call<UyeResponse> call, Response<UyeResponse> response) {
                if (response.body() != null && response.body().getUyeler() != null) {
                    uyeler = response.body().getUyeler();
                    uyeAdapter = new UyeAdapter(context, uyeler, service, toplulukId, ToplulukDetayFragment.this);

                    rvUyeler.setAdapter(uyeAdapter);

                    toplulukUyelikKontrol();
                    toplulukUyeYasakKontrol();
                }
            }

            @Override
            public void onFailure(Call<UyeResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void toplulukUyelikKontrol() {
        boolean toplulugaUye = false;

        for (Uye uye : uyeler) {
            if (String.valueOf(uye.getKullaniciId()).equals(Utils.AKTIF_KULLANICI_ID)) {
                toplulugaUye = true;
                Utils.AKTIF_KULLANICI_YETKI = uye.getKullaniciYetki();
                break;
            }
        }

        if (toplulugaUye) {
            btnToplulukAyril.setVisibility(View.VISIBLE);
            btnToplulukKatil.setVisibility(View.INVISIBLE);
        } else {
            btnToplulukAyril.setVisibility(View.INVISIBLE);
            btnToplulukKatil.setVisibility(View.VISIBLE);
        }

        if (Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI) {
            btnToplulukAyarlari.setVisibility(View.VISIBLE);
            btnYasakliUyeler.setVisibility(View.VISIBLE);
            btnToplulukAyril.setVisibility(View.INVISIBLE);
            btnToplulukKatil.setVisibility(View.INVISIBLE);
        }

        if (Utils.AKTIF_KULLANICI_YETKI != Utils.YETKI_YONETICI && Utils.AKTIF_KULLANICI_YETKI != Utils.YETKI_MODERATOR) {
            ((ToplulukActivity) getActivity()).actionKayitlar.setVisible(false);
        }
    }

    private void toplulukUyeYasakKontrol() {
        service.toplulukUyeYasakKontrol(toplulukId, Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                String durum = response.body().getDurum();
                String mesaj = response.body().getMesaj();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    btnToplulukKatil.setEnabled(false);

                    AlertDialog.Builder adYasaklandiniz = new AlertDialog.Builder(context);
                    adYasaklandiniz.setTitle(context.getResources().getString(R.string.title_hata));
                    adYasaklandiniz.setMessage(mesaj);
                    adYasaklandiniz.setCancelable(false);
                    adYasaklandiniz.setPositiveButton((context.getResources().getString(R.string.btn_tamam)), (dialogInterface, i) -> {
                        getActivity().onBackPressed();
                    });
                    adYasaklandiniz.create().show();
                }
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void toplulugaKatil(View view) {
        service.toplulukKatil(toplulukId, Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                String durum = response.body().getDurum();
                String mesaj = response.body().getMesaj();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    toplulukDetayGetir();
                    toplulukUyelerGetir();
                    toplulukUyelikKontrol();

                    Toast.makeText(context, mesaj, Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, mesaj, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void topluluktanAyril(View view) {
        service.toplulukAyril(toplulukId, Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                String durum = response.body().getDurum();
                String mesaj = response.body().getMesaj();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    getActivity().onBackPressed();

                    Toast.makeText(context, mesaj, Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, mesaj, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void toplulukAyarlari(View view) {
        View adToplulukAyarlariView = getLayoutInflater().inflate(R.layout.ad_topluluk_ayarlari, null);

        EditText adToplulukAyarlariEdtToplulukAd, adToplulukAyarlariEdtToplulukAciklama;
        RadioButton adToplulukAyarlariRdAcik, adToplulukAyarlariRdKapali, adToplulukAyarlariRdGizli;

        adToplulukAyarlariEdtToplulukAd = adToplulukAyarlariView.findViewById(R.id.adToplulukAyarlariEdtToplulukAd);
        adToplulukAyarlariEdtToplulukAciklama = adToplulukAyarlariView.findViewById(R.id.adToplulukAyarlariEdtToplulukAciklama);
        adToplulukAyarlariRdAcik = adToplulukAyarlariView.findViewById(R.id.adToplulukAyarlariRdAcik);
        adToplulukAyarlariRdKapali = adToplulukAyarlariView.findViewById(R.id.adToplulukAyarlariRdKapali);
        adToplulukAyarlariRdGizli = adToplulukAyarlariView.findViewById(R.id.adToplulukAyarlariRdGizli);

        adToplulukAyarlariEdtToplulukAd.setText(Utils.stringToTitleCase(topluluk.getToplulukAd()));
        adToplulukAyarlariEdtToplulukAciklama.setText(topluluk.getToplulukAciklama());
        if (topluluk.getToplulukGizlilik().equals(Utils.TOPLULUK_ACIK)) adToplulukAyarlariRdAcik.setChecked(true);
        else if (topluluk.getToplulukGizlilik().equals(Utils.TOPLULUK_KAPALI)) adToplulukAyarlariRdKapali.setChecked(true);
        else if (topluluk.getToplulukGizlilik().equals(Utils.TOPLULUK_GIZLI)) adToplulukAyarlariRdGizli.setChecked(true);
        else adToplulukAyarlariRdAcik.setChecked(true);

        AlertDialog.Builder adToplulukAyarlari = new AlertDialog.Builder(context);
        adToplulukAyarlari.setTitle(context.getResources().getString(R.string.adToplulukAyarlariTitle));
        adToplulukAyarlari.setView(adToplulukAyarlariView);
        adToplulukAyarlari.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> {});
        adToplulukAyarlari.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
            String toplulukAd, toplulukAciklama, toplulukGizlilik;
            toplulukAd = adToplulukAyarlariEdtToplulukAd.getText().toString().trim();
            toplulukAciklama = adToplulukAyarlariEdtToplulukAciklama.getText().toString().trim();

            if (adToplulukAyarlariRdAcik.isChecked()) toplulukGizlilik = Utils.TOPLULUK_ACIK;
            else if (adToplulukAyarlariRdKapali.isChecked()) toplulukGizlilik = Utils.TOPLULUK_KAPALI;
            else if (adToplulukAyarlariRdGizli.isChecked()) toplulukGizlilik = Utils.TOPLULUK_GIZLI;
            else toplulukGizlilik = Utils.TOPLULUK_ACIK;

            if (toplulukAd.isEmpty() || toplulukAciklama.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.adToplulukOlusturEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            service.toplulukDuzenle(toplulukId, toplulukAd, toplulukAciklama, toplulukGizlilik).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();
                    String mesaj = response.body().getMesaj();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        ((ToplulukActivity) getActivity()).toolbar.setTitle(Utils.stringToTitleCase(toplulukAd));
                        toplulukDetayGetir();

                        Utils.toplulukKayitEkle(context, toplulukId, context.getResources().getString(R.string.kayit_topluluk_bilgileri_guncellendi));

                        Toast.makeText(context, mesaj, Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(view, mesaj, Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CrudResponse> call, Throwable t) {
                    Log.e(getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        });
        adToplulukAyarlari.setNeutralButton((context.getResources().getString(R.string.btn_topluluk_sil)), (dialogInterface, i) -> {
            AlertDialog.Builder adToplulukSil = new AlertDialog.Builder(context);
            adToplulukSil.setTitle(context.getResources().getString(R.string.title_uyari));
            adToplulukSil.setMessage(context.getResources().getString(R.string.adToplulukSilUyari));
            adToplulukSil.setPositiveButton((context.getResources().getString(R.string.btn_evet)), (dialogInterface1, i1) -> {
                service.toplulukSil(toplulukId).enqueue(new Callback<CrudResponse>() {
                    @Override
                    public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                        String durum = response.body().getDurum();
                        String mesaj = response.body().getMesaj();

                        if(durum.equals(Utils.DURUM_TAMAM)) {
                            getActivity().onBackPressed();
                            Toast.makeText(context, mesaj, Toast.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(view, mesaj, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CrudResponse> call, Throwable t) {
                        Log.e(getResources().getString(R.string.title_hata), t.getMessage());
                    }
                });
            });
            adToplulukSil.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface2, i2) -> { });
            adToplulukSil.create().show();
        });

        AlertDialog ad = adToplulukAyarlari.create();
        ad.show();

        ad.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#F31717"));
    }

    private void yasakliUyeleriGetir() {
        Intent intent = new Intent(context, YasakliUyelerActivity.class);
        intent.putExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID, toplulukId);
        intent.putExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_AD, topluluk.getToplulukAd());
        startActivity(intent);
        getActivity().finish();
    }
}