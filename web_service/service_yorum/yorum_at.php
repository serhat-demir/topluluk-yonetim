<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['yorum_icerik']) && isset($_POST['yorum_yazar']) && isset($_POST['yorum_konu'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $yorum_icerik = $_POST['yorum_icerik'];
       $yorum_yazar = $_POST['yorum_yazar'];
       $yorum_konu = $_POST['yorum_konu'];
       $tarih = date('Y-m-d H:i:s', strtotime('+1 hours'));

       $yorum_at = $db->prepare("insert into yorumlar (yorum_icerik, yorum_yazar, yorum_konu, yorum_tarih) values(:yorum_icerik, :yorum_yazar, :yorum_konu, :yorum_tarih)");
       $yorum_at->execute(array(
         ":yorum_icerik" => $yorum_icerik,
         ":yorum_yazar" => $yorum_yazar,
         ":yorum_konu" => $yorum_konu,
         ":yorum_tarih" => $tarih
       ));

       if ($yorum_at->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Yorum gönderildi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Yorum gönderilirken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
