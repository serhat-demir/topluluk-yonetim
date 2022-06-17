package com.serhat.toplulukyonetim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.fragment.ToplulukDetayFragment;
import com.serhat.toplulukyonetim.fragment.ToplulukKonularFragment;
import com.serhat.toplulukyonetim.model.BildirimResponse;
import com.serhat.toplulukyonetim.model.Topluluk;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToplulukActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;

    public String toplulukId;
    public Topluluk topluluk;

    public Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> fragmentTitleList = new ArrayList<>();

    private MenuItem actionBildirimler;
    public MenuItem actionKayitlar;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topluluk);

        context = ToplulukActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);

        toplulukId = getIntent().getStringExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID);
        toplulukDetayGetir(toplulukId);

        toolbar = findViewById(R.id.actToplulukToolbar);
        tabLayout = findViewById(R.id.actToplulukTabLayout);
        viewPager2 = findViewById(R.id.actToplulukViewPager2);

        fragmentList.add(new ToplulukDetayFragment());
        fragmentList.add(new ToplulukKonularFragment());

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(vpAdapter);

        fragmentTitleList.add(getResources().getString(R.string.actToplulukFrgTitleToplulukBilgileri));
        fragmentTitleList.add(getResources().getString(R.string.actToplulukFrgTitleKonular));

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(fragmentTitleList.get(position))).attach();

        if (getIntent().getBooleanExtra(Utils.INTENT_STRING_EXTRA_KAYNAK_ACTIVITY_KONU_DETAY, false)) {
            viewPager2.setCurrentItem(1);
        }
    }

    private void toplulukDetayGetir(String toplulukId) {
        service.toplulukDetay(toplulukId).enqueue(new Callback<Topluluk>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onResponse(Call<Topluluk> call, Response<Topluluk> response) {
                if (response.body() != null && response.body().getToplulukId() != null) {
                    topluluk = response.body();

                    toolbar.setTitle(Utils.stringToTitleCase(topluluk.getToplulukAd()));
                    setSupportActionBar(toolbar);

                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
                    toolbar.setNavigationOnClickListener(v -> {
                        onBackPressed();
                    });
                }
            }

            @Override
            public void onFailure(Call<Topluluk> call, Throwable t) {
                Log.e(getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_topluluk, menu);

        actionBildirimler = menu.findItem(R.id.menuToplulukActionBildirimler);
        bildirimKontrol();

        actionKayitlar = menu.findItem(R.id.menuToplulukActionToplulukKayitlari);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Utils.InternetKontrol(context)) return false;

        switch (item.getItemId()) {
            case R.id.menuToplulukActionToplulukKayitlari:
                Intent intentToplulukKayitlari = new Intent(context, ToplulukKayitlariActivity.class);
                intentToplulukKayitlari.putExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID, toplulukId);
                intentToplulukKayitlari.putExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_AD, topluluk.getToplulukAd());
                startActivity(intentToplulukKayitlari);
                finish();
                return true;

            case R.id.menuToplulukActionSifreDegistir:
                Intent intentSifreDegistir = new Intent(context, SifreDegistirActivity.class);
                startActivity(intentSifreDegistir);
                finish();
                return true;

            case R.id.menuToplulukActionBildirimler:
                Intent intentBildirimler = new Intent(context, BildirimlerActivity.class);
                startActivity(intentBildirimler);
                finish();
                return true;

            case R.id.menuToplulukActionCikis:
                Intent intentCikis = new Intent(context, MainActivity.class);
                startActivity(intentCikis);
                finish();
                return true;

            default:
                return false;
        }
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }

    @Override
    public void onBackPressed() {
        if (Utils.InternetKontrol(context)) {
            Intent intent = new Intent(context, TopluluklarActivity.class);
            startActivity(intent);
            finish();
        }
    }
}