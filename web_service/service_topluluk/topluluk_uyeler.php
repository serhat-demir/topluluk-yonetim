<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];

       $topluluk_uyeler = $db->prepare("select kullanicilar.*, topluluk_uyeler.kullanici_yetki from topluluk_uyeler inner join kullanicilar on topluluk_uyeler.kullanici_id = kullanicilar.kullanici_id where topluluk_uyeler.topluluk_id = :topluluk_id order by topluluk_uyeler.kullanici_yetki desc, kullanicilar.kullanici_ad asc");
       $topluluk_uyeler->execute(array(
         ":topluluk_id" => $topluluk_id
       ));

       $response["uyeler"] = $topluluk_uyeler->fetchAll(PDO::FETCH_ASSOC);
       echo json_encode($response);
    } catch ( PDOException $e ){
         print $e->getMessage();
    }
  } else {
    $response["durum"] = 0;
    $response["mesaj"] = "Gerekli alanlar doldurulmamış.";

    echo json_encode($response);
  }
?>
