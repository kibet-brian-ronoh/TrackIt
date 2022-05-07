<?php

require_once 'connect.php';

$trackingUnitID = $_POST['trackID'];

$sql = "SELECT * FROM location_data WHERE time =(SELECT MAX(time) FROM location_data WHERE trackingUnit_id = $trackingUnitID);";

$json_array = array();

if ($res = mysqli_query($conn, $sql)) {
    if (mysqli_num_rows($res) > 0) {
       while ($row = mysqli_fetch_assoc($res)) {
            $json_array["latitude"] = $row["latitude"];
            $json_array["longitude"] = $row["longitude"];
            $json_array["speed"] = $row["speed"];
            $json_array["time"] = $row["time"];
            $json_array["Code"] = 1;
            $json_array["Message"] = "Location data updated";
            echo json_encode($json_array);
       } 
    }
    else {
        $json_array["Code"] = 2;
        $json_array["Message"] = "No location data for this tracking device";
        echo json_encode($json_array);
    }
}
else {
    $json_array["Code"] = 3;
    $json_array["Message"] = "Could not perform query, SQL error";
    echo json_encode($json_array); 
}
mysqli_close($conn);
?>