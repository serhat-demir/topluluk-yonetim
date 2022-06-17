<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];

       $topluluk_detay = $db->prepare("select *, (select count(*) from topluluk_uyeler where topluluk_id = topluluklar.topluluk_id) as topluluk_uye_sayisi from topluluklar where topluluk_id = :topluluk_id");
       $topluluk_detay->execute(array(
         ":topluluk_id" => $topluluk_id
       ));

       if ($topluluk_detay->rowCount() > 0) {
         $response = $topluluk_detay->fetchAll(PDO::FETCH_ASSOC)[0];

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Topluluk bulunamadı.";

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
