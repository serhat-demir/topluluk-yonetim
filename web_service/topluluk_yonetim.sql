-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 08 Haz 2022, 19:36:50
-- Sunucu sürümü: 10.4.24-MariaDB
-- PHP Sürümü: 8.1.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `topluluk_yonetim`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `bildirimler`
--

CREATE TABLE `bildirimler` (
  `bildirim_id` int(11) NOT NULL,
  `bildirim_alici` int(11) NOT NULL,
  `bildirim_icerik` text COLLATE utf8_turkish_ci NOT NULL,
  `bildirim_okundu` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: okunmadı, 1: okundu'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `konular`
--

CREATE TABLE `konular` (
  `konu_id` int(11) NOT NULL,
  `konu_baslik` varchar(50) COLLATE utf8_turkish_ci NOT NULL,
  `konu_icerik` varchar(500) COLLATE utf8_turkish_ci NOT NULL,
  `konu_yazar` int(11) NOT NULL,
  `konu_topluluk` int(11) NOT NULL,
  `konu_tarih` datetime NOT NULL,
  `konu_sabit` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: sabit değil, 1: sabit'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tetikleyiciler `konular`
--
DELIMITER $$
CREATE TRIGGER `yorumlari_temizle` AFTER DELETE ON `konular` FOR EACH ROW delete from yorumlar where yorum_konu = old.konu_id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `kullanicilar`
--

CREATE TABLE `kullanicilar` (
  `kullanici_id` int(11) NOT NULL,
  `kullanici_ad` varchar(20) COLLATE utf8_turkish_ci NOT NULL,
  `kullanici_sifre` varchar(20) COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tetikleyiciler `kullanicilar`
--
DELIMITER $$
CREATE TRIGGER `bildirim_temizle` AFTER DELETE ON `kullanicilar` FOR EACH ROW delete from bildirimler where bildirim_alici = old.kullanici_id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `topluluklar`
--

CREATE TABLE `topluluklar` (
  `topluluk_id` int(11) NOT NULL,
  `topluluk_ad` varchar(50) COLLATE utf8_turkish_ci NOT NULL,
  `topluluk_aciklama` varchar(250) COLLATE utf8_turkish_ci NOT NULL,
  `topluluk_gizlilik` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: açık, 1: kapalı, 2: gizli'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tetikleyiciler `topluluklar`
--
DELIMITER $$
CREATE TRIGGER `kayit_temizle` AFTER DELETE ON `topluluklar` FOR EACH ROW delete from topluluk_kayitlari where kayit_topluluk = old.topluluk_id
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `konulari_temizle` AFTER DELETE ON `topluluklar` FOR EACH ROW delete from konular where konu_topluluk = old.topluluk_id
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uye_temizle` AFTER DELETE ON `topluluklar` FOR EACH ROW delete from topluluk_uyeler where topluluk_id = old.topluluk_id
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `yasakli_uye_temizle` AFTER DELETE ON `topluluklar` FOR EACH ROW delete from topluluk_yasakli_uyeler where topluluk_id = old.topluluk_id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `topluluk_kayitlari`
--

CREATE TABLE `topluluk_kayitlari` (
  `kayit_id` int(11) NOT NULL,
  `kayit_topluluk` int(11) NOT NULL,
  `kayit_icerik` text COLLATE utf8_turkish_ci NOT NULL,
  `kayit_tarih` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `topluluk_uyeler`
--

CREATE TABLE `topluluk_uyeler` (
  `topluluk_id` int(11) NOT NULL,
  `kullanici_id` int(11) NOT NULL,
  `kullanici_yetki` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: kullanıcı, 1: moderatör, 2: yönetici'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `topluluk_yasakli_uyeler`
--

CREATE TABLE `topluluk_yasakli_uyeler` (
  `topluluk_id` int(11) NOT NULL,
  `kullanici_id` int(11) NOT NULL,
  `yasaklanma_sebebi` varchar(250) COLLATE utf8_turkish_ci NOT NULL,
  `yasaklanma_tarihi` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `yorumlar`
--

CREATE TABLE `yorumlar` (
  `yorum_id` int(11) NOT NULL,
  `yorum_icerik` varchar(500) COLLATE utf8_turkish_ci NOT NULL,
  `yorum_yazar` int(11) NOT NULL,
  `yorum_konu` int(11) NOT NULL,
  `yorum_tarih` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `bildirimler`
--
ALTER TABLE `bildirimler`
  ADD PRIMARY KEY (`bildirim_id`);

--
-- Tablo için indeksler `konular`
--
ALTER TABLE `konular`
  ADD PRIMARY KEY (`konu_id`);

--
-- Tablo için indeksler `kullanicilar`
--
ALTER TABLE `kullanicilar`
  ADD PRIMARY KEY (`kullanici_id`);

--
-- Tablo için indeksler `topluluklar`
--
ALTER TABLE `topluluklar`
  ADD PRIMARY KEY (`topluluk_id`);

--
-- Tablo için indeksler `topluluk_kayitlari`
--
ALTER TABLE `topluluk_kayitlari`
  ADD PRIMARY KEY (`kayit_id`);

--
-- Tablo için indeksler `yorumlar`
--
ALTER TABLE `yorumlar`
  ADD PRIMARY KEY (`yorum_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `bildirimler`
--
ALTER TABLE `bildirimler`
  MODIFY `bildirim_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `konular`
--
ALTER TABLE `konular`
  MODIFY `konu_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `kullanicilar`
--
ALTER TABLE `kullanicilar`
  MODIFY `kullanici_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `topluluklar`
--
ALTER TABLE `topluluklar`
  MODIFY `topluluk_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `topluluk_kayitlari`
--
ALTER TABLE `topluluk_kayitlari`
  MODIFY `kayit_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `yorumlar`
--
ALTER TABLE `yorumlar`
  MODIFY `yorum_id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
