package com.serhat.toplulukyonetim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SifreDegistirActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;

    private Toolbar toolbar;
    private EditText edtYeniSifre, edtYeniSifreOnay;
    private Button btnSifreDegistir;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);

        context = SifreDegistirActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);

        toolbar = findViewById(R.id.actSifreDegistirToolbar);
        edtYeniSifre = findViewById(R.id.actSifreDegistirEdtYeniSifre);
        edtYeniSifreOnay = findViewById(R.id.actSifreDegistirEdtYeniSifreOnay);
        btnSifreDegistir = findViewById(R.id.actSifreDegistirBtnSifreDegistir);

        toolbar.setTitle(getResources().getString(R.string.actSifreDegistirToolbarTitle));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        btnSifreDegistir.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                String yeniSifre = edtYeniSifre.getText().toString().trim();
                String yeniSifreOnay = edtYeniSifreOnay.getText().toString().trim();

                if (yeniSifre.isEmpty() || yeniSifreOnay.isEmpty()) {
                    Snackbar.make(view, getResources().getString(R.string.actSifreDegistirEmptyError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (yeniSifre.length() < Utils.KULLANICI_AD_SIFRE_UZUNLUK_MIN || yeniSifre.length() > Utils.KULLANICI_AD_SIFRE_UZUNLUK_MAX || yeniSifreOnay.length() < Utils.KULLANICI_AD_SIFRE_UZUNLUK_MIN || yeniSifreOnay.length() > Utils.KULLANICI_AD_SIFRE_UZUNLUK_MAX) {
                    Snackbar.make(view, getResources().getString(R.string.actSifreDegistirLengthError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!yeniSifre.equals(yeniSifreOnay)) {
                    Snackbar.make(view, getResources().getString(R.string.actSifreDegistirEqualError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                sifreDegistir(yeniSifre, view);
            }
        });
    }

    public void sifreDegistir(String yeniSifre, View view) {
        service.sifreDegistir(Utils.AKTIF_KULLANICI_ID, yeniSifre).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                String durum = response.body().getDurum();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    Toast.makeText(context, response.body().getMesaj(), Toast.LENGTH_SHORT).show();
                    Utils.AKTIF_KULLANICI_SIFRE = yeniSifre;
                    onBackPressed();
                } else {
                    Snackbar.make(view, response.body().getMesaj(), Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Utils.InternetKontrol(context)) {
            Intent intent = new Intent(context, TopluluklarActivity.class);;
            startActivity(intent);
            finish();
        }
    }
}