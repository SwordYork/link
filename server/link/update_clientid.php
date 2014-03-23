<?php
require_once "../lib/connectDb.php";

$user_id = $_POST['user_id']; 
$client_id = $_POST['client_id'];

//$userid = $_SESSION['userid'];

$db = connectDb();

$query =  sprintf("INSERT INTO link_clientid (user_id,client_id) VALUES ('%s','%s') ON DUPLICATE KEY UPDATE client_id='%s'",$user_id,$client_id,$client_id);

if (!($db->query($query))){
	$response = array('update_clientid'=>'sqlerror');
}
else{
	$response = array('update_clientid'=>TRUE);
}

$db->close();
echo json_encode($response);

?>