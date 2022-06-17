<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $kullanici_id = $_POST['kullanici_id'];

       $yoneticilik_kontrol = $db->prepare("select * from topluluk_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id and kullanici_yetki = :kullanici_yetki");
       $yoneticilik_kontrol->execute(array(
         ":topluluk_id" => $topluluk_id,
         ":kullanici_id" => $kullanici_id,
         ":kullanici_yetki" => YETKI_YONETICI
       ));

       if ($yoneticilik_kontrol->rowCount() > 0) {
         $response["durum"] = 0;
         $response["mesaj"] = "Kendi topluluğunuzdan ayrılamazsınız.";

         echo json_encode($response);
       } else {
         $uyelik_kontrol = $db->prepare("select * from topluluk_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
         $uyelik_kontrol->execute(array(
           ":topluluk_id" => $topluluk_id,
           ":kullanici_id" => $kullanici_id
         ));

         if ($uyelik_kontrol->rowCount() == 0) {
           $response["durum"] = 0;
           $response["mesaj"] = "Bu topluluğa üye değilsiniz.";

           echo json_encode($response);
         } else {
           $topluluk_ayril = $db->prepare("delete from topluluk_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
           $topluluk_ayril->execute(array(
             ":topluluk_id" => $topluluk_id,
             ":kullanici_id" => $kullanici_id
           ));

           if ($topluluk_ayril->rowCount() > 0) {
             $response["durum"] = 1;
             $response["mesaj"] = "Topluluktan ayrıldınız.";

             echo json_encode($response);
           } else {
             $response["durum"] = 0;
             $response["mesaj"] = "Topluluktan ayrılırken bir sorun oluştu. Daha sonra tekrar deneyin.";

             echo json_encode($response);
           }
         }
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
