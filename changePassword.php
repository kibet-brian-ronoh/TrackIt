<?php

require_once 'connect.php';

$oldPassword = $_POST['oldPassword'];
$newPassword = $_POST['newPassword'];
$ownerID = $_POST['ownerID'];

$sql = "SELECT password FROM car_owners WHERE id = '$ownerID';";
$json_array = array();

if ($res = mysqli_query($conn, $sql)) {
    if (mysqli_num_rows($res) > 0) {
        while ($row = mysqli_fetch_assoc($res)) {
            $fetched_password = $row["password"];

            $verify_password = password_verify($oldPassword, $fetched_password);
            if ($verify_password) {
                $hashed_password = password_hash($newPassword,  PASSWORD_DEFAULT);
                $sql2 = "UPDATE car_owners SET password = '$hashed_password' WHERE id = '$ownerID';";

                if (mysqli_query($conn, $sql2)) {
                    $json_array["Code"] = 1;
                    $json_array["Message"] = "Password changed successfully";
                    echo json_encode($json_array);
                }
                else {
                    $json_array["Code"] = 2;
                    $json_array["Message"] = "Could not update table"; 
                    echo json_encode($json_array);
                }
            }
            else {
                $json_array["Code"] = 3;
                $json_array["Message"] = "Incorrect old password";
                echo json_encode($json_array);
            }
        }
    }
    else {
        $json_array["Code"] = 4;
        $json_array["Message"] = "No such user";
        echo json_encode($json_array);
    }
}
else {
    $json_array["Code"] = 5;
    $json_array["Message"] = "Could not select owner to update table";
    echo json_encode($json_array);
}
mysqli_close($conn)
?>