<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];

       $yasakli_uyeler = $db->prepare("select kullanicilar.*, topluluk_yasakli_uyeler.yasaklanma_sebebi, topluluk_yasakli_uyeler.yasaklanma_tarihi from topluluk_yasakli_uyeler inner join kullanicilar on topluluk_yasakli_uyeler.kullanici_id = kullanicilar.kullanici_id where topluluk_yasakli_uyeler.topluluk_id = :topluluk_id order by yasaklanma_tarihi desc");
       $yasakli_uyeler->execute(array(
         ":topluluk_id" => $topluluk_id
       ));

       $response["yasakli_uyeler"] = $yasakli_uyeler->fetchAll(PDO::FETCH_ASSOC);
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
