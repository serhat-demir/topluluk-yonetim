package com.serhat.toplulukyonetim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.serhat.toplulukyonetim.adapter.ToplulukAdapter;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.BildirimResponse;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.Topluluk;
import com.serhat.toplulukyonetim.model.ToplulukResponse;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopluluklarActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Context context;
    private ApiInterface service;

    private List<Topluluk> topluluklar;
    private ToplulukAdapter toplulukAdapter;

    private Toolbar toolbar;
    private RadioButton rdTumTopluluklar, rdBenimTopluluklarim;
    private RecyclerView rvToplulukListesi;
    private FloatingActionButton fabToplulukOlustur;
    private MenuItem actionBildirimler;
    private TextView txtToplulukYok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topluluklar);

        context = TopluluklarActivity.this;
        Utils.InternetKontrol(context);
        Utils.AKTIF_KULLANICI_YETKI = Utils.YETKI_YOK;

        service = ApiClient.getClient().create(ApiInterface.class);

        toolbar = findViewById(R.id.actTopluluklarToolbar);
        rdTumTopluluklar = findViewById(R.id.actTopluluklarRdTumTopluluklar);
        rdBenimTopluluklarim = findViewById(R.id.actTopluluklarRdBenimTopluluklarim);
        rvToplulukListesi = findViewById(R.id.actTopluluklarRvToplulukListesi);
        fabToplulukOlustur = findViewById(R.id.actTopluluklarFabToplulukOlustur);
        txtToplulukYok = findViewById(R.id.actTopluluklarTxtToplulukYok);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitle(Utils.stringToTitleCase(Utils.AKTIF_KULLANICI_AD) + " olarak oturum açtınız.");
        setSupportActionBar(toolbar);

        rvToplulukListesi.setHasFixedSize(true);
        rvToplulukListesi.setLayoutManager(new LinearLayoutManager(context));

        tumTopluluklariGetir();

        rdTumTopluluklar.setOnClickListener(view -> {
            if (rdTumTopluluklar.isChecked() && Utils.InternetKontrol(context)) {
                tumTopluluklariGetir();
            }
        });

        rdBenimTopluluklarim.setOnClickListener(view -> {
            if (rdBenimTopluluklarim.isChecked() && Utils.InternetKontrol(context)) {
                benimTopluluklarimiGetir();
            }
        });

        fabToplulukOlustur.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                toplulukOlustur(view);
            }
        });
    }

    private void tumTopluluklariGetir() {
        rvToplulukListesi.setVisibility(View.VISIBLE);
        txtToplulukYok.setVisibility(View.INVISIBLE);

        service.tumTopluluklar().enqueue(new Callback<ToplulukResponse>() {
            @Override
            public void onResponse(Call<ToplulukResponse> call, Response<ToplulukResponse> response) {
                if (response.body() != null && response.body().getTopluluklar() != null) {
                    if (response.body().getTopluluklar().size() == 0) {
                        txtToplulukYok.setVisibility(View.VISIBLE);
                        rvToplulukListesi.setVisibility(View.INVISIBLE);
                    } else {
                        txtToplulukYok.setVisibility(View.INVISIBLE);
                        rvToplulukListesi.setVisibility(View.VISIBLE);

                        topluluklar = response.body().getTopluluklar();
                        toplulukAdapter = new ToplulukAdapter(context, topluluklar);
                        rvToplulukListesi.setAdapter(toplulukAdapter);
                    }
                } else {
                    txtToplulukYok.setVisibility(View.VISIBLE);
                    rvToplulukListesi.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ToplulukResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void benimTopluluklarimiGetir() {
        rvToplulukListesi.setVisibility(View.VISIBLE);
        txtToplulukYok.setVisibility(View.INVISIBLE);

        service.benimTopluluklarim(Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<ToplulukResponse>() {
            @Override
            public void onResponse(Call<ToplulukResponse> call, Response<ToplulukResponse> response) {
                if (response.body() != null && response.body().getTopluluklar() != null) {
                    if (response.body().getTopluluklar().size() == 0) {
                        txtToplulukYok.setVisibility(View.VISIBLE);
                        rvToplulukListesi.setVisibility(View.INVISIBLE);
                    } else {
                        txtToplulukYok.setVisibility(View.INVISIBLE);
                        rvToplulukListesi.setVisibility(View.VISIBLE);

                        topluluklar = response.body().getTopluluklar();
                        toplulukAdapter = new ToplulukAdapter(context, topluluklar);
                        rvToplulukListesi.setAdapter(toplulukAdapter);
                    }
                } else {
                    txtToplulukYok.setVisibility(View.VISIBLE);
                    rvToplulukListesi.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ToplulukResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    private void toplulukOlustur(View view) {
        View adToplulukOlusturView = getLayoutInflater().inflate(R.layout.ad_topluluk_olustur, null);

        EditText adToplulukOlusturEdtToplulukAd, adToplulukOlusturEdtToplulukAciklama;
        RadioButton adToplulukOlusturRdAcik, adToplulukOlusturRdKapali, adToplulukOlusturRdGizli;

        adToplulukOlusturEdtToplulukAd = adToplulukOlusturView.findViewById(R.id.adToplulukOlusturEdtToplulukAd);
        adToplulukOlusturEdtToplulukAciklama = adToplulukOlusturView.findViewById(R.id.adToplulukOlusturEdtToplulukAciklama);
        adToplulukOlusturRdAcik = adToplulukOlusturView.findViewById(R.id.adToplulukOlusturRdAcik);
        adToplulukOlusturRdKapali = adToplulukOlusturView.findViewById(R.id.adToplulukOlusturRdKapali);
        adToplulukOlusturRdGizli = adToplulukOlusturView.findViewById(R.id.adToplulukOlusturRdGizli);

        AlertDialog.Builder adToplulukOlustur = new AlertDialog.Builder(context);
        adToplulukOlustur.setTitle(context.getResources().getString(R.string.adToplulukOlusturTitle));
        adToplulukOlustur.setView(adToplulukOlusturView);
        adToplulukOlustur.setNegativeButton((context.getResources().getString(R.string.btn_iptal)), (dialogInterface, i) -> {});
        adToplulukOlustur.setPositiveButton((context.getResources().getString(R.string.btn_kaydet)), (dialogInterface, i) -> {
            String toplulukAd, toplulukAciklama, toplulukGizlilik;
            toplulukAd = adToplulukOlusturEdtToplulukAd.getText().toString().trim();
            toplulukAciklama = adToplulukOlusturEdtToplulukAciklama.getText().toString().trim();

            if (adToplulukOlusturRdAcik.isChecked()) toplulukGizlilik = Utils.TOPLULUK_ACIK;
            else if (adToplulukOlusturRdKapali.isChecked()) toplulukGizlilik = Utils.TOPLULUK_KAPALI;
            else if (adToplulukOlusturRdGizli.isChecked()) toplulukGizlilik = Utils.TOPLULUK_GIZLI;
            else toplulukGizlilik = Utils.TOPLULUK_ACIK;

            if (toplulukAd.isEmpty() || toplulukAciklama.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.adToplulukOlusturEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            service.toplulukOlustur(Utils.AKTIF_KULLANICI_ID, toplulukAd, toplulukAciklama, toplulukGizlilik).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    String durum = response.body().getDurum();

                    if(durum.equals(Utils.DURUM_TAMAM)) {
                        if (rdTumTopluluklar.isChecked()) {
                            tumTopluluklariGetir();
                        } else {
                            benimTopluluklarimiGetir();
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
        adToplulukOlustur.create().show();
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
        getMenuInflater().inflate(R.menu.menu_topluluklar, menu);

        actionBildirimler = menu.findItem(R.id.menuTopluluklarActionBildirimler);
        bildirimKontrol();

        MenuItem actionToplulukAra = menu.findItem(R.id.menuTopluluklarActionToplulukAra);
        SearchView searchView = (SearchView) actionToplulukAra.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Utils.InternetKontrol(context)) return false;

        switch (item.getItemId()) {
            case R.id.menuTopluluklarActionSifreDegistir:
                Intent intentSifreDegistir = new Intent(context, SifreDegistirActivity.class);
                startActivity(intentSifreDegistir);
                finish();
                return true;

            case R.id.menuTopluluklarActionBildirimler:
                Intent intentBildirimler = new Intent(context, BildirimlerActivity.class);
                startActivity(intentBildirimler);
                finish();
                return true;

            case R.id.menuTopluluklarActionCikis:
                Intent intentCikis = new Intent(context, MainActivity.class);
                startActivity(intentCikis);
                finish();
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (rdTumTopluluklar.isChecked()) {
            service.toplulukAra(newText, "0").enqueue(new Callback<ToplulukResponse>() {
                @Override
                public void onResponse(Call<ToplulukResponse> call, Response<ToplulukResponse> response) {
                    if (response.body() != null && response.body().getTopluluklar() != null) {
                        topluluklar = response.body().getTopluluklar();
                        toplulukAdapter = new ToplulukAdapter(context, topluluklar);
                        rvToplulukListesi.setAdapter(toplulukAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ToplulukResponse> call, Throwable t) {
                    Log.e(getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        } else {
            service.toplulukAra(newText, Utils.AKTIF_KULLANICI_ID).enqueue(new Callback<ToplulukResponse>() {
                @Override
                public void onResponse(Call<ToplulukResponse> call, Response<ToplulukResponse> response) {
                    if (response.body() != null && response.body().getTopluluklar() != null) {
                        topluluklar = response.body().getTopluluklar();
                        toplulukAdapter = new ToplulukAdapter(context, topluluklar);
                        rvToplulukListesi.setAdapter(toplulukAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ToplulukResponse> call, Throwable t) {
                    Log.e(getResources().getString(R.string.title_hata), t.getMessage());
                }
            });
        }

        return false;
    }
}