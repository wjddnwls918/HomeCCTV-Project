<?php

$con = mysqli_connect("localhost","wangtou","fkaus918","test");

#$year = $_POST["year"];
$year = "2018";


$response = array();

for($i =1; $i<=12; $i++){

	
	if($i<10)
		$month ="0".$i;
	else
		$month = $i;

	$select = "select avg(temperature),avg(humidity),avg(ppm) from sensordata where date_format(inputtime,'%Y-%m')='".$year."-".$month."'";

	$result = mysqli_query($con,$select);
	$row = mysqli_fetch_array($result);

	if($row[0] === null)
	{
		array_push($response,array('monthavgtem'=>0,
					'monthavghum'=>0,
					'monthavgppm'=>0));

	}
	else
	{
	array_push($response,array('monthavgtem'=>$row[0],
				'monthavghum'=>$row[1],
				'monthavgppm'=>$row[2]));
	}
}


$json = json_encode($response);

echo $json;



mysqli_close($con);



?>

