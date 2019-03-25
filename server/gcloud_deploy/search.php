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
/**
 *user_crit includes: "name", "verified"
 *subject crit includes all subject ids
 *school crit includes minimum grade, school, or special things such as university or sth, not sure yet
*/


//TODO CHANGE ACCORDING TO https://stackoverflow.com/a/17026879 

$user_crit = json_decode($_POST['user_crit'], true); 
$subject_crit = json_decode($_POST['subject_crit']);
$school_crit = json_decode($_POST['school_crit'], true);

//selecting all possible ids for school criteria
//building SQL search statement
/**
 
 	 SELECT grp.user_id, 
 	 	user_archive.uuid_text, 
 	 	user_archive.firstname, 
 	 	user_archive.lastname, 
 	 	school_conn.grade, 
 	 	school_list.uuid_text, 
 	 	school_list.schoolname, 
 	 	school_list.school_abrv, 
 	 	schooltype_list.uuid_text, 
 	 	schooltype_list.schooltype_name, 
 	 	schooltype_list.schooltype_abrv 
 	 FROM (
 		(SELECT DISTINCT user_id FROM user_archive WHERE (firstname + ' ' + lastname LIKE ? OR lastname + ' ' + firstname LIKE ?) AND account_verification_state >= 1)
 		UNION ALL
 		(SELECT DISTINCT user_id FROM subject_conn WHERE subject_id = ?)
 		UNION ALL 
 		(SELECT DISTINCT user_id FROM school_conn WHERE school_id = ?)
 	) AS grp 
 		INNER JOIN user_archive ON grp.user_id = user_archive.user_id 
 		INNER JOIN school_conn ON grp.user_id = school_conn.user_id 
 		INNER JOIN school_list ON school_conn.school_id = school_list.school_id 
 		INNER JOIN schooltype_list ON school_list.schooltype_id = schooltype_list.schooltype_id 
 	GROUP BY grp.user_id HAVING count(*) = 3;
 	
SELECT uni.user_id,
       uarch.uuid_text, 
       uarch.firstname, 
       uarch.lastname,
       sconn.grade,
       slist.uuid_text,
       slist.schoolname,
       slist.school_abrv,
       stype.uuid_text,
       stype.schooltype_name,
       stype.schooltype_abrv
FROM (

 	(SELECT DISTINCT user_id FROM user_archive WHERE (CONCAT(firstname, ' ', lastname) LIKE ? OR CONCAT(lastname, ' ', firstname) LIKE ?) AND account_verification_state >= 1)
	UNION ALL
	(SELECT DISTINCT user_id FROM subject_conn WHERE subject_id = ?)
 	UNION ALL 
 	(SELECT DISTINCT user_id FROM school_conn WHERE school_id = ? AND grade = ?)
) AS uni 
	LEFT JOIN user_archive AS uarch ON uni.user_id = uarch.user_id 
	LEFT JOIN school_conn AS sconn ON uni.user_id = sconn.user_id 
	LEFT JOIN school_list AS slist ON sconn.school_id = slist.school_id 
	LEFT JOIN schooltype_list AS stype ON slist.schooltype_id = stype.schooltype_id
GROUP BY uni.user_id HAVING COUNT(*) = ?;	
**/

$query = 'SELECT 
		uni.user_id,
		uarch.uuid_text, 
		uarch.firstname, 
		uarch.lastname,
		sconn.grade,
		slist.uuid_text,
       		slist.schoolname,
       		slist.school_abrv,
       		stype.uuid_text,
       		stype.schooltype_name,
       		stype.schooltype_abrv
	FROM ('
;

$args_cnt = 0;

if(isset($user_crit['name']) {

	$query = "(SELECT DESTINCT user_id FROM user_archive WHERE (firstname + ' ' + lastname LIKE ? OR lastname + ' ' + firstname LIKE ?)";
}



if($user_crit['verification'] == 1 && isset($user_crit['name'])) {
	$query .= " AND account_verification_state >= 1)";
	$args_cnt++;
} elseif($user_crit['verification'] == 1) {
	$query .= "(SELECT DESTINCT user_id FROM user_archive WHERE account_verification_state >= 1)";
	$args_cnt++;
} elseif(isset($user_crit['name'])) {
	$query .= ')';
	$args_cnt++;
}

//safe, since the ids are guaranteed by the database and not inserted by the user directly

if(isset($subject_crit)) {

	foreach($subjects_ids as $subject_id) {

		if($args_cnt > 0) {
			$query .= ' UNION ALL';
		}

		$query .= " (SELECT DISTINCT user_id FROM subject_conn WHERE subject_id = " . $subject_id  .  ')';
		$args_cnt++;
	}
}

if(isset($school_crit['school_uuid'])) {
	
	if($args_cnt > 0) {
		$query .= ' UNION ALL';
	}

	//todo query school_id from uuid in db

	$query .= "(SELECT DESTINCT user_id FROM school_conn WHERE school_id = " . $school_id;

	if(isset($school_crit['school_grade'])) {
		$query .= ' AND grade = ?)';
	} else {
		$query .= ')';
	}
	$args_cnt++;
}

if($args_cnt > 0) {
	
	$query .= 
') AS uni 
	INNER JOIN user_archive AS uarch ON uni.user_id = uarch.user_id 
	LEFT JOIN school_conn AS sconn ON uni.user_id = sconn.user_id 
	LEFT JOIN school_list AS slist ON sconn.school_id = slist.school_id 
	LEFT JOIN schooltype_list AS stype ON slist.schooltype_id = stype.schooltype_id
GROUP BY uni.user_id HAVING COUNT(*) = ' . $args_cnt .';';

} else {
	//todo print back that no criteriars were received
}

//creating prepared statement according to the received crterias

$stmt = mysqli_prepare($con, $query);

if(isset($user_crit['name']) && isset($school_crit['school_grade'])) {

	mysqli_stmt_bind_param($stmt, 'ssi', $user_crit['name'], $user_crit['name'], $school_crit['school_grade']);

} elseif(isset($user_crit['name'])) {

	mysqli_stmt_bind_param($stmt, 'ss', $user_crit['name'], $user_crit['name']);

} elseif(isset($school_crit['school_grade'])) {

	mysqli_stmt_bind_param($stmt, 'i', $school_crit['school_grade']);
}

//executing statemen an fetching all the results
//

mysqli_stmt_execute($stmt);
mysqli_stmt_store_result($stmt);

if(mysqli_stmt_num_rows($stmt) == 0) {
	//todo print back that no results could be found
}

mysqli_stmt_bind_result($stmt, $user_id);

$result = array();

while($row = mysqli_stmt_fetch($stmt)) {
	$result[] = $user_id;
}


?>
