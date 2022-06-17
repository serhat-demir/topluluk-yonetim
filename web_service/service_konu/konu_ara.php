<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();
  $response["konular"] = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['aranan_baslik'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $aranan_baslik = $_POST['aranan_baslik'];

       $konular = $db->prepare("select konular.*, (select count(*) from yorumlar where yorum_konu = konular.konu_id) as konu_yorum_sayisi, kullanicilar.kullanici_ad as konu_yazar_ad from konular inner join kullanicilar on konular.konu_yazar = kullanicilar.kullanici_id where konular.konu_topluluk = :topluluk_id and konular.konu_baslik like '%". $aranan_baslik ."%' order by konu_sabit desc, konu_id desc");
       $konular->execute(array(
         ":topluluk_id" => $topluluk_id
       ));

       if ($konular->rowCount() > 0) {
         foreach ($konular->fetchAll(PDO::FETCH_ASSOC) as $konu) {
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
           array_push($response["konular"], $konu);
         }

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Herhangi bir konu bulunmuyor.";

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
