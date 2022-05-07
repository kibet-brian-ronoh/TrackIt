<?php
require_once 'connect.php';

$otp = $_POST['otp'];
$email = $_POST['email'];
 
$check = "SELECT otp, time_requested FROM password_reset WHERE email = '$email' AND time_requested = (SELECT MAX(time_requested) FROM password_reset);";
$json_array = array();

if ($res_check = mysqli_query($conn, $check)) {
    if (mysqli_num_rows($res_check) > 0) {
        $row = mysqli_fetch_assoc($res_check);
        $dbOTP = $row['otp'];
        if ($dbOTP == $otp) {
            $json_array["Code"] = 1;
            $json_array["Message"] = "OTP is valid";
            echo json_encode($json_array);
        }
        else {
            $json_array["Code"] = 0;
            $json_array["Message"] = "OTP is invalid";
            echo json_encode($json_array);
        }  
    }
    else {
        $json_array["Code"] = 2;
        $json_array["Message"] = "Request for OTP has not been made";
        echo json_encode($json_array);
    }
}
else {
    $json_array["Code"] = 3;
    $json_array["Message"] = "Could not perform SQL query 1";
    echo json_encode($json_array);
}
mysqli_close($conn);
?>