package com.serhat.toplulukyonetim.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.api.ApiClient;
import com.serhat.toplulukyonetim.api.ApiInterface;
import com.serhat.toplulukyonetim.model.CrudResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {
    public static String AKTIF_KULLANICI_ID;
    public static String AKTIF_KULLANICI_AD;
    public static String AKTIF_KULLANICI_SIFRE;

    public static String SHARED_PREFS_DB = "oturum_bilgisi";
    public static String SHARED_PREFS_COL_AD = "kullanici_ad";
    public static String SHARED_PREFS_COL_SIFRE = "kullanici_sifre";

    public static String DURUM_TAMAM = "1";
    public static String DURUM_HATA = "0";

    public static String INTENT_STRING_EXTRA_TOPLULUK_ID = "topluluk_id";
    public static String INTENT_STRING_EXTRA_TOPLULUK_AD = "topluluk_ad";

    public static String INTENT_STRING_EXTRA_KONU_ID = "konu_id";

    public static String INTENT_STRING_EXTRA_KAYNAK_ACTIVITY_KONU_DETAY = "kaynak_activity_konu_detay";

    public static String BILDIRIM_OKUNMADI = "0";
    public static String BILDIRIM_OKUNDU = "1";

    public static String BILDIRIM_KONU_YOK = "0";

    public static int KONU_SABIT_DEGIL = 0;
    public static int KONU_SABIT = 1;

    public static String TOPLULUK_ACIK = "0";
    public static String TOPLULUK_KAPALI = "1";
    public static String TOPLULUK_GIZLI = "2";

    public static int YETKI_YOK = -1;
    public static int YETKI_KULLANICI = 0;
    public static int YETKI_MODERATOR = 1;
    public static int YETKI_YONETICI = 2;

    public static String YASAKLANMA_SEBEBI_YOK = "Yasaklanma sebebi girilmedi.";

    public static int AKTIF_KULLANICI_YETKI = YETKI_YOK;

    public static int KULLANICI_AD_SIFRE_UZUNLUK_MIN = 4;
    public static int KULLANICI_AD_SIFRE_UZUNLUK_MAX = 20;

    public static boolean InternetKontrol(Context context) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            AlertDialog.Builder internetUyari = new AlertDialog.Builder(context);
            internetUyari.setTitle(context.getResources().getString(R.string.title_hata));
            internetUyari.setMessage(context.getResources().getString(R.string.internet_baglanilamadi));
            internetUyari.setCancelable(false);
            internetUyari.setPositiveButton((context.getResources().getString(R.string.btn_tekrar_dene)), (dialogInterface, i) -> {
                if (InternetKontrol(context)) {
                    Toast.makeText(context, context.getResources().getString(R.string.internet_baglanildi), Toast.LENGTH_SHORT).show();
                }
            });
            internetUyari.create().show();
            return false;
        }

        return true;
    }

    public static String stringToTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static void toplulukKayitEkle(Context context, String toplulukId, String kayitIcerik) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        service.toplulukKayitEkle(toplulukId, kayitIcerik).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                Log.e(context.getResources().getString(R.string.title_bilgi), context.getResources().getString(R.string.log_topluluk_kayitlari_guncellendi));
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }

    public static void bildirimGonder(Context context, String bildirimAlici, String bildirimIcerik) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        service.bildirimGonder(bildirimAlici, bildirimIcerik).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                Log.e(context.getResources().getString(R.string.title_bilgi), context.getResources().getString(R.string.log_bildirim_gonderildi));
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.title_hata), t.getMessage());
            }
        });
    }
}
