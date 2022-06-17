<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['yorum_id']) && isset($_POST['yorum_icerik'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $yorum_id = $_POST['yorum_id'];
       $yorum_icerik = $_POST['yorum_icerik'];

       $yorum_duzenle = $db->prepare("update yorumlar set yorum_icerik = :yorum_icerik where yorum_id = :yorum_id");
       $yorum_duzenle->execute(array(
         ":yorum_icerik" => $yorum_icerik,
         ":yorum_id" => $yorum_id
       ));

       if ($yorum_duzenle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Yorum düzenlendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Yorum düzenlenemedi.";

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
