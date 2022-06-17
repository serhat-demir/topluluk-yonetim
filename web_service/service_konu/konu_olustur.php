<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['konu_baslik']) && isset($_POST['konu_icerik']) && isset($_POST['konu_yazar']) && isset($_POST['konu_topluluk'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $konu_baslik = $_POST['konu_baslik'];
       $konu_icerik = $_POST['konu_icerik'];
       $konu_yazar = $_POST['konu_yazar'];
       $konu_topluluk = $_POST['konu_topluluk'];
       $tarih = date('Y-m-d H:i:s', strtotime('+1 hours'));

       $konu_olustur = $db->prepare("insert into konular (konu_baslik, konu_icerik, konu_yazar, konu_topluluk, konu_tarih) values(:konu_baslik, :konu_icerik, :konu_yazar, :konu_topluluk, :konu_tarih)");
       $konu_olustur->execute(array(
         ":konu_baslik" => $konu_baslik,
         ":konu_icerik" => $konu_icerik,
         ":konu_yazar" => $konu_yazar,
         ":konu_topluluk" => $konu_topluluk,
         ":konu_tarih" => $tarih
       ));

       if ($konu_olustur->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Konu oluşturuldu.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Konu oluşturulurken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
