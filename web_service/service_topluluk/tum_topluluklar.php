<?php
  require_once dirname(__DIR__) . '/db_config.php';
  require_once dirname(__DIR__) . '/utils.php';

  $response = array();
  $response["topluluklar"] = array();

  try {
     $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

     $tum_topluluklar = $db->prepare("select *, (select count(*) from topluluk_uyeler where topluluk_id = topluluklar.topluluk_id) as topluluk_uye_sayisi from topluluklar where topluluk_gizlilik != :topluluk_gizli order by topluluk_uye_sayisi desc");
     $tum_topluluklar->execute(array(
       ":topluluk_gizli" => TOPLULUK_GIZLI
     ));

     if ($tum_topluluklar->rowCount() > 0) {
       $response["topluluklar"] = $tum_topluluklar->fetchAll(PDO::FETCH_ASSOC);

       echo json_encode($response);
     } else {
       $response["durum"] = 0;
       $response["mesaj"] = "Herhangi bir topluluk bulunmuyor.";

       echo json_encode($response);
     }
  } catch ( PDOException $e ){
       print $e->getMessage();
  }
?>
