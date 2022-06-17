<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['kullanici_id']) && isset($_POST['kullanici_yetki'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $kullanici_id = $_POST['kullanici_id'];
       $kullanici_yetki = $_POST['kullanici_yetki'];

       $uye_duzenle = $db->prepare("update topluluk_uyeler set kullanici_yetki = :kullanici_yetki where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
       $uye_duzenle->execute(array(
         ":kullanici_yetki" => $kullanici_yetki,
         ":topluluk_id" => $topluluk_id,
         ":kullanici_id" => $kullanici_id
       ));

       if ($uye_duzenle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Üye yetkisi düzenlendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Üye yetkisi düzenlenemedi.";

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
