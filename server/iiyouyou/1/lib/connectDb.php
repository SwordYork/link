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
	$hostname = SAE_MYSQL_HOST_M.':'.SAE_MYSQL_PORT;
	$dbuser = SAE_MYSQL_USER;
	$dbpass = SAE_MYSQL_PASS;
	$dbname = SAE_MYSQL_DB;
	$mysqli = new mysqli($hostname,$dbuser,$dbpass,$dbname);
	if($mysqli->connect_errno) {
		exit("Failed to connect to MySQL: " . $mysqli->connect_error);
	}
	$mysqli->query("SET NAMES UTF8");
	return $mysqli;
}

?>
