<?php

require_once 'connect.php';

$imei = $_GET['imei'];
$imsi = $_GET['imsi'];
$vehicleID = $_GET['vehicleID'];

$query = "INSERT INTO tracking_unit (imei, imsi, vehicle_id) VALUES ($imei, $imsi, $vehicleID);";

$json_array = array();

if ($res = mysqli_query($conn, $query)) {
    $last_insertedID = mysqli_insert_id($conn);
    $json_array["Code"] = 1;
    $json_array["trackingUnitID"] = $last_insertedID;
    $json_array["Message"] = "Successfully registered Tracking device";
    echo json_encode($json_array);
}
else {
    $json_array["Code"] = 3;
    $json_array["Message"] = "Could not insert TD";
    echo json_encode($json_array);
}
mysqli_close($conn);

?>