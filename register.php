<?php

require_once 'connect.php';

$firstName = $_POST['firstName'];
$lastName = $_POST['lastName'];
$email = $_POST['email'];
$phoneNumber = $_POST['phoneNo'];
$password = $_POST['password'];
$make = $_POST['make'];
$model = $_POST['model'];
$year = $_POST['year'];
$type = $_POST['type'];
$color = $_POST['color'];
$numberPlate = $_POST['numberPlate'];
$imei = $_POST['imei'];
$imsi = $_POST['imsi'];

$hashed_password = password_hash($password,  PASSWORD_DEFAULT);

$check_existing = "SELECT * FROM car_owners WHERE email = '$email';";

$sql_insert = "INSERT INTO car_owners(firstName, lastName, email, phoneNo, password) VALUES ('$firstName', '$lastName', '$email', '$phoneNumber', '$hashed_password');
INSERT INTO vehicle_details (make, model, year, type, color, vehicleOwnerID, plateNumber) VALUES ('$make', '$model', '$year', '$type', '$color', (SELECT id FROM car_owners WHERE email = '$email'), '$numberPlate');
INSERT INTO tracking_unit (imei, imsi, vehicle_id) VALUES ('$imei', '$imsi', LAST_INSERT_ID());";

$json_array = array();

if ($res_check = mysqli_query($conn, $check_existing)) {
    if (mysqli_num_rows($res_check) > 0) {
        $json_array["Code"] = 0;
        $json_array["Message"] = "Email already exists";
        echo json_encode($json_array);
    }
    else {
        if ($res_insert = mysqli_multi_query($conn, $sql_insert)) {
            $json_array["Code"] = 1;
            $json_array["Message"] = "Successfully registered.";
            echo json_encode($json_array);
        }
        else {
            $json_array["Code"] = 3;
            $json_array["Message"] = "Could not insert";
            echo json_encode($json_array);
        }
    }
}
else {
    $json_array["Code"] = 4;
    $json_array["Message"] = "Could not check existing user";
    echo json_encode($json_array);
}
mysqli_close($conn);

?>