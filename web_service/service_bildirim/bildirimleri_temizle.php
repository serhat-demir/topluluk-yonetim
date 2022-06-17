<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $kullanici_id = $_POST['kullanici_id'];

       $bildirimleri_temizle = $db->prepare("delete from bildirimler where bildirim_alici = :kullanici_id");
       $bildirimleri_temizle->execute(array(
         ":kullanici_id" => $kullanici_id
       ));

       if ($bildirimleri_temizle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Bildirimler temizlendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Herhangi bir bildirim bulunmuyor.";

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
