<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['kayit_icerik'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $kayit_icerik = $_POST['kayit_icerik'];
       $tarih = date('Y-m-d H:i:s', strtotime('+1 hours'));

       $kayit_ekle = $db->prepare("insert into topluluk_kayitlari (kayit_topluluk, kayit_icerik, kayit_tarih) values(:kayit_topluluk, :kayit_icerik, :kayit_tarih)");
       $kayit_ekle->execute(array(
         ":kayit_topluluk" => $topluluk_id,
         ":kayit_icerik" => $kayit_icerik,
         ":kayit_tarih" => $tarih
       ));

       if ($kayit_ekle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Kayıt eklendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Kayıt eklenirken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
