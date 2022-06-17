<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['bildirim_alici']) && isset($_POST['bildirim_icerik'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $bildirim_alici = $_POST['bildirim_alici'];
       $bildirim_icerik = $_POST['bildirim_icerik'];

       $bildirim_gonder = $db->prepare("insert into bildirimler (bildirim_alici, bildirim_icerik) values(:bildirim_alici, :bildirim_icerik)");
       $bildirim_gonder->execute(array(
         ":bildirim_alici" => $bildirim_alici,
         ":bildirim_icerik" => $bildirim_icerik
       ));

       if ($bildirim_gonder->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Bildirim gönderildi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Bildirim gönderilirken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
