<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['konu_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $konu_id = $_POST['konu_id'];

       $konu_sil = $db->prepare("delete from konular where konu_id = :konu_id");
       $konu_sil->execute(array(
         ":konu_id" => $konu_id
       ));

       if ($konu_sil->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Konu silindi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Konu silinirken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
