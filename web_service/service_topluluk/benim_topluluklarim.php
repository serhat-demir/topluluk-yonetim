<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();
  $response["topluluklar"] = array();

  if (isset($_POST['kullanici_id'])) {
      try {
         $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

         $kullanici_id = $_POST['kullanici_id'];

         $benim_topluluklarim = $db->prepare("select * from topluluk_uyeler where kullanici_id = :kullanici_id");
         $benim_topluluklarim->execute(array(
           ":kullanici_id" => $kullanici_id
         ));

         if ($benim_topluluklarim->rowCount() > 0) {
           $topluluklar_id_arr = array(); //[1, 2, 3]
           $topluluklar_id_str = ""; //"1, 2, 3"

           foreach ($benim_topluluklarim->fetchAll(PDO::FETCH_ASSOC) as $topluluk) {
             array_push($topluluklar_id_arr, $topluluk["topluluk_id"]);
           }

           $topluluklar_id_str = implode(", ", $topluluklar_id_arr);

           $tum_topluluklar = $db->prepare("select *, (select count(*) from topluluk_uyeler where topluluk_id = topluluklar.topluluk_id) as topluluk_uye_sayisi from topluluklar where topluluk_id in(". $topluluklar_id_str .") order by topluluk_uye_sayisi desc");
           $tum_topluluklar->execute();

           $response["topluluklar"] = $tum_topluluklar->fetchAll(PDO::FETCH_ASSOC);

           echo json_encode($response);
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Herhangi bir topluluğa üye değilsiniz.";

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
