<?php

$con = mysqli_connect("#","#","#","test");



$select = " select distinct(date_format(inputtime,'%Y-%m-%d')) from sensordata where date_format(inputtime,'%Y-%m-%d') between '2018-01-01' and '2018-12-29'";
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

