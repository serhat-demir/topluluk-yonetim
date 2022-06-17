package com.serhat.toplulukyonetim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.OturumResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEdit;
    private ApiInterface service;

    private Toolbar toolbar;
    private EditText edtKullaniciAd, edtKullaniciSifre;
    private CheckBox cbBeniHatirla;
    private Button btnHesapOlustur, btnOturumAc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        Utils.InternetKontrol(context);
        Utils.AKTIF_KULLANICI_YETKI = Utils.YETKI_YOK;

        sp = getSharedPreferences(Utils.SHARED_PREFS_DB, MODE_PRIVATE);
        spEdit = sp.edit();

        service = ApiClient.getClient().create(ApiInterface.class);

        toolbar = findViewById(R.id.actMainToolbar);
        edtKullaniciAd = findViewById(R.id.actMainEdtKullaniciAd);
        edtKullaniciSifre = findViewById(R.id.actMainEdtKullaniciSifre);
        cbBeniHatirla = findViewById(R.id.actMainCbBeniHatirla);
        btnHesapOlustur = findViewById(R.id.actMainBtnHesapOlustur);
        btnOturumAc = findViewById(R.id.actMainBtnOturumAc);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        btnHesapOlustur.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                String kullaniciAd = edtKullaniciAd.getText().toString().trim();
                String sifre = edtKullaniciSifre.getText().toString().trim();
                boolean beniHatirla = cbBeniHatirla.isChecked();

                if (kullaniciAd.isEmpty() || sifre.isEmpty()) {
                    Snackbar.make(view, getResources().getString(R.string.actMainEmptyError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (kullaniciAd.length() < 4 || kullaniciAd.length() > 20 || sifre.length() < 4 || sifre.length() > 20) {
                    Snackbar.make(view, getResources().getString(R.string.actMainLengthError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                hesapOlustur(kullaniciAd, sifre, beniHatirla, view);
            }
        });

        btnOturumAc.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                String kullaniciAd = edtKullaniciAd.getText().toString().trim();
                String kullaniciSifre = edtKullaniciSifre.getText().toString().trim();
                boolean beniHatirla = cbBeniHatirla.isChecked();

                if (kullaniciAd.isEmpty() || kullaniciSifre.isEmpty()) {
                    Snackbar.make(view, getResources().getString(R.string.actMainEmptyError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (kullaniciAd.length() < Utils.KULLANICI_AD_SIFRE_UZUNLUK_MIN || kullaniciAd.length() > Utils.KULLANICI_AD_SIFRE_UZUNLUK_MAX || kullaniciSifre.length() < Utils.KULLANICI_AD_SIFRE_UZUNLUK_MIN || kullaniciSifre.length() > Utils.KULLANICI_AD_SIFRE_UZUNLUK_MAX) {
                    Snackbar.make(view, getResources().getString(R.string.actMainLengthError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                oturumAc(kullaniciAd, kullaniciSifre, beniHatirla, view);
            }
        });

        kayitliBilgileriGetir();
    }

    private void kayitliBilgileriGetir() {
        String kullaniciAd = sp.getString(Utils.SHARED_PREFS_COL_AD, null);
        String kullaniciSifre = sp.getString(Utils.SHARED_PREFS_COL_SIFRE, null);

        if (kullaniciAd != null && kullaniciSifre != null) {
            edtKullaniciAd.setText(kullaniciAd);
            edtKullaniciSifre.setText(kullaniciSifre);
        }
    }

    private void oturumAc(String kullaniciAd, String kullaniciSifre, boolean beniHatirla, View view) {
        service.oturumAc(kullaniciAd, kullaniciSifre).enqueue(new Callback<OturumResponse>() {
            @Override
            public void onResponse(Call<OturumResponse> call, Response<OturumResponse> response) {
                String durum = response.body().getDurum();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    Utils.AKTIF_KULLANICI_ID = response.body().getKullaniciId();
                    Utils.AKTIF_KULLANICI_AD = kullaniciAd;
                    Utils.AKTIF_KULLANICI_SIFRE = kullaniciSifre;

                    if(beniHatirla) {
                        spEdit.putString(Utils.SHARED_PREFS_COL_AD, kullaniciAd);
                        spEdit.putString(Utils.SHARED_PREFS_COL_SIFRE, kullaniciSifre);
                        spEdit.commit();
                    }

                    Intent intent = new Intent(context, TopluluklarActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(context, response.body().getMesaj(), Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, response.body().getMesaj(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OturumResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void hesapOlustur(String kullaniciAd, String kullaniciSifre, boolean beniHatirla, View view) {
        service.hesapOlustur(kullaniciAd, kullaniciSifre).enqueue(new Callback<OturumResponse>() {
            @Override
            public void onResponse(Call<OturumResponse> call, Response<OturumResponse> response) {
                String durum = response.body().getDurum();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    Utils.AKTIF_KULLANICI_ID = response.body().getKullaniciId();
                    Utils.AKTIF_KULLANICI_AD = kullaniciAd;
                    Utils.AKTIF_KULLANICI_SIFRE = kullaniciSifre;

                    if(beniHatirla) {
                        spEdit.putString(Utils.SHARED_PREFS_COL_AD, kullaniciAd);
                        spEdit.putString(Utils.SHARED_PREFS_COL_SIFRE, kullaniciSifre);
                        spEdit.commit();
                    }

                    Intent intent = new Intent(context, TopluluklarActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(context, response.body().getMesaj(), Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, response.body().getMesaj(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OturumResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }
}