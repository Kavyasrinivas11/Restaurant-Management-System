<?php
    
    define('HOST', 'localhost');
    define('USER', 'root');
    define('PASS', '');
   // define('USER', 'amaldz_user');
   // define('PASS', 'GreenWattP@ssw0rd');
    define('DB', 'restaurant_management');

    $con = mysqli_connect(HOST, USER, PASS, DB);
    mysqli_set_charset($con, 'utf8');
    
    if(!$con){
        die("Error in connection ".mysqli_connect_error());
    }
?>