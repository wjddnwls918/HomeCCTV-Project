<?php

$con = mysqli_connect("localhost","wangtou","fkaus918","test");



$select = " select distinct(date_format(inputtime,'%Y')) from sensordata where date_format(inputtime,'%Y') between '2018' and '2118'";
$result = mysqli_query($con, $select);

$response = array();


while($row = mysqli_fetch_array($result))
{
	array_push($response,array('inputtime'=>$row[0]));

}

$json = json_encode($response);

echo $json;



mysqli_close($con);



?>

