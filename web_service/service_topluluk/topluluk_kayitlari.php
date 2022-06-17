<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();

  if (isset($_POST['topluluk_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $topluluk_id = $_POST['topluluk_id'];

       $topluluk_kayitlari = $db->prepare("select * from topluluk_kayitlari where kayit_topluluk = :kayit_topluluk order by kayit_id desc");
       $topluluk_kayitlari->execute(array(
         ":kayit_topluluk" => $topluluk_id
       ));

       if ($topluluk_kayitlari->rowCount() > 0) {
         $response["kayitlar"] = $topluluk_kayitlari->fetchAll(PDO::FETCH_ASSOC);

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Toplulukta kayıt bulunmuyor.";

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
