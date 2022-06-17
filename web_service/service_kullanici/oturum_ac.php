<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['kullanici_ad']) && isset($_POST['kullanici_sifre'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $kullanici_ad = $_POST['kullanici_ad'];
       $kullanici_sifre = $_POST['kullanici_sifre'];

       $oturum_kontrol = $db->prepare("select * from kullanicilar where kullanici_ad = :kullanici_ad and kullanici_sifre = :kullanici_sifre");
       $oturum_kontrol->execute(array(
         ":kullanici_ad" => $kullanici_ad,
         ":kullanici_sifre" => $kullanici_sifre
       ));

       if ($oturum_kontrol->rowCount() > 0) {
          $response["durum"] = 1;
          $response["mesaj"] = "Oturum açıldı.";
          $response["kullanici_id"] = $oturum_kontrol->fetchAll(PDO::FETCH_ASSOC)[0]["kullanici_id"];

          echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Kullanıcı adı veya şifre yanlış.";

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
