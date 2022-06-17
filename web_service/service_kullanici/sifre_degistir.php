<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['kullanici_id']) && isset($_POST['yeni_sifre'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $kullanici_id = $_POST['kullanici_id'];
       $yeni_sifre = $_POST['yeni_sifre'];

       $sifre_degistir = $db->prepare("update kullanicilar set kullanici_sifre = :yeni_sifre where kullanici_id = :kullanici_id");
       $sifre_degistir->execute(array(
         ":yeni_sifre" => $yeni_sifre,
         ":kullanici_id" => $kullanici_id
       ));

       if ($sifre_degistir->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Şifre değiştirildi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Yeni şifreniz eskisiyle aynı olamaz.";

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
