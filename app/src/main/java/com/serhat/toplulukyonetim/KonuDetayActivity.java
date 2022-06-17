package com.serhat.toplulukyonetim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.adapter.YorumAdapter;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.BildirimResponse;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.Konu;
import com.serhat.toplulukyonetim.model.Topluluk;
import com.serhat.toplulukyonetim.model.Yorum;
import com.serhat.toplulukyonetim.model.YorumResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KonuDetayActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;

    private Toolbar toolbar;
    private RecyclerView rvYorumlar;
    private TextView txtYazar, txtYazarYetki, txtIcerik, txtTarih, btnSabitle, btnDuzenle, btnSil, txtYorumYok;
    private FloatingActionButton fabYorumAt;

    private String konuId, toplulukId, toplulukAd;
    private Konu konu;

    private MenuItem actionBildirimler;

    private List<Yorum> yorumlar;
    private YorumAdapter yorumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konu_detay);

        context = KonuDetayActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        konuId = getIntent().getStringExtra(Utils.INTENT_STRING_EXTRA_KONU_ID);

        toolbar = findViewById(R.id.actKonuDetayToolbar);
        rvYorumlar = findViewById(R.id.actKonuDetayRvYorumlar);
        txtYazar = findViewById(R.id.cardKonuDetayTxtYazar);
        txtYazarYetki = findViewById(R.id.cardKonuDetayTxtYazarYetki);
        txtIcerik = findViewById(R.id.cardKonuDetayTxtIcerik);
        txtTarih = findViewById(R.id.cardKonuDetayTxtTarih);
        btnSabitle = findViewById(R.id.cardKonuDetayBtnSabitle);
        btnDuzenle = findViewById(R.id.cardKonuDetayBtnDuzenle);
        btnSil = findViewById(R.id.cardKonuDetayBtnSil);
        txtYorumYok = findViewById(R.id.actKonuDetayTxtYorumYok);
        fabYorumAt = findViewById(R.id.actKonuDetayFabYorumAt);

        rvYorumlar.setHasFixedSize(true);
        rvYorumlar.setLayoutManager(new LinearLayoutManager(context));

        fabYorumAt.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                yorumAt(view);
            }
        });

        konuDetayGetir();
    }

    private void yorumAt(View view) {
        View adYorumAtView = getLayoutInflater().inflate(R.layout.ad_yorum_at, null);

        EditText adYorumAtEdtYorumIcerik;

        adYorumAtEdtYorumIcerik = adYorumAtView.findViewById(R.id.adYorumAtEdtYorumIcerik);

        AlertDialog.Builder adYorumAt = new AlertDialog.Builder(context);
        adYorumAt.setTitle(context.getResources().getString(R.string.adYorumAtTitle));
        adYorumAt.setView(adYorumAtView);
        adYorumAt.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> {});
        adYorumAt.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
            String yorumIcerik;
            yorumIcerik = adYorumAtEdtYorumIcerik.getText().toString().trim();

            if (yorumIcerik.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.adYorumAtEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            service.yorumAt(yorumIcerik, Utils.AKTIF_KULLANICI_ID, konuId).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        if (!Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                            Utils.bildirimGonder(context, String.valueOf(konu.getKonuYazar()), "\"" + Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + "\" adlı kullanıcı, \"" + Utils.stringToTitleCase(toplulukAd) + "\" topluluğunda \"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + "\" " + context.getResources().getString(R.string.bildirim_konunuza_yorum_yapti));
                        }

                        Toast.makeText(context, context.getResources().getString(R.string.yorum_gonderildi), Toast.LENGTH_SHORT).show();

                        yorumlariGetir();
                    } else {
                        Snackbar.make(view, context.getResources().getString(R.string.genel_hata), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CrudResponse> call, Throwable t) {
                    Log.e(getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        });
        adYorumAt.create().show();
    }

    private void yorumlariGetir() {
        service.yorumlariGetir(konuId).enqueue(new Callback<YorumResponse>() {
            @Override
            public void onResponse(Call<YorumResponse> call, Response<YorumResponse> response) {
                if (response.body() != null && response.body().getYorumlar() != null) {
                    if (response.body().getYorumlar().size() == 0) {
                        rvYorumlar.setVisibility(View.INVISIBLE);
                        txtYorumYok.setVisibility(View.VISIBLE);
                    } else {
                        rvYorumlar.setVisibility(View.VISIBLE);
                        txtYorumYok.setVisibility(View.INVISIBLE);

                        yorumlar = response.body().getYorumlar();
                        yorumAdapter = new YorumAdapter(context, yorumlar, service, konu.getKonuYazar(), toplulukId, toplulukAd, konu.getKonuBaslik());
                        rvYorumlar.setAdapter(yorumAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<YorumResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void konuDetayGetir() {
        service.konuDetayGetir(konuId).enqueue(new Callback<Konu>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(Call<Konu> call, Response<Konu> response) {
                if (response.body() != null && response.body().getKonuId() != null) {
                    konu = response.body();
                    toplulukId = String.valueOf(konu.getKonuTopluluk());

                    toolbar.setTitle(Utils.stringToTitleCase(konu.getKonuBaslik()));
                    setSupportActionBar(toolbar);

                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
                    toolbar.setNavigationOnClickListener(v -> {
                        onBackPressed();
                    });

                    String yazarYetki = (konu.getKonuYazarYetki() == Utils.YETKI_YONETICI) ? (context.getResources().getString(R.string.yetki_yonetici)) : (konu.getKonuYazarYetki() == Utils.YETKI_MODERATOR) ? (context.getResources().getString(R.string.yetki_moderator)) : (konu.getKonuYazarYetki() == Utils.YETKI_KULLANICI) ? (context.getResources().getString(R.string.yetki_uye)) : (context.getResources().getString(R.string.yetki_yok));
                    txtYazarYetki.setText(yazarYetki + ", " + context.getResources().getString(R.string.konu_sahibi));

                    txtYazar.setText(Utils.stringToTitleCase(konu.getKonuYazarAd()));
                    txtIcerik.setText(konu.getKonuIcerik());
                    txtTarih.setText(konu.getKonuTarih().substring(0, 16));

                    if (konu.getKonuSabit() == Utils.KONU_SABIT) {
                        btnSabitle.setText(context.getResources().getString(R.string.btn_sabitten_kaldir));
                    } else {
                        btnSabitle.setText(context.getResources().getString(R.string.btn_sabitle));
                    }

                    btnSabitle.setOnClickListener(view -> {
                        if (Utils.InternetKontrol(context)) {
                            konuSabitle(view);
                        }
                    });

                    btnDuzenle.setOnClickListener(view -> {
                        if (Utils.InternetKontrol(context)) {
                            konuDuzenle(view);
                        }
                    });

                    btnSil.setOnClickListener(view -> {
                        if (Utils.InternetKontrol(context)) {
                            konuSil(view);
                        }
                    });

                    if (Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI || Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_MODERATOR) {
                        btnSabitle.setVisibility(View.VISIBLE);
                        btnDuzenle.setVisibility(View.VISIBLE);
                        btnSil.setVisibility(View.VISIBLE);
                    }

                    if (Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                        btnDuzenle.setVisibility(View.VISIBLE);
                        btnSil.setVisibility(View.VISIBLE);
                    }

                    if (Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YOK) {
                        fabYorumAt.setVisibility(View.GONE);
                        btnDuzenle.setVisibility(View.INVISIBLE);
                        btnSil.setVisibility(View.INVISIBLE);
                    }

                    toplulukAdiGetir();
                    yorumlariGetir();
                }
            }

            @Override
            public void onFailure(Call<Konu> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void toplulukAdiGetir() {
        service.toplulukDetay(toplulukId).enqueue(new Callback<Topluluk>() {
            @Override
            public void onResponse(Call<Topluluk> call, Response<Topluluk> response) {
                toplulukAd = response.body().getToplulukAd();
            }

            @Override
            public void onFailure(Call<Topluluk> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void konuSabitle(View view) {
        String sabit;

        if (konu.getKonuSabit() == Utils.KONU_SABIT) {
            sabit = "0";
        } else {
            sabit = "1";
        }

        service.konuDuzenle(konuId, konu.getKonuBaslik(), konu.getKonuIcerik(), sabit).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                String durum = response.body().getDurum();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    if (sabit.equals(String.valueOf(Utils.KONU_SABIT))) {
                        if (!Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                            Utils.bildirimGonder(context, String.valueOf(konu.getKonuYazar()), "\"" + Utils.stringToTitleCase(toplulukAd) + "\" topluluğunda, \"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + "\" " + context.getResources().getString(R.string.bildirim_konunuz_sabitlendi));
                        }

                        Utils.toplulukKayitEkle(context, toplulukId, "\"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + context.getResources().getString(R.string.kayit_konu_sabitlendi_1) + Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + context.getResources().getString(R.string.kayit_konu_sabitlendi_2));

                        Toast.makeText(context, context.getResources().getString(R.string.konu_sabitlendi), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                            Utils.bildirimGonder(context, String.valueOf(konu.getKonuYazar()), "\"" + Utils.stringToTitleCase(toplulukAd) + "\" topluluğunda, \"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + "\" " + context.getResources().getString(R.string.bildirim_konunuz_sabitten_kaldirildi));
                        }

                        Utils.toplulukKayitEkle(context, toplulukId, "\"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + context.getResources().getString(R.string.kayit_konu_sabitten_kaldirildi_1) + Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + context.getResources().getString(R.string.kayit_konu_sabitten_kaldirildi_2));

                        Toast.makeText(context, context.getResources().getString(R.string.konu_sabitten_kaldirildi), Toast.LENGTH_SHORT).show();
                    }

                    konuDetayGetir();
                } else {
                    Snackbar.make(view, context.getResources().getString(R.string.genel_hata), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void konuDuzenle(View view) {
        View adKonuDuzenleView = getLayoutInflater().inflate(R.layout.ad_konu_olustur, null);

        EditText adKonuDuzenleEdtKonuBaslik, adKonuDuzenleEdtKonuIcerik;

        adKonuDuzenleEdtKonuBaslik = adKonuDuzenleView.findViewById(R.id.adKonuOlusturEdtKonuBaslik);
        adKonuDuzenleEdtKonuIcerik = adKonuDuzenleView.findViewById(R.id.adKonuOlusturEdtKonuIcerik);

        adKonuDuzenleEdtKonuBaslik.setText(konu.getKonuBaslik());
        adKonuDuzenleEdtKonuIcerik.setText(konu.getKonuIcerik());

        AlertDialog.Builder adKonuDuzenle = new AlertDialog.Builder(context);
        adKonuDuzenle.setTitle(context.getResources().getString(R.string.adKonuDuzenleTitle));
        adKonuDuzenle.setView(adKonuDuzenleView);
        adKonuDuzenle.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> {});
        adKonuDuzenle.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
            String konuBaslik, konuIcerik;
            konuBaslik = adKonuDuzenleEdtKonuBaslik.getText().toString().trim();
            konuIcerik = adKonuDuzenleEdtKonuIcerik.getText().toString().trim();

            if (konuBaslik.isEmpty() || konuIcerik.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.adKonuOlusturEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            service.konuDuzenle(konuId, konuBaslik, konuIcerik, String.valueOf(konu.getKonuSabit())).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        konuDetayGetir();

                        if (!Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                            Utils.bildirimGonder(context, String.valueOf(konu.getKonuYazar()), "\"" + Utils.stringToTitleCase(toplulukAd) + "\" topluluğunda, \"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + "\" " + context.getResources().getString(R.string.bildirim_konunuz_duzenlendi));
                        }

                        if ((Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI || Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_MODERATOR) && !Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                            Utils.toplulukKayitEkle(context, toplulukId, "\"" + Utils.stringToTitleCase(konuBaslik) + context.getResources().getString(R.string.kayit_konu_duzenlendi_1) + Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + context.getResources().getString(R.string.kayit_konu_duzenlendi_2));
                        }

                        Toast.makeText(context, response.body().getMesaj(), Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(view, response.body().getMesaj(), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CrudResponse> call, Throwable t) {
                    Log.e(getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        });
        adKonuDuzenle.create().show();
    }

    private void konuSil(View view) {
        AlertDialog.Builder adKonuSil = new AlertDialog.Builder(context);
        adKonuSil.setTitle(context.getResources().getString(R.string.adKonuSilTitle));
        adKonuSil.setMessage(context.getResources().getString(R.string.adKonuSilMessage));
        adKonuSil.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> { });
        adKonuSil.setPositiveButton((context.getResources().getString(R.string.btn_evet)), (dialogInterface, i) -> {
            service.konuSil(konuId).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        onBackPressed();

                        if (!Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                            Utils.bildirimGonder(context, String.valueOf(konu.getKonuYazar()), "\"" + Utils.stringToTitleCase(toplulukAd) + "\" topluluğunda, \"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + "\" " + context.getResources().getString(R.string.bildirim_konunuz_silindi));
                        }

                        if ((Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YONETICI || Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_MODERATOR) && !Utils.AKTIF_KULLANICI_ID.equals(String.valueOf(konu.getKonuYazar()))) {
                            Utils.toplulukKayitEkle(context, toplulukId, "\"" + Utils.stringToTitleCase(konu.getKonuBaslik()) + context.getResources().getString(R.string.kayit_konu_silindi_1) + Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + context.getResources().getString(R.string.kayit_konu_silindi_2));
                        }

                        Toast.makeText(context, response.body().getMesaj(), Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(view, response.body().getMesaj(), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CrudResponse> call, Throwable t) {
                    Log.e(getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        });
        adKonuSil.create().show();
    }

    private void bildirimKontrol() {
        service.okunmamisBildirimleriGetir(Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<BildirimResponse>() {
            @Override
            public void onResponse(Call<BildirimResponse> call, Response<BildirimResponse> response) {
                if (response.body() != null) {
                    if (response.body().getBildirimler() != null) {
                        if (response.body().getBildirimler().size() > 0) {
                            actionBildirimler.setIcon(R.drawable.ic_notifications_active);
                        }
                    } else {
                        actionBildirimler.setIcon(R.drawable.ic_notifications);
                    }
                }
            }

            @Override
            public void onFailure(Call<BildirimResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_konu, menu);

        actionBildirimler = menu.findItem(R.id.menuKonuActionBildirimler);
        bildirimKontrol();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Utils.InternetKontrol(context)) return false;

        switch (item.getItemId()) {
            case R.id.menuKonuActionSifreDegistir:
                Intent intentSifreDegistir = new Intent(context, SifreDegistirActivity.class);
                startActivity(intentSifreDegistir);
                finish();
                return true;

            case R.id.menuKonuActionBildirimler:
                Intent intentBildirimler = new Intent(context, BildirimlerActivity.class);
                startActivity(intentBildirimler);
                finish();
                return true;

            case R.id.menuKonuActionCikis:
                Intent intentCikis = new Intent(context, MainActivity.class);
                startActivity(intentCikis);
                finish();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (Utils.InternetKontrol(context)) {
            Intent intent = new Intent(context, ToplulukActivity.class);;
            intent.putExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID, toplulukId);
            intent.putExtra(Utils.INTENT_STRING_EXTRA_KAYNAK_ACTIVITY_KONU_DETAY, true);
            startActivity(intent);
            finish();
        }
    }
}