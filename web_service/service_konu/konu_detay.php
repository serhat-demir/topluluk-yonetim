<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['konu_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $konu_id = $_POST['konu_id'];

       $konu_detay = $db->prepare("select konular.*, (select count(*) from yorumlar where yorum_konu = konular.konu_id) as konu_yorum_sayisi, kullanicilar.kullanici_ad as konu_yazar_ad from konular inner join kullanicilar on konular.konu_yazar = kullanicilar.kullanici_id where konular.konu_id = :konu_id");
       $konu_detay->execute(array(
         ":konu_id" => $konu_id
       ));

       if ($konu_detay->rowCount() > 0) {
         $konu = $konu_detay->fetchAll(PDO::FETCH_ASSOC)[0];

         $konu_yazar_yetki = $db->prepare("select kullanici_yetki from topluluk_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
         $konu_yazar_yetki->execute(array(
           ":topluluk_id" => $konu["konu_topluluk"],
           ":kullanici_id" => $konu["konu_yazar"]
         ));

         if ($konu_yazar_yetki->rowCount() > 0) {
           $yazar_yetki = $konu_yazar_yetki->fetchAll(PDO::FETCH_ASSOC)[0];
         } else {
           $yazar_yetki["kullanici_yetki"] = intval(YETKI_YOK);
         }

         $konu["konu_yazar_yetki"] = $yazar_yetki["kullanici_yetki"];
         $response = $konu;
         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Konu bulunamadı.";

         echo json_encode($response);
       }
    } catch ( PDOException $e ){
         print $e->getMessage();
    }
  } else {
    $response["durum"] = 0;
    $response["mesaj"] = "Gerekli alanlar doldurulmamış.";

    echo json_encode($response);
  }
?>
