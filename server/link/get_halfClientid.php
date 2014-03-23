<?php
require_once "../lib/connectDb.php";

$user_id = $_POST['user_id']; 

//$userid = $_SESSION['userid'];

$db = connectDb();

$query =  sprintf("SELECT client_id FROM link_clientid WHERE user_id='%s'",$user_id);

$result = $db->query($query);


if (!$result){
	$response = array('get_clientid'=>'sqlerror');
}
else{
	$row = $result->fetch_assoc();
	$response = array('get_clientid'=>TRUE,'half_clientid'=>$row['client_id']);
}

$db->close();
echo json_encode($response);

?> 
