<?php

$con = mysqli_connect("192.168.219.136","wangtou","fkaus918","test");

$select = " select * from sensordata order by idx desc";
$result = mysqli_query($con, $select);

$row = mysqli_fetch_assoc($result);

$response = array();

$response["temperature"] = $row['temperature'];
$response["humidity"] = $row['humidity'];
$response["ppm"] = $row['ppm'];
$response["flameState"] = $row['flameState'];
$response["humState"] = $row['humState'];
$response["inputtime"] = $row['inputtime'];

echo json_encode($response);

mysqli_close($con);



?>

