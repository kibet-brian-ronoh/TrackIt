<?php

require_once 'connect.php';
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require $_SERVER['DOCUMENT_ROOT'] . '/mail/Exception.php';
require $_SERVER['DOCUMENT_ROOT'] . '/mail/PHPMailer.php';
require $_SERVER['DOCUMENT_ROOT'] . '/mail/SMTP.php';

$email = $_POST['email'];

$check_email = "SELECT * FROM car_owners WHERE email = '$email';";

$json_array = array();

if ($res_check = mysqli_query($conn, $check_email)) {
    if (mysqli_num_rows($res_check) > 0) {
        $otp = generateNumericOTP(4);
        $insert = "INSERT INTO password_reset(email, otp) VALUES ('$email', '$otp');";
        if ($res_insert = mysqli_query($conn, $insert)) {
            $mail = new PHPMailer;
            $mail->isSMTP(); 
            $mail->SMTPDebug = 0; // 0 = off (for production use) - 1 = client messages - 2 = client and server messages
            $mail->Host = "smtp.gmail.com"; // use $mail->Host = gethostbyname('smtp.gmail.com'); // if your network does not support SMTP over IPv6
            $mail->Port = 587; // TLS only
            $mail->SMTPSecure = 'tls'; // ssl is deprecated
            $mail->SMTPAuth = true;
            $mail->Username = 'kbtbrn@gmail.com'; // email
            $mail->Password = 'brian2017.'; // password
            $mail->setFrom('kbtbrn@gmail.com', 'TrackIt Systems'); // From email and name
            $mail->addAddress($email, 'User'); // to email and name
            $mail->Subject = 'Password reset';
            $mail->msgHTML("Your one-time PIN for password recovery is $otp."); //$mail->msgHTML(file_get_contents('contents.html'), __DIR__); //Read an HTML message body from an external file, convert referenced images to embedded,
            $mail->AltBody = 'HTML messaging not supported'; // If html emails is not supported by the receiver, show this body
            // $mail->addAttachment('images/phpmailer_mini.png'); //Attach an image file
            $mail->SMTPOptions = array(
                                'ssl' => array(
                                    'verify_peer' => false,
                                    'verify_peer_name' => false,
                                    'allow_self_signed' => true
                                )
                            );
            if(!$mail->send()){
                $json_array["Code"] = 2;
                $json_array["Message"] = "Mailer Error";
                $json_array["error"] = $mail->ErrorInfo;
                echo json_encode($json_array);
                //echo "Mailer Error: " . $mail->ErrorInfo;
            }else{
                $json_array["Code"] = 3;
                $json_array["Message"] = "An email was sent to you containing an OTP valid for the next 30 minutes. Please check your inbox.";
                echo json_encode($json_array);
            }
        }  
    }
    else {
        $json_array["Code"] = 0;
        $json_array["Message"] = "Email is not registered!";
        echo json_encode($json_array);
    }
}

else {
    $json_array["Code"] = 4;
    $json_array["Message"] = "Could not check your email if it exists. Try again.";
    echo json_encode($json_array);
}

// Function to generate OTP
function generateNumericOTP($n) {
      
    // Take a generator string which consist of
    // all numeric digits
    $generator = "1357902468";
  
    // Iterate for n-times and pick a single character
    // from generator and append it to $result
      
    // Login for generating a random character from generator
    //     ---generate a random number
    //     ---take modulus of same with length of generator (say i)
    //     ---append the character at place (i) from generator to result
  
    $otpresult = "";
  
    for ($i = 1; $i <= $n; $i++) {
        $otpresult .= substr($generator, (rand()%(strlen($generator))), 1);
    }
  
    // Return result
    return $otpresult;
}
?>