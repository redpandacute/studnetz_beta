<?php
use Google\Cloud\Storage\StorageClient;

require __DIR__ . '/env.php';
require __DIR__ . '/app_ini.php';


$servername = null;
$username = $app['mysql_user'];
$password = $app['mysql_password'];
$dbname = $app['mysql_dbname'];
$dbport = null;

$connectionname = $app['connection_name'];

$con = new mysqli($servername, $username, $password, $dbname, $dbport, "/cloudsql/". $connectionname);

if($con->connect_error) {
	
	$response = [
		'success' => false,
		'error' => $con->connect_error . ':Failed to connect to DB'
	];
		
	print_r(json_encode($response));
	exit();
}

$email = $_POST['email'];
$password_plain = $_POST['password'];

if(!isset($email) || !isset($password_plain)) {
	
	$response = [
		'success' => false,
		'error' => '400:1:Bad Input'
	];

	print_r(json_encode($response));
	exit();
}


$stmt = mysqli_prepare($con, "SELECT user_archive.* FROM user_archive WHERE email = ? LIMIT 1");
mysqli_stmt_bind_param($stmt, 's', $email);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);

if(mysqli_stmt_num_rows($stmt) != 1) {
	$response = [
		'success' => false,
		'error' => '404:1:Bad user'
	];

	print_r(json_encode($response));
	exit();
}

mysqli_stmt_bind_result($stmt, $user_id, $uuid_bin, $uuid_text, $firstname, $lastname, $email, $password_hash, $account_verification_state, $email_vefification_state, $creation_date);


while($row = mysqli_stmt_fetch($stmt)) {
	
	$user = [
		'uuid_bin' => $uuid_bin,
		'uuid_text' => $uuid_text,
		'firstname' => $firstname,
		'lastname' => $lastname,
		'email' => $email,
		'account_verification_state' => $account_verification_state,
		'email_verification_state' => $email_verification_state,
		'creation_date' => $creation_date
	];
}

mysqli_stmt_close($stmt);

if(!password_verify($password_plain, $user['password_hash'])) {		
	
	$response = [
		'success' => false,
		'error' => '401:1:Bad user'
	];

	print_r(json_encode($response));
	exit();
}

$response = [
	'success' => true,
	'user' => $user
];

print_r(json_encode($response));

?>
