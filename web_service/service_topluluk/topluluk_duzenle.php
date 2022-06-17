<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['topluluk_ad']) && isset($_POST['topluluk_aciklama']) && isset($_POST['topluluk_gizlilik'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $topluluk_ad = $_POST['topluluk_ad'];
       $topluluk_aciklama = $_POST['topluluk_aciklama'];
       $topluluk_gizlilik = $_POST['topluluk_gizlilik'];

       $topluluk_duzenle = $db->prepare("update topluluklar set topluluk_ad = :topluluk_ad, topluluk_aciklama = :topluluk_aciklama, topluluk_gizlilik = :topluluk_gizlilik where topluluk_id = :topluluk_id");
       $topluluk_duzenle->execute(array(
         ":topluluk_ad" => $topluluk_ad,
         ":topluluk_aciklama" => $topluluk_aciklama,
         ":topluluk_gizlilik" => $topluluk_gizlilik,
         ":topluluk_id" => $topluluk_id
       ));

       if ($topluluk_duzenle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Topluluk bilgileri düzenlendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Topluluk bilgileri düzenlenemedi.";

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
