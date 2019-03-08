<?php 

require __DIR__ . '/config/env.php';

use Google\Cloud\Storage\StorageClient;

require __DIR__ . '/app_ini.php';

$servername = null;
$username = $app['mysql_user'];
$password = $app['mysql_password'];
$dbname = $app['mysql_dbname'];
$dbport = null;

$connectionname = $app['connection_name'];

$con = new mysqli($servername, $username, $password, $dbname, $dbport, '/cloudsql/' . $connectionname);

if($con->connect_error) {
	$response = [
		'success' => false,
		'error' => '500:1:Failed to connect to database'
	];

	print_r(json_encode($response));
	exit();
}

$email = $_POST['email'];

if(!isset($email)) {
	$response = [
		'success' => false,
		'error' => '400:1:Bad Input'
	];

	print_r(json_encode($response));
	exit();
}

$stmt = mysqli_prepare($con, "SELECT EXISTS(SELECT 1 FROM user_archive WHERE email = ? LIMIT 1)");
mysqli_stmt_bind_param($stmt, "s", $email);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);
mysqli_stmt_bind_result($stmt, $exists);
mysqli_stmt_fetch($stmt);

if($exists == 1) {

	$response = [
		'success' => false,
		'error' => '406:1:Email taken'
	];

	print_r(json_encode($response));
	
} else {

	$response = [
		'success' => true
	];
	
	print_r(json_encode($response));

}	

mysqli_stmt_close($stmt);
?>
