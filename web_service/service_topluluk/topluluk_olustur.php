<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['kullanici_id']) && isset($_POST['topluluk_ad']) && isset($_POST['topluluk_aciklama']) && isset($_POST['topluluk_gizlilik'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $kullanici_id = $_POST['kullanici_id'];
       $topluluk_ad = $_POST['topluluk_ad'];
       $topluluk_aciklama = $_POST['topluluk_aciklama'];
       $topluluk_gizlilik = $_POST['topluluk_gizlilik'];

       $topluluk_ad_kontrol = $db->prepare("select * from topluluklar where topluluk_ad like '%". $topluluk_ad ."%'");
       $topluluk_ad_kontrol->execute(array());

       if ($topluluk_ad_kontrol->rowCount() == 0) {
         $topluluk_olustur = $db->prepare("insert into topluluklar (topluluk_ad, topluluk_aciklama, topluluk_gizlilik) values(:topluluk_ad, :topluluk_aciklama, :topluluk_gizlilik)");
         $topluluk_olustur->execute(array(
           ":topluluk_ad" => $topluluk_ad,
           ":topluluk_aciklama" => $topluluk_aciklama,
           ":topluluk_gizlilik" => $topluluk_gizlilik
         ));

         if ($topluluk_olustur->rowCount() > 0) {
           $topluluk_id = $db->lastInsertId();

           $topluluk_yonetici_ekle = $db->prepare("insert into topluluk_uyeler (topluluk_id, kullanici_id, kullanici_yetki) values(:topluluk_id, :kullanici_id, :kullanici_yetki)");
           $topluluk_yonetici_ekle->execute(array(
             ":topluluk_id" => $topluluk_id,
             ":kullanici_id" => $kullanici_id,
             ":kullanici_yetki" => YETKI_YONETICI
           ));

           $response["durum"] = 1;
           $response["mesaj"] = "Topluluk oluşturuldu.";

           echo json_encode($response);
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Topluluk oluşturulurken bir sorun oluştu. Daha sonra tekrar deneyin.";

           echo json_encode($response);
         }
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Bu ad başka bir topluluk tarafından kullanılıyor.";

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
