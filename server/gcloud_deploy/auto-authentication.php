<?php

require __DIR__ . '/config/env.php';
require __DIR__ . '/config/jwt_key.php';
require __DIR__ . '/inc/jwt_helper.php';
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
		'error' => '500:1:Failed to connect to database'	
	];
		
	print_r(json_encode($response));
	exit();
}

$token = $_POST['token'];

try {

	$decoded_key = base64_decode($key);
	$payload = json_decode(json_encode(get_object_vars(JWT::decode($token, $decoded_key, array('HS512')))), true); //dont fkn ask me how this works but it does

} catch (Exception $e) {

	$response = [
		'success' => false,
		'error' => '401:1:Invalid Token'
	];
	
	print_r(json_encode($response));
	exit();
}
$data  = $payload['data'];

$email = $data['email'];
$uuid = $data['uuid_text'];

$stmt = mysqli_prepare($con, "SELECT user_archive.* FROM user_archive WHERE email = ? AND uuid_bin = UNHEX(REPLACE(?,'-','')) LIMIT 1");

mysqli_stmt_bind_param($stmt, 'ss', $email, $uuid);
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

mysqli_stmt_bind_result($stmt, $user_id, $uuid_bin, $uuid_text, $firstname, $lastname, $email, $result_password_hash, $account_verification_state, $email_vefification_state, $creation_date, $lastactive);


while($row = mysqli_stmt_fetch($stmt)) {
	
	$user = [
		'uuid_text' => $uuid_text,
		'firstname' => $firstname,
		'lastname' => $lastname,
		'email' => $email,
		'account_verification_state' => $account_verification_state,
		'email_verification_state' => $email_verification_state,
	];
}

mysqli_stmt_close($stmt);

//update lastactive

$stmt = mysqli_prepare($con, "UPDATE user_archive SET lastactive = NOW()");
mysqli_stmt_execute($stmt);
mysqli_stmt_close($stmt);

$response = [
	'success' => true,
	'user' => $user
];

print_r(json_encode($response));

?>
