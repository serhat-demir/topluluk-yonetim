<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();
  $response["yorumlar"] = array();

  if (isset($_POST['konu_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $konu_id = $_POST['konu_id'];

       $yorumlar = $db->prepare("select yorumlar.*, kullanicilar.kullanici_ad as yorum_yazar_ad from yorumlar inner join kullanicilar on yorumlar.yorum_yazar = kullanicilar.kullanici_id where yorumlar.yorum_konu = :konu_id order by yorumlar.yorum_id");
       $yorumlar->execute(array(
         ":konu_id" => $konu_id
       ));

       if ($yorumlar->rowCount() > 0) {
         foreach ($yorumlar->fetchAll(PDO::FETCH_ASSOC) as $yorum) {
           $yorum_yazar_yetki = $db->prepare("select kullanici_yetki from topluluk_uyeler where topluluk_id = (select konu_topluluk from konular where konu_id = :konu_id) and kullanici_id = :kullanici_id");
           $yorum_yazar_yetki->execute(array(
             ":konu_id" => $yorum["yorum_konu"],
             ":kullanici_id" => $yorum["yorum_yazar"]
           ));

           if ($yorum_yazar_yetki->rowCount() > 0) {
             $yazar_yetki = $yorum_yazar_yetki->fetchAll(PDO::FETCH_ASSOC)[0];
           } else {
             $yazar_yetki["kullanici_yetki"] = intval(YETKI_YOK);
           }

           $yorum["yorum_yazar_yetki"] = $yazar_yetki["kullanici_yetki"];
           array_push($response["yorumlar"], $yorum);
         }

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Herhangi bir yorum bulunmuyor.";

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
