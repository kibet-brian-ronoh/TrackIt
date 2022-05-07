<?php

require_once 'connect.php';

$email = $_POST['email'];
$password = $_POST['password'];

$sql = "SELECT id, firstName, lastName, email, phoneNo, password FROM car_owners WHERE email = '$email';";

$json_array = array();

if ($res = mysqli_query($conn, $sql)) {
    if (mysqli_num_rows($res) > 0) {
        while ($row = mysqli_fetch_assoc($res)) {
            $fetched_password = $row["password"];

            $verify_password = password_verify($password, $fetched_password);

            if ($verify_password) {

                $ownerID = $row["id"];
                $json_array["ownerID"] = $row["id"];
                $json_array["firstName"] = $row["firstName"];
                $json_array["lastName"] = $row["lastName"];
                $json_array["email"] = $row["email"];
                $json_array["phoneNo"] = $row["phoneNo"];
                $json_array["Code"] = 1;
                $json_array["Message"] = "Login Successful";

                $queryVehicleDetails = "SELECT * FROM vehicle_details WHERE vehicleOwnerID = '$ownerID';"; 

                if ($result2 = mysqli_query($conn, $queryVehicleDetails)) {
                    if (mysqli_num_rows($result2) > 0) {
                        while ($vehicleRows = mysqli_fetch_assoc($result2)) {

                            $vehicleID = $vehicleRows["id"];

                            $json_array["vehicleID"] = $vehicleRows["id"];
                            $json_array["make"] = $vehicleRows["make"];
                            $json_array["model"] = $vehicleRows["model"];
                            $json_array["year"] = $vehicleRows["year"];
                            $json_array["type"] = $vehicleRows["type"];
                            $json_array["plateNumber"] = $vehicleRows["plateNumber"];
                            $json_array["color"] = $vehicleRows["color"];

                            $queryTD = "SELECT * FROM tracking_unit WHERE vehicle_id = '$vehicleID';";
                            if ($result3 = mysqli_query($conn, $queryTD)) {
                                if (mysqli_num_rows($result3) > 0) {
                                    while ($TDrows = mysqli_fetch_assoc($result3)) {
                                        $json_array["trackingDeviceID"] = $TDrows["id"];
                                        $json_array["IMEI"] = $TDrows["imei"];
                                        $json_array["IMSI"] = $TDrows["imsi"];
                                    }
                                }
                            }
                        }
                    }
                }
                echo json_encode($json_array);
            } 
            else {
                $json_array["Code"] = 2;
                $json_array["Message"] = "Password incorrect";
                echo json_encode($json_array);
            }
        }
    }
    else {
        $json_array["Code"] = 3;
        $json_array["Message"] = "Invalid user email";
        echo json_encode($json_array);
    }
}
else {
    $json_array["Code"] = 4;
    $json_array["Message"] = "Could not check email of user";
    echo json_encode($json_array);
}
mysqli_close($conn);
?>