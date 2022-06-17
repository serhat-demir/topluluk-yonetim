<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $kullanici_id = $_POST['kullanici_id'];

       $uyelik_kontrol = $db->prepare("select * from topluluk_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
       $uyelik_kontrol->execute(array(
         ":topluluk_id" => $topluluk_id,
         ":kullanici_id" => $kullanici_id
       ));

       if ($uyelik_kontrol->rowCount() > 0) {
         $response["durum"] = 0;
         $response["mesaj"] = "Bu topluluğa üyesiniz.";

         echo json_encode($response);
       } else {
         $topluluk_katil = $db->prepare("insert into topluluk_uyeler (topluluk_id, kullanici_id) values(:topluluk_id, :kullanici_id)");
         $topluluk_katil->execute(array(
           ":topluluk_id" => $topluluk_id,
           ":kullanici_id" => $kullanici_id
         ));

         if ($topluluk_katil->rowCount() > 0) {
           $response["durum"] = 1;
           $response["mesaj"] = "Topluluğa üye oldunuz.";

           echo json_encode($response);
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Topluluğa üye olurken bir sorun oluştu. Daha sonra tekrar deneyin.";

           echo json_encode($response);
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
