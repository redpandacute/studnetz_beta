<?php
$app = array();
$app['mysql_user'] = $mysql_user;
$app['mysql_password'] = $mysql_password;
$app['mysql_dbname'] = "Studnetz_beta_MySQL_DB";
$app['project_id'] = getenv('GCLOUD_PROJECT');
$app['connection_name'] = "deep-contact-232012:europe-west1:studnetz-beta-1-db";
?>
