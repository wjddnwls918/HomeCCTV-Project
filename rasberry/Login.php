<?php

	$con = mysqli_connect("#","#","#","#");

	$userID = $_POST["userID"];
	const PASSWORD_COST = ['cost' =>12];
#	$userPW = password_hash( $_POST["userPW"] , PASSWORD_DEFAULT, PASSWORD_COST );
	$userPW = $_POST["userPW"];
	$accessLevel = "accessLevel";



	$statement = mysqli_prepare($con, "select * from user where userID = ? AND userPW = ?");
	mysqli_stmt_bind_param($statement,"ss",$userID,$userPW);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $userID, $userPW, $accessLevel);
		
	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement))
{

	$response["success"] = true;
	$response["userID"] = $userID;
	$response["userPW"] = $userPW;
	$response["accessLevel"] = $accessLevel;


}

	echo json_encode($response);



?>	
