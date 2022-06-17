package com.serhat.toplulukyonetim.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.ToplulukActivity;
import com.serhat.toplulukyonetim.adapter.KonuAdapter;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.Konu;
import com.serhat.toplulukyonetim.model.KonuResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToplulukKonularFragment extends Fragment {
    private Context context;
    private ApiInterface service;

    private String toplulukId;

    private EditText edtKonuAra;
    private RecyclerView rvKonular;
    private FloatingActionButton fabKonuOlustur;
    private TextView txtKonuYok;

    private List<Konu> konular;
    private KonuAdapter konuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tasarim = inflater.inflate(R.layout.fragment_topluluk_konular, container, false);

        context = getActivity();
        service = ApiClient.getClient().create(ApiInterface.class);

        toplulukId = ((ToplulukActivity) getActivity()).toplulukId;

        edtKonuAra = tasarim.findViewById(R.id.frgToplulukKonularEdtKonuAra);
        rvKonular = tasarim.findViewById(R.id.frgToplulukKonularRvKonular);
        fabKonuOlustur = tasarim.findViewById(R.id.frgToplulukKonularFabKonuOlustur);
        txtKonuYok = tasarim.findViewById(R.id.frgToplulukKonularTxtKonuYok);

        rvKonular.setHasFixedSize(true);
        rvKonular.setLayoutManager(new LinearLayoutManager(context));

        konulariGetir();

        edtKonuAra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                konuAra();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fabKonuOlustur.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                konuOlustur(view);
            }
        });

        if (Utils.AKTIF_KULLANICI_YETKI == Utils.YETKI_YOK) {
            fabKonuOlustur.setVisibility(View.INVISIBLE);
        }

        return tasarim;
    }

    private void konulariGetir() {
        service.konulariGetir(toplulukId).enqueue(new Callback<KonuResponse>() {
            @Override
            public void onResponse(Call<KonuResponse> call, Response<KonuResponse> response) {
                if (response.body() != null && response.body().getKonular() != null) {
                    if (response.body().getKonular().size() == 0) {
                        txtKonuYok.setVisibility(View.VISIBLE);
                        rvKonular.setVisibility(View.INVISIBLE);
                    } else {
                        txtKonuYok.setVisibility(View.INVISIBLE);
                        rvKonular.setVisibility(View.VISIBLE);

                        konular = response.body().getKonular();
                        konuAdapter = new KonuAdapter(context, konular);
                        rvKonular.setAdapter(konuAdapter);
                    }
                } else {
                    txtKonuYok.setVisibility(View.VISIBLE);
                    rvKonular.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<KonuResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void konuAra() {
        service.konuAra(toplulukId, edtKonuAra.getText().toString().trim()).enqueue(new Callback<KonuResponse>() {
            @Override
            public void onResponse(Call<KonuResponse> call, Response<KonuResponse> response) {
                if (response.body() != null && response.body().getKonular() != null) {
                    konular = response.body().getKonular();
                    konuAdapter = new KonuAdapter(context, konular);
                    rvKonular.setAdapter(konuAdapter);
                }
            }

            @Override
            public void onFailure(Call<KonuResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void konuOlustur(View view) {
        View adKonuOlusturView = getLayoutInflater().inflate(R.layout.ad_konu_olustur, null);

        EditText adKonuOlusturEdtKonuBaslik, adKonuOlusturEdtKonuIcerik;

        adKonuOlusturEdtKonuBaslik = adKonuOlusturView.findViewById(R.id.adKonuOlusturEdtKonuBaslik);
        adKonuOlusturEdtKonuIcerik = adKonuOlusturView.findViewById(R.id.adKonuOlusturEdtKonuIcerik);

        AlertDialog.Builder adKonuOlustur = new AlertDialog.Builder(context);
        adKonuOlustur.setTitle(context.getResources().getString(R.string.adKonuOlusturTitle));
        adKonuOlustur.setView(adKonuOlusturView);
        adKonuOlustur.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> {});
        adKonuOlustur.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
            String konuBaslik, konuIcerik;
            konuBaslik = adKonuOlusturEdtKonuBaslik.getText().toString().trim();
            konuIcerik = adKonuOlusturEdtKonuIcerik.getText().toString().trim();

            if (konuBaslik.isEmpty() || konuIcerik.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.adKonuOlusturEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            service.konuOlustur(konuBaslik, konuIcerik, Utils.AKTIF_KULLANICI_ID, toplulukId).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        konulariGetir();
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
        adKonuOlustur.create().show();
    }
}