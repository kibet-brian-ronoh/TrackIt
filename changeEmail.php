<?php

require_once 'connect.php';

$newEmail = $_POST['newEmail'];
$ownerID = $_POST['ownerID'];

$sql = "UPDATE car_owners SET email = '$newEmail' WHERE id = '$ownerID';";
$json_array = array();

if ($res = mysqli_query($conn, $sql)) {
    $json_array["Code"] = 1;
    $json_array["Message"] = "Email changed successfully";
    echo json_encode($json_array);
}
else {
    $json_array["Code"] = 2;
    $json_array["Message"] = "Could not change email";
    echo json_encode($json_array);
}
mysqli_close($conn);

?>