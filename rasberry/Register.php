<?php

$con = mysqli_connect("localhost","wangtou","fkaus918","test");

$userID = $_POST["userID"];
$userPW = $_POST["userPW"];
$accessLevel = 0;

$response = array();

if ($userID == "" || $userPW=="")
{
	$response["success"] = false;

	echo json_encode($response);
}
else
{

$statement = mysqli_prepare($con, "insert into user values (?,?,?)");
mysqli_stmt_bind_param($statement, "ssi",$userID,$userPW,$accessLevel);

$result = mysqli_stmt_execute($statement);

if($result == true)
{
$response["success"] = true;

echo json_encode($response);
}
else
{
	$response["success"] = false;

	echo json_encode($response);
}



}

?>
