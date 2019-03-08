<?php
use Google\Cloud\Storage\StorageClient;


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

$uuid = $_GET['uuid_text'];

//returning array

$profile = array();

//getting basic user ----------------------------------------------

$stmt = mysqli_prepare($con, "SELECT * FROM user_archive INNER JOIN profile_archive ON user_archive.user_id = profile_archive.user_id WHERE user_archive.uuid_bin = UNHEX(REPLACE(?,'-','')) LIMIT 1");
mysqli_stmt_bind_param($stmt, 's', $uuid);
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

mysqli_stmt_bind_result($stmt, $user_id, $uuid_bin, $uuid_text, $firstname, $lastname, $email, $result_password_hash, $account_verification_state, $email_vefification_state, $creation_date, $lastactive, $uid, $profilepicture_id, $calendar_id, $description);
mysqli_stmt_fetch($stmt);

$user = [
	'uuid_text' => $uuid,
	'firstname' => $firstname,
	'lastname' => $lastname,
	'account_verification_state' => $account_verification_state,
	'lastactive' => $lastactive
];

//TODO calendar and profilepicture

$ext_profile = [
	'description' => $description
];
	

$uid = $user_id;

$profile['ext_profile'] = $ext_profile;
$profile['user'] = $user;

mysqli_stmt_close($stmt);

//getting school -------------------------------------------

$stmt = mysqli_prepare($con, "SELECT school_conn.grade, school_list.uuid_text, school_list.schoolname, school_list.school_abrv, schooltype_list.schooltype_name, schooltype_list.schooltype_abrv FROM school_conn INNER JOIN school_list ON school_conn.school_id = school_list.school_id INNER JOIN schooltype_list ON school_list.schooltype_id = schooltype_list.schooltype_id WHERE school_conn.user_id = ?");

mysqli_stmt_bind_param($stmt, 'i', $uid);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);

if(mysqli_stmt_num_rows($stmt) == 1) {
	mysqli_stmt_bind_result($stmt, $grade, $school_uuid_text, $schoolname, $school_abrv, $schooltype_name, $schooltype_abrv);

	while($row = mysqli_stmt_fetch($stmt)) {

		$school = [ 
			'school_uuid_text' => $school_uuid_text,
			'schoolname' => $schoolname,
			'school_abrv' => $school_abrv,
			'schooltype_name' => $schooltype_name,
			'schooltype_abrv' => $schooltype_abrv,
			'grade' => $grade
		];
	}

	$profile['school'] = $school;
}

mysqli_stmt_close($stmt);

//getting description and subjects ----------------------------

$stmt = mysqli_prepare($con, "SELECT subject_list.uuid_text, subject_list.subjectname, HEX(subject_list.color) FROM subject_conn INNER JOIN subject_list ON subject_conn.subject_id = subject_list.subject_id WHERE subject_conn.user_id = ?");

mysqli_stmt_bind_param($stmt, 'i', $uid);
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);
mysqli_stmt_bind_result($stmt, $subject_uuid_text, $subjectname, $subjectcolor);

$subjects = array();

while($row = mysqli_stmt_fetch($stmt)) {

	$subject = [
		'subject_uuid_text' => $subject_uuid_text,
		'subjectname' => $subjectname,
		'color' => $subjectcolor
	];

	$subjects[] = $subject;
}

$profile['subjects'] = $subjects;

mysqli_stmt_close($stmt);

$response = [
	'success' => true,
	'profile' => $profile
];

print_r(json_encode($response));
?>
