<?php
require_once 'connect.php';

$newPassword = $_POST['newPassword'];
$email = $_POST['email'];
$hashed_password = password_hash($newPassword,  PASSWORD_DEFAULT);

$sql = "UPDATE car_owners SET password = '$hashed_password' WHERE email = '$email';";

if (mysqli_query($conn, $sql)) {
    $json_array["Code"] = 1;
    $json_array["Message"] = "Password changed successfully";
    echo json_encode($json_array);
}
else {
    $json_array["Code"] = 2;
    $json_array["Message"] = "Could not change password. Try again.";
    echo json_encode($json_array);
}
mysqli_close($conn);
?>