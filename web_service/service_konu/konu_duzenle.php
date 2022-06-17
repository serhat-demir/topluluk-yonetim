<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['konu_id']) && isset($_POST['konu_baslik']) && isset($_POST['konu_icerik']) && isset($_POST['konu_sabit'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $konu_id = $_POST['konu_id'];
       $konu_baslik = $_POST['konu_baslik'];
       $konu_icerik = $_POST['konu_icerik'];
       $konu_sabit = $_POST['konu_sabit'];

       $konu_duzenle = $db->prepare("update konular set konu_baslik = :konu_baslik, konu_icerik = :konu_icerik, konu_sabit = :konu_sabit where konu_id = :konu_id");
       $konu_duzenle->execute(array(
         ":konu_baslik" => $konu_baslik,
         ":konu_icerik" => $konu_icerik,
         ":konu_sabit" => $konu_sabit,
         ":konu_id" => $konu_id
       ));

       if ($konu_duzenle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Konu düzenlendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Konu düzenlenemedi.";

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
