<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['kullanici_id']) && isset($_POST['yasaklanma_sebebi'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $kullanici_id = $_POST['kullanici_id'];
       $yasaklanma_sebebi = $_POST['yasaklanma_sebebi'];
       $tarih = date('Y-m-d H:i:s', strtotime('+1 hours'));

       $topluluk_ayril = $db->prepare("delete from topluluk_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
       $topluluk_ayril->execute(array(
         ":topluluk_id" => $topluluk_id,
         ":kullanici_id" => $kullanici_id
       ));

       if ($topluluk_ayril->rowCount() > 0) {
         $uye_yasakla = $db->prepare("insert into topluluk_yasakli_uyeler (topluluk_id, kullanici_id, yasaklanma_sebebi, yasaklanma_tarihi) values(:topluluk_id, :kullanici_id, :yasaklanma_sebebi, :yasaklanma_tarihi)");
         $uye_yasakla->execute(array(
           ":topluluk_id" => $topluluk_id,
           ":kullanici_id" => $kullanici_id,
           ":yasaklanma_sebebi" => $yasaklanma_sebebi,
           ":yasaklanma_tarihi" => $tarih
         ));

         if ($uye_yasakla->rowCount() > 0) {
           $response["durum"] = 1;
           $response["mesaj"] = "Üye yasaklandı.";

           echo json_encode($response);
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Üye yasaklanırken bir sorun oluştu. Daha sonra tekrar deneyin.";

           echo json_encode($response);
         }
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Üye yasaklanırken bir sorun oluştu. Daha sonra tekrar deneyin.";

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
