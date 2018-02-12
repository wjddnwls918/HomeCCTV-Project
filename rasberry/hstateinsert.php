<?php


	$hstate = $_POST["hstate"];
#$token = "tes33333";

	$con = mysqli_connect("localhost","wangtou","fkaus918","test");
	$query = "insert into hdetect(hstate) values('".$hstate."');";


	$result = mysqli_query($con,$query);

	echo $result;



?>
















