<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];

       $topluluk_sil = $db->prepare("delete from topluluklar where topluluk_id = :topluluk_id");
       $topluluk_sil->execute(array(
         ":topluluk_id" => $topluluk_id
       ));

       if ($topluluk_sil->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Topluluk silindi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Topluluk silinirken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
