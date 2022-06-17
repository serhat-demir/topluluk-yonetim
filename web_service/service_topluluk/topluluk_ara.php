<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();
  $response["topluluklar"] = array();

  if (isset($_POST['aranan_baslik']) && isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $aranan_baslik = $_POST['aranan_baslik'];
       $kullanici_id = $_POST['kullanici_id'];

       if ($kullanici_id == 0) { //tüm topluluklar arasında ara
         $tum_topluluklar = $db->prepare("select *, (select count(*) from topluluk_uyeler where topluluk_id = topluluklar.topluluk_id) as topluluk_uye_sayisi from topluluklar where topluluk_gizlilik != :topluluk_gizli and topluluk_ad like '%". $aranan_baslik ."%' order by topluluk_uye_sayisi desc");
         $tum_topluluklar->execute(array(
           ":topluluk_gizli" => TOPLULUK_GIZLI
         ));

         if ($tum_topluluklar->rowCount() > 0) {
           $response["topluluklar"] = $tum_topluluklar->fetchAll(PDO::FETCH_ASSOC);

           echo json_encode($response);
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Aranan isimle eşleşen topluluk bulunamadı.";

           echo json_encode($response);
         }
       } else { //üye olunan topluluklar arasında ara
         $uye_olunan_topluluklar = $db->prepare("select * from topluluk_uyeler where kullanici_id = :kullanici_id");
         $uye_olunan_topluluklar->execute(array(
           ":kullanici_id" => $kullanici_id
         ));

         if ($uye_olunan_topluluklar->rowCount() > 0) {
           $topluluklar_id_arr = array(); //[1, 2, 3]
           $topluluklar_id_str = ""; //"1, 2, 3"

           foreach ($uye_olunan_topluluklar->fetchAll(PDO::FETCH_ASSOC) as $uye_olunan_topluluk) {
             array_push($topluluklar_id_arr, $uye_olunan_topluluk["topluluk_id"]);
           }

           $topluluklar_id_str = implode(", ", $topluluklar_id_arr);

           $tum_topluluklar = $db->prepare("select *, (select count(*) from topluluk_uyeler where topluluk_id = topluluklar.topluluk_id) as topluluk_uye_sayisi from topluluklar where topluluk_id in(". $topluluklar_id_str .") and topluluk_ad like '%". $aranan_baslik ."%' order by topluluk_uye_sayisi desc");
           $tum_topluluklar->execute();

           if ($tum_topluluklar->rowCount() > 0) {
             $response["topluluklar"] = $tum_topluluklar->fetchAll(PDO::FETCH_ASSOC);

             echo json_encode($response);
           } else {
             $response["durum"] = 0;
             $response["mesaj"] = "Aranan isimle eşleşen topluluk bulunamadı.";

             echo json_encode($response);
           }
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Herhangi bir topluluğa üye değilsiniz.";

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
