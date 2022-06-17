<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id']) && isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];
       $kullanici_id = $_POST['kullanici_id'];

       $yasak_kontrol = $db->prepare("select * from topluluk_yasakli_uyeler where topluluk_id = :topluluk_id and kullanici_id = :kullanici_id");
       $yasak_kontrol->execute(array(
         ":topluluk_id" => $topluluk_id,
         ":kullanici_id" => $kullanici_id
       ));

       if ($yasak_kontrol->rowCount() > 0) {
         $yasak_bilgi = $yasak_kontrol->fetchAll(PDO::FETCH_ASSOC)[0];

         $response["durum"] = 1;
         $response["mesaj"] = "Bu topluluktan " . substr($yasak_bilgi["yasaklanma_tarihi"], 0, 10) . " tarihinde yasaklandınız. \n\nSebep: " . $yasak_bilgi["yasaklanma_sebebi"];

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Kullanıcı bu toplulukta yasaklı değil.";

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
