<?php
   // inclure la classe de connexion
    require_once __DIR__ . '/connexion.php';
   $db = new CONNEXION_DB ();
   
 if (isset($_POST['mail']) && isset($_POST['password'])) {
 $mail = $_POST['mail'];
 $password = $_POST['password'];
$password = md5($password);
 }
 
//|| empty($_POST['password'])
			 if (empty($mail) || empty($password) ){ 
			 
			 // Create some data that will be the JSON response 
				$response["success"] = 0;
				$response["message"] = "Debut . One or both of the fields are empty .";
	
				//die is used to kill the page, will not let the code below to be executed. It will also 
				//display the parameter, that is the json data which our android application will parse to be 
				//shown to the users 
				die(json_encode($response));
				 }else{
	
						
							$mail=$_POST["mail"];
							 $query = " SELECT * FROM utilisateur WHERE mail = '$mail' and password='$password'";
							 $sql1=mysql_query($query);
							 $row = mysql_fetch_array($sql1);
							 if (!empty($row)) { 
										$response["success"] = 1;
										$response["message"] = "You have been sucessfully login";
										die(json_encode($response));
							 } 
							else{ 
										$response["success"] = 0;
										$response["message"] = "invalid mail or password ";
										die(json_encode($response));
							 } 
 } 

 mysql_close();
 ?>