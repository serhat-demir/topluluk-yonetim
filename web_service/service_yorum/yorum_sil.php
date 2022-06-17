<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['yorum_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $yorum_id = $_POST['yorum_id'];

       $yorum_sil = $db->prepare("delete from yorumlar where yorum_id = :yorum_id");
       $yorum_sil->execute(array(
         ":yorum_id" => $yorum_id
       ));

       if ($yorum_sil->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Yorum silindi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Yorum silinirken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
