<?php
/*
if(session_id() == '') {
    // session isn't started
    if(isset($_REQUEST['sessionid'])){
	session_id($_REQUEST['sessionid']); 
    }
    SESSION_START();
    
    //neither login nor no session, then destroy
    if(!isset($_SESSION['userid']) && !isset($_POST['password'])){
	echo htmlspecialchars(json_encode(array("session"=>"nologin")),ENT_NOQUOTES);
	SESSION_DESTROY();
	exit();
    }
}

*/

// connect to database
function connectDb(){
	$mysqli = new mysqli("localhost","link","*******","link");
	if($mysqli->connect_errno) {
		exit("Failed to connect to MySQL: " . $mysqli->connect_error);
	}
	$mysqli->query("SET NAMES UTF8");
	return $mysqli;
}

?>
