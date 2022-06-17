<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $kullanici_id = $_POST['kullanici_id'];

       $bildirimleri_okundu_isaretle = $db->prepare("update bildirimler set bildirim_okundu = :bildirim_okundu where bildirim_alici = :kullanici_id");
       $bildirimleri_okundu_isaretle->execute(array(
         ":bildirim_okundu" => BILDIRIM_OKUNDU,
         ":kullanici_id" => $kullanici_id
       ));

       if ($bildirimleri_okundu_isaretle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Bildirimler okundu olarak işaretlendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Bütün bildirimler okunmuş.";

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
