<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $kullanici_id = $_POST['kullanici_id'];

       $yasak_kaldir = $db->prepare("delete from topluluk_yasakli_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
       $yasak_kaldir->execute(array(
         ":topluluk_id" => $topluluk_id,
         ":kullanici_id" => $kullanici_id
       ));

       if ($yasak_kaldir->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Üye yasaklaması kaldırıldı.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Yasak kaldırılırken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
