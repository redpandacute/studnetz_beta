<?php

require __DIR__ . '/config/env.php';

use Google\Cloud\Storage\StorageClient;

require __DIR__ . '/app_ini.php';

$ACTIVE_STATE_VALUE = 1;

$servername = null;
$username = $app['mysql_user'];
$password = $app['mysql_password'];
$dbname = $app['mysql_dbname'];
$dbport = null;

$connectionname = $app['connection_name'];

//Connection to DB
$con = new mysqli($servername, $username, $password, $dbname, $dbport, "/cloudsql/". $connectionname);

if($con->connect_error) {

	$response = [
		'success' => false,
		'error' => '500:1:Failed to connect to database' 
	];

	print_r(json_encode($response));

	exit();
}

$email = $_POST["email"];
$firstname = $_POST["firstname"];
$lastname = $_POST["lastname"];
$password_plain = $_POST["password"];


//INPUT VALIDATION --------------------------------------
$patternspaced = '/^[a-zA-Z0-9 \s]+$/';
$pattern = '/^[a-zA-Z0-9\s]+$/';
$patternemail = '/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]+$/';

$valid = true;
$errorstring = "";


if(empty($firstname) || !preg_match($patternspaced, $firstname) || strlen($firstname) > 255) {
	$valid = false;
	$errorstring .= '406:1:bad firstname;';
}

if(empty($lastname) || !preg_match($patternspaced, $lastname) || strlen($lastname) > 255) {
        $valid = false;
        $errorstring .= '406:2:bad lastname;';
}


if(empty($email) || !preg_match($patternemail, $email) || strlen($email) > 255) {
        $valid = false;
        $errorstring .= '406:3:bad email;';
}

if(empty($password_plain) || !preg_match($patternspaced, $password_plain) || strlen($password_plain) > 255 || strlen($password_plain) < 8) {
        $valid = false;
        $errorstring .= '406:5:bad password;';
}

//CHECK AVAILIBILITY OF EMAIL
$stmt = mysqli_prepare($con, "SELECT EXISTS(SELECT 1 FROM user_archive WHERE email = ? LIMIT 1)");
mysqli_stmt_bind_param($stmt, "s", $email);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);
mysqli_stmt_bind_result($stmt, $exists);
mysqli_stmt_fetch($stmt);

if($exists == 1) { 

	$valid = false;

	$errorstring .= '406:4:Email taken';
}

mysqli_stmt_close($stmt);

if(!$valid) {

	$response = [
		'success' => false,
		'error' => explode(";",$errorstring)
	];

	print_r(json_encode($response));

	exit();
}
//-----------------------------------------------------
//
//HASHING OF PASSWORD
$options = [
	'cost' => 12
];

$password_hash = password_hash($password_plain, PASSWORD_BCRYPT, $options);

//VERIFICATION ----------------------------------------

$school_verification_state = 0;

$stmt = mysqli_prepare($con, "SELECT EXISTS(SELECT 1 FROM schoolverification_archive WHERE schoolverification_archive.email = ?");
mysqli_stmt_bind_param($stmt, "s", $email);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);
mysqli_stmt_bind_result($stmt, $exists);
mysqli_stmt_fetch($stmt);
mysqli_stmt_close($stmt);

if($exists == 1) {

	mysqli_prepare($con, "SELECT * FROM schoolverification_archive WHERE email = ?");
	mysqli_stmt_bind_param($stmt, 's', $email);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_store_result($stmt);

	mysqli_stmt_bind_result($stmt, $verf_school_id, $verification_state, $verf_email, $verf_firstname, $verf_lastname, $verf_grade, $active_state, $instertion_date, $expiration_date);

	while($row = mysqli_stmt_fetch($stmt)) {
		$firstname = $verf_firstname;
		$lastname = $verf_lastname;
		$school_verification_state = $verification_state;
		$grade = $verf_grade;
		$school_id = $verf_school_id;
	}

	mysqli_stmt_close($stmt);

	$stmt = mysqli_prepare($con, "UPDATE schoolverification_archive SET active_state = ?, insertion_date = CURDATE()");
	mysqli_stmt_bind_param($stmt, "i", $ACTIVE_STATE_VALUE);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);

	//INSERTION FOR VERIFIED USER --------------------------

	$stmt = mysqli_prepare($con, "INSERT INTO user_archive(uuid_bin, firstname, lastname, email, password_hash, account_verification_state, creation_date) VALUES (UNHEX(REPLACE(UUID(),'-','')),?,?,?,?,?, CURDATE())");
	mysqli_stmt_bind_param($stmt, "ssssi", $firstname, $lastname, $email, $password_hash, $school_verification_state);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);

	$stmt = mysqli_prepare($con, "SELECT user_archive.user_id WHERE user_archive.email = ?");
	mysqli_stmt_bind_param($stmt, "s", $email);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_store_result($stmt);
	mysqli_stmt_bind_result($stmt, $user_id_fetch);

	while($row = mysqli_stmt_fetch($stmt)) {
		$user_id = $user_id_fetch;
	}

	$mysqli_stmt_close($stmt);

	$stmt = mysqli_prepare($con, "INSERT INTO school_conn(user_id, school_id, grade) VALUES (?,?,?)");
	mysqli_stmt_bind_param($stmt, "iii", $user_id, $school_id, $grade);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);

} else {

	//INSERTIONS FOR UNVERIFIED USERS ----------------------
	
	$stmt = mysqli_prepare($con, "INSERT INTO user_archive(uuid_bin, firstname, lastname, email, password_hash, creation_date) VALUES (UNHEX(REPLACE(UUID(),'-','')),?,?,?,?, CURDATE())");
	mysqli_stmt_bind_param($stmt, "ssss", $firstname, $lastname, $email, $password_hash);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);
	
}

//TODO SEND EMAIL FOR EMAIL VERIFICATION


$response = [
	'success' => true
];

print_r(json_encode($response));

?>
