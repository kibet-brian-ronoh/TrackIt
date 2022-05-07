<?php

require_once 'connect.php';

$make = $_POST['make'];
$model = $_POST['model'];
$year = $_POST['year'];
$type = $_POST['type'];
$color = $_POST['color'];
$vehicleOwner = $_POST['vehicleOwner'];
$numberPlate = $_POST['numberPlate'];

$query = "INSERT INTO vehicle_details (make, model, year, type, color, vehicleOwnerID, plateNumber) VALUES ('$make', '$model', $year, '$type', '$color', '$vehicleOwner', '$numberPlate');";

$json_array = array();

if ($res = mysqli_query($conn, $query)) {
    $last_insertedID = mysqli_insert_id($conn);
    $json_array["Code"] = 1;
    $json_array["vehicleID"] = $last_insertedID;
    $json_array["Message"] = "Successfully registered vehicle";
    echo json_encode($json_array);
}
else {
    $json_array["Code"] = 3;
    $json_array["Message"] = "Could not insert car";
    echo json_encode($json_array);
}
mysqli_close($conn);

?>