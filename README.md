# Topluluk Yönetim
Android için topluluk yönetim uygulaması.

## İçerik
- Genel Bilgi
- Özellikler
- Teknoloji
- Kurulum
- Ekran Görüntüleri

## Genel Bilgi
Topluluk yönetim uygulaması, kullanıcıların ilgili oldukları herhangi bir konu hakkında bir araya gelip bilgi paylaşımında bulunması amacıyla geliştirilmiş olan bir uygulamadır.

## Özellikler
Kullanıcıların uygulama içinde yapabileceği işlemler:
- Topluluk oluştur
- Topluluklara katıl/ayrıl (sadece açık topluluklar)
- Bütün toplulukların konularını görüntüle (sadece açık topluluklar)

Uygulama kullanıcılarının topluluk içindeki yetkileri:
|İşlem|Topluluk Üyesi|Topluluk Moderatörü|Topluluk Yöneticisi|
|:---:|:---:|:---:|:---:|
|Konu Aç|✔|✔|✔|
|Kendi Konusunu Düzenle/Sil|✔|✔|✔|
|Yorum At|✔|✔|✔|
|Kendi Yorumunu Düzenle/Sil|✔|✔|✔|
|Bütün Konuları Düzenle/Sil|❌|✔|✔|
|Bütün Yorumları Düzenle/Sil|❌|✔|✔|
|Bütün Konuları Sabitle/Sök|❌|✔|✔|
|Topluluk Kayıtlarını Görüntüle|❌|✔|✔|
|Topluluk Bilgilerini Düzenle|❌|❌|✔|
|Topluluk Üyelerini Yasakla|❌|❌|✔|
|Topluluk Üyelerinin Yetkilerini Düzenle|❌|❌|✔|

Topluluk gizlilik ayarları:
|İşlem|Açık|Kapalı|Gizli|
|:---:|:---:|:---:|:---:|
|Topluluk Detaylarını Görüntüle|✔|✔|❌|
|Topluluk Konularını Görüntüle|✔|✔|❌|
|Topluluğa Katıl|✔|❌|❌|


## Teknoloji
Uygulamada kullanılan diller ve kütüphaneler:
- **[Java](https://developer.android.com/studio/intro)**
- **[Retrofit](https://square.github.io/retrofit/)**
- **[PHP](https://www.php.net/)**
- **[MySql](https://www.mysql.com/)**

**IDE**: [Android Studio Bumblebee | 2021.1.1 Patch 2](https://androidstudio.googleblog.com/2022/02/android-studio-bumblebee-202111-patch-2.html)

<details>
  <summary><b>Veri Tabanı Tasarımı</b></summary>
	
  ![veri tabanı tasarımı](https://img001.prntscr.com/file/img001/wmbmIQi5Qs2k3fXWhECAlg.png)
</details>

## Kurulum
Uygulama kodlarının düzenlenmesi ve çalıştırılması için kurulum adımları:

#### Gereksinimler:
Uygulama kodlarının düzenleneceği cihazda Android Studio IDE kurulu olmalı.

#### Kurulum adımları:
- Projenin GitHub'dan klonlanması.
	- `git clone https://github.com/serhatdemirr/topluluk-yonetim.git`
- Veri tabanı kurulumu için `web_service/topluluk_yonetim.sql` dosyasının kurulum yapılacak sunucuda phpMyAdmin üstünden içeri aktarılması.
- Web servislerindeki veri tabanı bağlantı kodlarının düzenlenmesi.
	- `web_service/db_config.php`
- Web servislerin veri tabanının kurulu olduğu sunucuya aktarılması.
- Projenin Android Studio IDE üstünden açılması.
	- `file > open > projenin kurulu olduğu dizin`
- Android Studio IDE üstünden `ApiClient` sınıfındaki `BASE_URL` ayarının düzenlenmesi.

Bu adımların sonunda, proje herhangi bir emülatör üstünde çalıştırılabilir veya kod derlenip APK çıktısı alınabilir.

#### Uygulamanın çalıştırılabileceği android versiyonları:
|Codename|Version|API Level|
|:---:|:---:|:---:|
|Android12L|12|API Level 32|
|Android12|12|API Level 31|
|Android11|11|API Level 30|
|Android10|10|API Level 29|
|Pie|9|API Level 28|
|Oreo|8.1.0|API Level 27|
|Oreo|8.0.0|API Level 26|
|Nougat|7.1|API Level 25|
|Nougat|7.0|API Level 24|
|Marshmallow|6.0|API Level 23|
|Lollipop|5.1|API Level 22|
|Lollipop|5.0|API Level 21|

## Ekran Görüntüleri - Demo
![img1](https://img001.prntscr.com/file/img001/M-PT9sPbRFa9vjTwZ7MYnw.png)
![img2](https://img001.prntscr.com/file/img001/CUnIqWWgReKXXb2hF93mYQ.png)
![img3](https://img001.prntscr.com/file/img001/KR12cNR8TLG3GPHC5uCNrQ.png)
