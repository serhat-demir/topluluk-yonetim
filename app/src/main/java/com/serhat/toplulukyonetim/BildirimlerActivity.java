package com.serhat.toplulukyonetim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.adapter.BildirimAdapter;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.Bildirim;
import com.serhat.toplulukyonetim.model.BildirimResponse;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BildirimlerActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;

    private List<Bildirim> bildirimler;
    private BildirimAdapter bildirimAdapter;

    private Toolbar toolbar;
    private RecyclerView rvBildirimListesi;
    private FloatingActionButton fabBildirimleriTemizle;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirimler);

        context = BildirimlerActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);

        toolbar = findViewById(R.id.actBildirimlerToolbar);
        rvBildirimListesi = findViewById(R.id.actBildirimlerRvBildirimListesi);
        fabBildirimleriTemizle = findViewById(R.id.actBildirimlerFabBildirimleriTemizle);

        toolbar.setTitle(getResources().getString(R.string.actBildirimlerToolbarTitle));
        okunmamisBildirimSayisiniGetir();
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        rvBildirimListesi.setHasFixedSize(true);
        rvBildirimListesi.setLayoutManager(new LinearLayoutManager(context));

        bildirimleriGetir();
        bildirimleriOkunduIsaretle();

        fabBildirimleriTemizle.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                bildirimleriTemizle(view);
            }
        });
    }

    private void bildirimleriGetir() {
        service.bildirimleriGetir(Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<BildirimResponse>() {
            @Override
            public void onResponse(Call<BildirimResponse> call, Response<BildirimResponse> response) {
                if (response.body() != null && response.body().getBildirimler() != null) {
                    bildirimler = response.body().getBildirimler();
                    bildirimAdapter = new BildirimAdapter(context, bildirimler);
                    rvBildirimListesi.setAdapter(bildirimAdapter);
                } else {
                    toolbar.setSubtitle(getResources().getString(R.string.actBildirimlerToolbarSubTitleBildirimYok));
                }
            }

            @Override
            public void onFailure(Call<BildirimResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void okunmamisBildirimSayisiniGetir() {
        service.okunmamisBildirimleriGetir(Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<BildirimResponse>() {
            @Override
            public void onResponse(Call<BildirimResponse> call, Response<BildirimResponse> response) {
                if (response.body() != null) {
                    if (response.body().getBildirimler() != null) {
                        if (response.body().getBildirimler().size() > 0) {
                            toolbar.setSubtitle(response.body().getBildirimler().size() + " " + getResources().getString(R.string.actBildirimlerToolbarSubTitleBildirimlerOkunmamis));
                        }
                    } else {
                        toolbar.setSubtitle(getResources().getString(R.string.actBildirimlerToolbarSubTitleBildirimlerOkunmus));
                    }
                }
            }

            @Override
            public void onFailure(Call<BildirimResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void bildirimleriOkunduIsaretle() {
        service.bildirimleriOkunduIsaretle(Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                Log.e(getString(R.string.response), response.body().getMesaj());
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void bildirimleriTemizle(View view) {
        service.bildirimleriTemizle(Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<CrudResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                String durum = response.body().getDurum();

                if(durum.equals(Utils.DURUM_TAMAM)) {
                    toolbar.setSubtitle(getResources().getString(R.string.actBildirimlerToolbarSubTitleBildirimYok));
                    Toast.makeText(context, response.body().getMesaj(), Toast.LENGTH_SHORT).show();
                    bildirimler.clear();
                    bildirimAdapter.notifyDataSetChanged();
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