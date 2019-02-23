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
		'error' => 'DB Connection Error:' . $con->connect_error
	];
		
	print_r(json_encode($response));
	die("500:1:Connection failed: " . $con->connect_error);
}

$email = $_POST['email'];
$password_plain = $_POST['password'];

if(!isset($email) || !isset($password_plain)) {
	
	$response = [
		'success' => false,
		'error' => '400:1:Bad Input'
	];

	print_r(json_encode($response));
	die("400:1:Bad Input");
}

$stmt = mysqli_prepare($conn, "SELECT user_archive.* FROM user_archive WHERE email = ?");
mysqli_stmt_bind_param($stmt, 's', $email);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);

if(mysqli_stmt_num_rows($stmt) != 1) {
	
	$response = [
		'success' => false,
		'error' => '404:1:User not found'
	];

	print_r(json_encode($response));
	die("404:1:User not found");
}

mysqli_stmt_bind_result($user_id, $firstname, $lastname, $email, $password_hash, $account_verification_state, $email_vefification_state, $creation_date);


while(mysqli_stmt_fetch($stmt)) {
	
	$user = [
		'user_id' => $user_id,
		'firstname' => $firstname,
		'lastname' => $lastname,
		'email' => $email,
		'password_hash' => $password_hash,
		'account_verification_state' => $account_verification_state,
		'email_verification_state' => $email_verification_state,
		'creation_date' => $creation_date
	];
}

if(!password_verify($password_plain, $user['password_hash'])) {		
	
	$response = [
		'success' => false,
		'error' => '401:1:Bad Password'
	];

	print_r(json_encode($response));
	die("401:1:Bad Password");
}

$response = [
	'success' => true,
	'user' => $user
];

print_r(json_encode($response));

?>
