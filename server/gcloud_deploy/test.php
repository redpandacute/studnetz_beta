<?php

require __DIR__ . '/env.php';

use Google\Cloud\Storage\StorageClient;

$app = array();
$app['mysql_user'] = $mysql_user;
$app['mysql_password'] = $mysql_password;
$app['mysql_dbname'] = "Studnetz_beta_MySQL_DB";
$app['project_id'] = getenv('GCLOUD_PROJECT');

$servername = null;
$username = $app['mysql_user'];
$password = $app['mysql_password'];
$dbname = $app['mysql_dbname'];
$dbport = null;

$connectionname = "deep-contact-232012:europe-west1:studnetz-beta-1-db"; 

//CREATE CONNECTION TO DB

$con = new mysqli($servername, $username, $password, $dbname, $dbport, "/cloudsql/". $connectionname);

if($con->connect_error) {
	die("Connection failed: " . $con->connect_error);
}

echo "\nConnected successfully\n";
?>


