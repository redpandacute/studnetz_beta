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
		'error' => $con->connect_error . ':Failed to connect to DB'
	];
		
	print_r(json_encode($response));
	exit();
}

$token = $_POST['token'];


foreach($alters as $out) {
	print_r('//'.$out.'//');
}

try {
	$decoded_key = base64_decode($key);
	$payload = json_decode(json_encode(get_object_vars(JWT::decode($token, $decoded_key, array('HS512')))), true);
	
} catch (Exception $e) {

	$response = [
		'success' => false,
		'error' => '401:1:Invalid Token'
	];
	
	print_r(json_encode($response));
	exit();
}

$data = $payload['data'];

$email = $data['email'];
$uuid = $data['uuid_text'];

$stmt = mysqli_prepare($con, "SELECT user_id FROM user_archive WHERE uuid_bin = UNHEX(REPLACE(?,'-','')) LIMIT 1");
mysqli_stmt_bind_param($stmt, 's', $uuid);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);
mysqli_stmt_bind_result($stmt, $uid);
mysqli_stmt_fetch($stmt);
mysqli_stmt_close($stmt);

if(!isset($uid)) {

	$response = [
		'success' => false,
		'error' => '401:1:Invalid user'
	];

	print_r(json_encode($response));
	exit();
}


if(isset($_POST['subjects'])) { 
	$subjects = json_decode($_POST['subjects'], true);
       	$rmv = $subjects['rmv'];
	$add = $subjects['add'];

	foreach($rmv as $uuid) {
		$stmt = mysqli_prepare($con, "DELETE subject_conn.* FROM subject_conn INNER JOIN subject_list ON subject_conn.subject_id = subject_list.subject_id WHERE user_id = ? AND subject_list.uuid_bin = UNHEX(REPLACE(?,'-',''))");
		mysqli_stmt_bind_param($stmt, 'is', $uid, $uuid);
		mysqli_stmt_execute($stmt);
		mysqli_stmt_close($stmt);
	}

	foreach($add as $uuid) {
		$stmt = mysqli_prepare($con, "INSERT INTO subject_conn (user_id, subject_id) SELECT ?, subject_id FROM subject_list WHERE uuid_bin = UNHEX(REPLACE(?,'-',''))");
		mysqli_stmt_bind_param($stmt, 'is', $uid, $uuid);
		mysqli_stmt_execute($stmt);
		mysqli_stmt_close($stmt);
	}
}

if(isset($_POST['profile'])) {
	$profile = json_decode($_POST['profile'], true);
	$stmt = mysqli_prepare($con,"UPDATE user_archive SET firstname = ?, lastname = ? WHERE user_id = ?");
	mysqli_stmt_bind_param($stmt, 'ssi', $profile['firstname'], $profile['lastname'], $uid);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);

	$stmt = mysqli_prepare($con,"UPDATE user_profile SET description = ? WHERE user_id = ?");
	mysqli_stmt_bind_param($stmt, 'si', $profile['description'],  $uid);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);
}

	
if(isset($_POST['school'])) {
	$school = json_decode($_POST['school'], true);
	$stmt = mysqli_prepare($con, "UPDATE school_conn SET school_id = (SELECT school_id FROM school_list WHERE uuid_bin = UNHEX(REPLACE(?,'-',''), grade = ? WHERE user_id = ?");
	mysqli_stmt_bind_param($stmt, 'is', $school['uuid'], $school['grade'], $uid);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);
}

$response = [
	'success' => true
];

print_r(json_encode($response));
?>
