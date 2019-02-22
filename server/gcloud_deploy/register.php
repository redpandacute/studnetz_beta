<?php

require __DIR__ . '/env.php';

use Google\Cloud\Storage\StorageClient;

require __DIR__ . '/app_ini.php';

$servername = null;
$username = $app['mysql_user'];
$password = $app['mysql_password'];
$dbname = $app['mysql_dbname'];
$dbport = null;

$connectionname = "deep-contact-232012:europe-west1:studnetz-beta-1-db";

//Connection to DB
$con = new mysqli($servername, $username, $password, $dbname, $dbport, "/cloudsql/". $connectionname);

if($con->connect_error) {
	
	$response[
		'success' => false,
		'error' => 'DB Connection Error:' . $con->connect_error
	];
		
	print_r(json_encode($response));

	die("Connection failed: " . $con->connect_error);
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
$stmt = mysqli_prepare($con, "SELECT user_archive.* FROM user_archive WHERE user_archive.email = ?");
mysqli_stmt_bind_param($stmt, "s", $email);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);

if(mysqli_stmt_num_rows($stmt) > 0) {
	$valid = false;

	$errorstring .= '406:4:taken email';
}

mysqli_stmt_close($stmt);

if(!$valid) {
			
	$response[
		'success' => false,
		'error' => explode(";",$errorstring)
	];
		
	print_r(json_encode($response));

	die("An error ocurred with the userinput: " . $errorstring);
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

$stmt = mysqli_prepare($con, "SELECT schooverification_archive.* WHERE schoolverification_archive.email = ?");
mysqli_stmt_bind_param($stmt, "s", $email);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);

if(mysqli_stmt_num_row($stmt) > 0) {

	mysqli_stmt_bind_result($stmt, $verf_school_id, $verification_state, $verf_firstname, $verf_lastname, $verf_grade, $active);
	
	while($row = mysqli_stmt_fetch($stmt)) {
		$firstname = $verf_firstname;
		$lastname = $verf_lastname;
		$school_verification_state = $verification_state;
		$grade = $verf_grade;
		$school_id = $verf_school_id;
	}

	mysqli_stmt_close($stmt);

	//INSERTION FOR VERIFIED USER --------------------------
	
	$stmt = mysqli_prepare($con, "INSERT INTO user_archive(firstname, lastname, email, password, verification_state) VALUES (?,?,?,?,?)");
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

	$stmt = mysqli_prepare($con, "INSERT INTO school_archive(user_id, school_id, grade) VALUES (?,?,?)");
	mysqli_stmt_bind_param($stmt, "iii", $user_id, $school_id, $grade);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);

} else {

	//INSERTIONS FOR UNVERIFIED USERS ----------------------

	$stmt = mysqli_prepare($con, "INSERT INTO user_archive(firstname, lastname, email, password, verification_state) VALUES (?,?,?,?,?)");
	mysqli_stmt_bind_param($stmt, "ssssi", $firstname, $lastname, $email, $password_hash, 0);
	mysqli_stmt_execute($stmt);
	mysqli_stmt_close($stmt);
}

//TODO SEND EMAIL FOR EMAIL VERIFICATION


$response[
	'success' => true
];

print_r(json_encode($response));
?>
