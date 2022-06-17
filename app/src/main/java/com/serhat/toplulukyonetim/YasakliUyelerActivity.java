package com.serhat.toplulukyonetim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.serhat.toplulukyonetim.adapter.YasakliUyeAdapter;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.YasakliUye;
import com.serhat.toplulukyonetim.model.YasakliUyeResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YasakliUyelerActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;

    private Toolbar toolbar;
    public RecyclerView rvUyeler;
    public TextView txtUyeYok;

    private String toplulukId, toplulukAd;

    private List<YasakliUye> yasakliUyeler;
    private YasakliUyeAdapter yasakliUyeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yasakli_uyeler);

        context = YasakliUyelerActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        toplulukId = getIntent().getStringExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID);
        toplulukAd = getIntent().getStringExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_AD);

        toolbar = findViewById(R.id.actYasakliUyelerToolbar);
        rvUyeler = findViewById(R.id.actYasakliUyelerRvUyeler);
        txtUyeYok = findViewById(R.id.actYasakliUyelerTxtUyeYok);

        toolbar.setTitle(getResources().getString(R.string.actYasakliUyelerTitle));
        toolbar.setSubtitle(Utils.stringToTitleCase(toplulukAd));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        rvUyeler.setHasFixedSize(true);
        rvUyeler.setLayoutManager(new LinearLayoutManager(context));

        yasakliUyeleriGetir();
    }

    private void yasakliUyeleriGetir() {
        service.yasakliUyeler(toplulukId).enqueue(new Callback<YasakliUyeResponse>() {
            @Override
            public void onResponse(Call<YasakliUyeResponse> call, Response<YasakliUyeResponse> response) {
                if (response.body() != null && response.body().getYasakliUyeler() != null) {
                    if (response.body().getYasakliUyeler().size() == 0) {
                        txtUyeYok.setVisibility(View.VISIBLE);
                        rvUyeler.setVisibility(View.INVISIBLE);
                    } else {
                        yasakliUyeler = response.body().getYasakliUyeler();
                        yasakliUyeAdapter = new YasakliUyeAdapter(context, yasakliUyeler, service, toplulukId);
                        rvUyeler.setAdapter(yasakliUyeAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<YasakliUyeResponse> call, Throwable t) {
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