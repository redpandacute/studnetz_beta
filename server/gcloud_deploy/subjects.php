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
		'error' => $con->connect_error . ':Failed to connect to DB'
	];
		
	print_r(json_encode($response));
	exit();
}

$stmt = mysqli_prepare($con, "SELECT uuid_text, subjectname, HEX(color) FROM subject_list");
mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);
mysqli_stmt_bind_result($stmt, $subject_uuid, $subject_name, $subject_color);

$school = array();

while($row = mysqli_stmt_fetch($stmt)) {

    $subject = [
        'subject_uuid' => $subject_uuid,
        'subject_name' => $subject_name,
        'subject_color' => $subject_color
    ];

    $subjects[] = $subject;
}

mysqli_stmt_close($stmt);

$response = [
    'success' => true,
    'schools' => $subjects
];

print_r(json_encode($response));

?>
