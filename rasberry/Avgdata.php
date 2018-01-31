<?php

$con = mysqli_connect("#","#","#","test");

$day = $_POST["day"];
#$day = '2018-01-31';

if(!$con)
{
	echo "error";
}

$response = array();

for($i=0; $i<24; $i++)
{

	
$hour = $i+1;
if($hour < 10)
{
	$hour = "0".$hour;
}

$sql = "select avg(temperature),avg(humidity),avg(ppm) from sensordata where date_format(inputtime, '%Y-%m-%d %H')='".$day." ".$hour."'";

#echo $i+1;
#echo $sql;
 	$result = mysqli_query($con,$sql);
#	echo json_encode($result);
	$row = mysqli_fetch_array($result);

#echo var_dump($row);
	if($row[0]==NULL)
	{
		array_push($response, array("avgtem"=>"0",
					"avghum"=>"0",
					"avgppm"=>"0"));
	}
	else{
	array_push($response, array("avgtem"=>$row[0],
				"avghum"=>$row[1],
				"avgppm"=>$row[2]));
	}
#echo json_encode($row);
#	echo '<br>';

}

echo json_encode($response);


?>
