<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['kullanici_ad']) && isset($_POST['kullanici_sifre'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $kullanici_ad = $_POST['kullanici_ad'];
       $kullanici_sifre = $_POST['kullanici_sifre'];

       $kullanici_ad_kontrol = $db->prepare("select * from kullanicilar where kullanici_ad = :kullanici_ad");
       $kullanici_ad_kontrol->execute(array(
         ":kullanici_ad" => $kullanici_ad
       ));

       if ($kullanici_ad_kontrol->rowCount() == 0) {
         $kullanici_kayit = $db->prepare("insert into kullanicilar (kullanici_ad, kullanici_sifre) values(:kullanici_ad, :kullanici_sifre)");
         $kullanici_kayit->execute(array(
           ":kullanici_ad" => $kullanici_ad,
           ":kullanici_sifre" => $kullanici_sifre
         ));

         if ($kullanici_kayit->rowCount() > 0) {
           $response["durum"] = 1;
           $response["mesaj"] = "Hesap oluşturuldu.";
           $response["kullanici_id"] = $db->lastInsertId();

           echo json_encode($response);
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Hesap oluşturulurken bir sorun oluştu. Daha sonra tekrar deneyin.";

           echo json_encode($response);
         }
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Bu kullanıcı adı başkası tarafından alınmış.";

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
