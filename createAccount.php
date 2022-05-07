<?php

require_once 'connect.php';

$firstName = $_POST['firstName'];
$lastName = $_POST['lastName'];
$email = $_POST['email'];
$phoneNumber = $_POST['phoneNumber'];
$password = $_POST['password'];

$hashed_password = password_hash($password,  PASSWORD_DEFAULT); 

$check_existing = "SELECT * FROM car_owners WHERE email = '$email';";
$sql_insert = "INSERT INTO car_owners(firstName, lastName, email, phoneNo, password) VALUES ('$firstName', '$lastName', '$email', '$phoneNumber', '$hashed_password');";

$json_array = array();

if ($res = mysqli_query($conn, $check_existing)) {
    if (mysqli_num_rows($res) > 0) {
        $json_array["Code"] = 0;
        $json_array["Message"] = "Email already exists";
        echo json_encode($json_array);
    }
    else {
        if ($insert = mysqli_query($conn, $sql_insert)) {
            $last_insertedID = mysqli_insert_id($conn);
            $json_array["ownerID"] = $last_insertedID;
            $json_array["Code"] = 1;
            $json_array["Message"] = "Successfully registered.";
            echo json_encode($json_array);
        }
        else {
            $json_array["Code"] = 2;
            $json_array["Message"] = "SQL could not execute";
            echo json_encode($json_array);
        }
    }
}
else {
    $json_array["Code"] = 3;
    $json_array["Message"] = "Could not check existing user";
    echo json_encode($json_array);
}
mysqli_close($conn);
?>