<?php

  require_once 'DB_functions.php';
      $db = new DB_functions();
	
 
// Path to move uploaded files
$target_path = "uploads/";
 
 

 
 
// array for final json respone
$response = array();
 
// getting server ip address
$server_ip = gethostbyname(gethostname());
 
// final file url that is being uploaded
$file_upload_url = 'http://37.187.122.209/' . $target_path;
 
 
if (isset($_FILES['image']['name'])) {
    $target_path = $target_path . basename($_FILES['image']['name']);
 
    // reading other post parameters
    $idDefi = isset($_POST['idDefi']) ? $_POST['idDefi'] : '';
    $lanceurDefiChoisi = isset($_POST['lanceurDefiChoisi']) ? $_POST['lanceurDefiChoisi'] : '';
	$typePreuve = isset($_POST['typePreuve']) ? $_POST['typePreuve'] : '';
	$commentaireRealisateurDefi = isset($_POST['commentaireRealisateurDefi']) ? $_POST['commentaireRealisateurDefi'] : '';
	$typeDefi = isset($_POST['typeDefi']) ? $_POST['typeDefi'] : '';
 
    $response['file_name'] = basename($_FILES['image']['name']);
    $response['idDefi'] = $idDefi;
    $response['lanceurDefiChoisi'] = $lanceurDefiChoisi;
	$response['typePreuve'] = $typePreuve;
	$response['commentaireRealisateurDefi'] = $commentaireRealisateurDefi;
	$response['typeDefi'] = $typeDefi;
	
	
 
    try {
        // Throws exception incase file is not being moved
        if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
            // make error flag true
            $response['error'] = true;
            $response['message'] = 'Could not move the file!';
        }
 
        // File successfully uploaded
		 $insert = mysql_query("INSERT INTO preuve( cheminPreuve,datePreuve, typePreuve,clefDefi) VALUES('$target_path', NOW(), '$typePreuve','$idDefi')");
		 $insert = mysql_query("UPDATE `defi` SET etat=1,commentaireRealisateurDefi='$commentaireRealisateurDefi',typeDefi='$typeDefi'  WHERE idDefi =$idDefi");
		 
		
        $response['message'] = 'File uploaded successfully!';
        $response['error'] = false;
        $response['file_path'] = $file_upload_url . basename($_FILES['image']['name']);
    } catch (Exception $e) {
        // Exception occurred. Make error flag true
        $response['error'] = true;
        $response['message'] = $e->getMessage();
    }
} else {
    // File parameter is missing
    $response['error'] = true;
    $response['message'] = 'Not received any file!F';
}
 
// Echo final json response to client
echo json_encode($response);
?>