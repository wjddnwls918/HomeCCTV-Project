<?php

	$con = mysqli_connect("localhost","wangtou","fkaus918","test");

	$receivedId = $_POST["receivedId"];
	
	const PASSWORD_COST = ['cost'=>12];

#	$receivedPW = password_hash($_POST["receivedPW"], PASSWORD_DEFAULT, PASSWORD_COST );
	$receivedPW = $_POST["receivedPW"];
	

	$sql = "update `user` set userPW='".$receivedPW."' where userID='".$receivedId."'";

	


	$response = array();

	if( $receivedPW != "")
{
	if( mysqli_query($con,$sql) )
	{
	$response["success"] = true;
	}
	else
	{	
	$response["success"] = false;
	}

	echo json_encode($response);
}

	else
{
	$response["success"] = false;
}


?>	
