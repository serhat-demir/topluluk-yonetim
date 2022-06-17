package com.serhat.toplulukyonetim.api;

import com.serhat.toplulukyonetim.model.BildirimResponse;
import com.serhat.toplulukyonetim.model.CrudResponse;
import com.serhat.toplulukyonetim.model.KayitResponse;
import com.serhat.toplulukyonetim.model.Konu;
import com.serhat.toplulukyonetim.model.KonuResponse;
import com.serhat.toplulukyonetim.model.OturumResponse;
import com.serhat.toplulukyonetim.model.Topluluk;
import com.serhat.toplulukyonetim.model.ToplulukResponse;
import com.serhat.toplulukyonetim.model.UyeResponse;
import com.serhat.toplulukyonetim.model.YasakliUyeResponse;
import com.serhat.toplulukyonetim.model.YorumResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    //Kullanıcı
    @POST("service_kullanici/oturum_ac.php")
    @FormUrlEncoded
    Call<OturumResponse> oturumAc(@Field("kullanici_ad") String kullanici_ad, @Field("kullanici_sifre") String kullanici_sifre);

    @POST("service_kullanici/hesap_olustur.php")
    @FormUrlEncoded
    Call<OturumResponse> hesapOlustur(@Field("kullanici_ad") String kullanici_ad, @Field("kullanici_sifre") String kullanici_sifre);

    @POST("service_kullanici/sifre_degistir.php")
    @FormUrlEncoded
    Call<CrudResponse> sifreDegistir(@Field("kullanici_id") String kullanici_id, @Field("yeni_sifre") String yeni_sifre);

    //Topluluk
    @GET("service_topluluk/tum_topluluklar.php")
    Call<ToplulukResponse> tumTopluluklar();

    @POST("service_topluluk/benim_topluluklarim.php")
    @FormUrlEncoded
    Call<ToplulukResponse> benimTopluluklarim(@Field("kullanici_id") String kullanici_id);

    @POST("service_topluluk/topluluk_ara.php")
    @FormUrlEncoded
    Call<ToplulukResponse> toplulukAra(@Field("aranan_baslik") String aranan_baslik, @Field("kullanici_id") String kullanici_id);

    @POST("service_topluluk/topluluk_olustur.php")
    @FormUrlEncoded
    Call<CrudResponse> toplulukOlustur(@Field("kullanici_id") String kullanici_id, @Field("topluluk_ad") String topluluk_ad, @Field("topluluk_aciklama") String topluluk_aciklama, @Field("topluluk_gizlilik") String topluluk_gizlilik);

    @POST("service_topluluk/topluluk_detay.php")
    @FormUrlEncoded
    Call<Topluluk> toplulukDetay(@Field("topluluk_id") String topluluk_id);

    @POST("service_topluluk/topluluk_katil.php")
    @FormUrlEncoded
    Call<CrudResponse> toplulukKatil(@Field("topluluk_id") String topluluk_id, @Field("kullanici_id") String kullanici_id);

    @POST("service_topluluk/topluluk_ayril.php")
    @FormUrlEncoded
    Call<CrudResponse> toplulukAyril(@Field("topluluk_id") String topluluk_id, @Field("kullanici_id") String kullanici_id);

    @POST("service_topluluk/topluluk_duzenle.php")
    @FormUrlEncoded
    Call<CrudResponse> toplulukDuzenle(@Field("topluluk_id") String topluluk_id, @Field("topluluk_ad") String topluluk_ad, @Field("topluluk_aciklama") String topluluk_aciklama, @Field("topluluk_gizlilik") String topluluk_gizlilik);

    @POST("service_topluluk/topluluk_sil.php")
    @FormUrlEncoded
    Call<CrudResponse> toplulukSil(@Field("topluluk_id") String topluluk_id);

    //Üye
    @POST("service_topluluk/topluluk_uyeler.php")
    @FormUrlEncoded
    Call<UyeResponse> toplulukUyeler(@Field("topluluk_id") String topluluk_id);

    @POST("service_topluluk/topluluk_uye_yasak_kontrol.php")
    @FormUrlEncoded
    Call<CrudResponse> toplulukUyeYasakKontrol(@Field("topluluk_id") String topluluk_id, @Field("kullanici_id") String kullanici_id);

    @POST("service_topluluk/topluluk_uye_duzenle.php")
    @FormUrlEncoded
    Call<CrudResponse> uyeDuzenle(@Field("topluluk_id") String topluluk_id, @Field("kullanici_id") String kullanici_id, @Field("kullanici_yetki") String kullanici_yetki);

    @POST("service_topluluk/topluluk_uye_yasakla.php")
    @FormUrlEncoded
    Call<CrudResponse> uyeYasakla(@Field("topluluk_id") String topluluk_id, @Field("kullanici_id") String kullanici_id, @Field("yasaklanma_sebebi") String yasaklanma_sebebi);

    @POST("service_topluluk/topluluk_uye_yasak_kaldir.php")
    @FormUrlEncoded
    Call<CrudResponse> uyeYasakKaldir(@Field("topluluk_id") String topluluk_id, @Field("kullanici_id") String kullanici_id);

    @POST("service_topluluk/topluluk_yasakli_uyeler.php")
    @FormUrlEncoded
    Call<YasakliUyeResponse> yasakliUyeler(@Field("topluluk_id") String topluluk_id);

    //Topluluk Kayıtları
    @POST("service_topluluk/topluluk_kayitlari.php")
    @FormUrlEncoded
    Call<KayitResponse> toplulukKayitlari(@Field("topluluk_id") String topluluk_id);

    @POST("service_topluluk/topluluk_kayit_ekle.php")
    @FormUrlEncoded
    Call<CrudResponse> toplulukKayitEkle(@Field("topluluk_id") String topluluk_id, @Field("kayit_icerik") String kayit_icerik);

    //Konu
    @POST("service_konu/konular.php")
    @FormUrlEncoded
    Call<KonuResponse> konulariGetir(@Field("topluluk_id") String topluluk_id);

    @POST("service_konu/konu_ara.php")
    @FormUrlEncoded
    Call<KonuResponse> konuAra(@Field("topluluk_id") String topluluk_id, @Field("aranan_baslik") String aranan_baslik);

    @POST("service_konu/konu_detay.php")
    @FormUrlEncoded
    Call<Konu> konuDetayGetir(@Field("konu_id") String konu_id);

    @POST("service_konu/konu_olustur.php")
    @FormUrlEncoded
    Call<CrudResponse> konuOlustur(@Field("konu_baslik") String konu_baslik, @Field("konu_icerik") String konu_icerik, @Field("konu_yazar") String konu_yazar, @Field("konu_topluluk") String konu_topluluk);

    @POST("service_konu/konu_duzenle.php")
    @FormUrlEncoded
    Call<CrudResponse> konuDuzenle(@Field("konu_id") String konu_id, @Field("konu_baslik") String konu_baslik, @Field("konu_icerik") String konu_icerik, @Field("konu_sabit") String konu_sabit);

    @POST("service_konu/konu_sil.php")
    @FormUrlEncoded
    Call<CrudResponse> konuSil(@Field("konu_id") String konu_id);

    //Yorum
    @POST("service_yorum/yorumlar.php")
    @FormUrlEncoded
    Call<YorumResponse> yorumlariGetir(@Field("konu_id") String konu_id);

    @POST("service_yorum/yorum_at.php")
    @FormUrlEncoded
    Call<CrudResponse> yorumAt(@Field("yorum_icerik") String yorum_icerik, @Field("yorum_yazar") String yorum_yazar, @Field("yorum_konu") String yorum_konu);

    @POST("service_yorum/yorum_duzenle.php")
    @FormUrlEncoded
    Call<CrudResponse> yorumDuzenle(@Field("yorum_id") String yorum_id, @Field("yorum_icerik") String yorum_icerik);

    @POST("service_yorum/yorum_sil.php")
    @FormUrlEncoded
    Call<CrudResponse> yorumSil(@Field("yorum_id") String yorum_id);

    //Bildirim
    @POST("service_bildirim/bildirimleri_getir.php")
    @FormUrlEncoded
    Call<BildirimResponse> bildirimleriGetir(@Field("kullanici_id") String kullanici_id);

    @POST("service_bildirim/okunmamis_bildirimleri_getir.php")
    @FormUrlEncoded
    Call<BildirimResponse> okunmamisBildirimleriGetir(@Field("kullanici_id") String kullanici_id);

    @POST("service_bildirim/bildirimleri_okundu_isaretle.php")
    @FormUrlEncoded
    Call<CrudResponse> bildirimleriOkunduIsaretle(@Field("kullanici_id") String kullanici_id);

    @POST("service_bildirim/bildirimleri_temizle.php")
    @FormUrlEncoded
    Call<CrudResponse> bildirimleriTemizle(@Field("kullanici_id") String kullanici_id);

    @POST("service_bildirim/bildirim_gonder.php")
    @FormUrlEncoded
    Call<CrudResponse> bildirimGonder(@Field("bildirim_alici") String bildirim_alici, @Field("bildirim_icerik") String bildirim_icerik);
}
