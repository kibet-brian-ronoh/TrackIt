<?php

$servername = "localhost";
$username = "id16231071_yobra";
$password = "SL8<!2v>vR5Z{Iof";
$database = "id16231071_trackit";

$trackingunitid = $_GET['trackingUnit'];
$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];
$speed = $_GET['speed'];

// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}
$currentTime = new DateTime("now", new DateTimeZone('Africa/Nairobi') );
$timestamp = $currentTime->format('Y-m-d H:i:s');

$sql = "INSERT INTO location_data (trackingUnit_id, latitude, longitude, speed, time) VALUES ($trackingunitid, $latitude, $longitude, $speed, '$timestamp');";
if($conn->query($sql) === TRUE) {
	echo "New record added successfully";
}else{
	echo "Error: " .$sql . "<br>" . $conn->error;
}

$conn->close();
?>