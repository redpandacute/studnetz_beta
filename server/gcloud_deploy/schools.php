<?php
use Google\Cloud\Storage\StorageClient;

require __DIR__ . '/config/env.php';
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

$stmt = mysqli_prepare($con, "SELECT school_list.uuid_text, school_list.schoolname, school_list.schoolgrades, schooltype_list.uuid_text, schooltype_list.schooltype_name, schooltype_list.schooltype_abrv FROM school_list INNER JOIN schooltype_list ON school_list.schooltype_id = schooltype_list.schooltype_id");
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);
mysqli_stmt_bind_result($stmt, $school_uuid, $school_name, $school_grades, $schooltype_uuid, $schooltype_name, $schooltype_abrv);

$school = array();

while($row = mysqli_stmt_fetch($stmt)) {

    $school = [
        'school_uuid' => $school_uuid,
	'school_name' => $school_name,
	'school_grades' => $school_grades,
        'schooltype_uuid' => $schooltype_uuid,
        'schooltype_name' => $schooltype_name,
        'schooltype_abrv' => $schooltype_abrv
    ];

    $schools[] = $school;
}

$response = [
    'success' => true,
    'schools' => $schools
];

print_r(json_encode($response));
?>
