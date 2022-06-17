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
import android.widget.TextView;

import com.serhat.toplulukyonetim.adapter.KayitAdapter;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.Kayit;
import com.serhat.toplulukyonetim.model.KayitResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToplulukKayitlariActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;

    private Toolbar toolbar;
    private RecyclerView rvKayitlar;
    private TextView txtKayitYok;

    private String toplulukId, toplulukAd;

    private List<Kayit> kayitlar;
    private KayitAdapter kayitAdapter;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topluluk_kayitlari);

        context = ToplulukKayitlariActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        toplulukId = getIntent().getStringExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID);
        toplulukAd = getIntent().getStringExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_AD);

        toolbar = findViewById(R.id.actToplulukKayitlariToolbar);
        rvKayitlar = findViewById(R.id.actToplulukKayitlariRvKayitlar);
        txtKayitYok = findViewById(R.id.actToplulukKayitlariTxtKayitYok);

        toolbar.setTitle(getResources().getString(R.string.actToplulukKayitlariTitle));
        toolbar.setSubtitle(Utils.stringToTitleCase(toplulukAd));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        rvKayitlar.setHasFixedSize(true);
        rvKayitlar.setLayoutManager(new LinearLayoutManager(context));

        kayitlariGetir();
    }

    private void kayitlariGetir() {
        service.toplulukKayitlari(toplulukId).enqueue(new Callback<KayitResponse>() {
            @Override
            public void onResponse(Call<KayitResponse> call, Response<KayitResponse> response) {
                if (response.body() != null && response.body().getKayitlar() != null) {
                    if (response.body().getKayitlar().size() == 0) {
                        txtKayitYok.setVisibility(View.VISIBLE);
                        rvKayitlar.setVisibility(View.INVISIBLE);
                    } else {
                        kayitlar = response.body().getKayitlar();
                        kayitAdapter = new KayitAdapter(context, kayitlar);
                        rvKayitlar.setAdapter(kayitAdapter);
                    }
                } else {
                    txtKayitYok.setVisibility(View.VISIBLE);
                    rvKayitlar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<KayitResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Utils.InternetKontrol(context)) {
            Intent intent = new Intent(context, ToplulukActivity.class);;
            intent.putExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID, toplulukId);
            startActivity(intent);
            finish();
        }
    }
}