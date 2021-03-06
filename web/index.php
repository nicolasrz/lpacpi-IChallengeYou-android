<?php

if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];
    // include db handler
    require_once 'DB_functions.php';
    $db = new DB_functions();
    // response Array
    $response = array("tag" => $tag, "error" => FALSE);
    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $pseudo = $_POST['pseudo'];
	
        $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByPseudoAndPassword($pseudo, $password);
        if ($user != false) {
          
            $response["error"] = FALSE;
      
            $response["utilisateur"]["pseudo"] = $user["pseudo"];
            $response["utilisateur"]["email"] = $user["email"];
         
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = TRUE;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $pseudo = $_POST['pseudo'];
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check if user is already existed
        if ($db->isUserExisted($pseudo,$email)) {
            // user is already existed - error response
            $response["error"] = TRUE;
            $response["error_msg"] = "Pseudo ou email éxiste déjà";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($pseudo, $email, $password);
            if ($user) {
                // user stored successfully
                $response["error"] = FALSE;
               // $response["uid"] = $user["unique_id"];
                $response["utilisateur"]["pseudo"] = $user["pseudo"];
                $response["utilisateur"]["email"] = $user["email"];
				$response["utilisateur"]["password"] = $user["password"];
               // $response["user"]["created_at"] = $user["created_at"];
                //$response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = TRUE;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    }else if($tag == "defiRecu"){
		$pseudo = $_POST['pseudo'];
		$result=$db->listeDefiRecu($pseudo);
		if($result){
			
			$response["valeurDefi"] = array();
			while ($row = mysql_fetch_array($result)) {
			
			$ligne = array();
			$ligne["resumeDefi"] = $row["defi"];
			$ligne["idDefi"] = $row["idDefi"];
			$ligne["lanceurDefi"] = $row["lanceurDefi"];
			
			// push single row into final response array
			array_push($response["valeurDefi"], $ligne);
			}
			$response["success"] = 1;
			
			//$response["error"] = FALSE;
			
			
		}else{
			$response["error"] = TRUE;
			$response["error_msg"] = "Aucun defi propose pour cette personne";
		}
		echo json_encode($response);
		
	}	else if($tag == "dernierDefi"){
	
		$result=$db->listeDernierDefi();
			if($result){
				
				$response["dernierDefi"] = array();
				while ($row = mysql_fetch_array($result)) {
				
				$ligne = array();
				$idDefi=$row["idDefi"];
				$ligne["resumeDefi"] = $row["defi"];
				$ligne["idDefi"] = $row["idDefi"];
				$ligne["realisateurDefi"] = $row["realisateurDefi"];
				$ligne["lanceurDefi"] = $row["lanceurDefi"];
				$ligne["typePreuve"]= $row["typePreuve"];
				$ligne["commentaireRealisateurDefi"]= $row["commentaireRealisateurDefi"];
				
				$preuve=mysql_query("Select cheminPreuve from preuve where clefDefi='$idDefi' ");
				
				while ($row = mysql_fetch_array($preuve)) {
					$cheminPreuve=$row['cheminPreuve'];
				}	
				$ligne["cheminPreuve"] = $cheminPreuve;
				// push single row into final response array
				array_push($response["dernierDefi"], $ligne);
				}
				$response["success"] = 1;
				
				//$response["error"] = FALSE;
				
				
			}else{
				$response["error"] = TRUE;
				$response["error_msg"] = "Aucun defi propose pour cette personne";
			}
			echo json_encode($response);
		
	}else if($tag == "listeAmi"){
		$pseudo = $_POST['pseudo'];
		$result=$db->listeAmiFrom($pseudo);
		$result2=$db->listeAmiTo($pseudo);
		if($result){
			
			$response["listeAmi"] = array();
			while ($row = mysql_fetch_array($result)) {
			
			$ligne = array();
			//$idDefi=$row["idDefi"];
			$ligne["amiFrom"] = $row["amiFrom"];
			$ligne["amiTo"] = $row["amiTo"];
			$ligne["amiDate"] = $row["amiDate"];
			$ligne["amiLien"] = $row["amiLien"];
			// push single row into final response array
			array_push($response["listeAmi"], $ligne);
			}
			$response["success"] = 1;
			
			//$response["error"] = FALSE;
			
			
		}
		if($result2){
			while ($row = mysql_fetch_array($result2)) {
				$ligne = array();
				//$idDefi=$row["idDefi"];
				$ligne["amiFrom"] = $row["amiFrom"];
				$ligne["amiTo"] = $row["amiTo"];
				$ligne["amiDate"] = $row["amiDate"];
				$ligne["amiLien"] = $row["amiLien"];
				// push single row into final response array
				array_push($response["listeAmi"], $ligne);
			}
			$response["success"] = 1;
		}
		
		else{
			$response["error"] = TRUE;
			$response["error_msg"] = "Aucun ami enregistré";
		}
		echo json_encode($response);
		
	}
	else if($tag == "supprimerAmi"){
		$amiLien = $_POST['amiLien'];   	 
		$delete = mysql_query("DELETE FROM `ami` WHERE amiLien='$amiLien' ");
		$response['message'] = 'Suppresion ok';
		$response['error'] = false;
        		
}else if($tag == "invitationRecu"){
		$pseudo = $_POST['pseudo'];
		$result=$db->invitationRecu($pseudo);
		if($result){
			
			$response["invitationRecu"] = array();
			while ($row = mysql_fetch_array($result)) {
			
				$ligne = array();
				//$idDefi=$row["idDefi"];
				$ligne["amiFrom"] = $row["amiFrom"];
				$ligne["amiTo"] = $row["amiTo"];
				$ligne["amiDate"] = $row["amiDate"];
				$ligne["amiLien"] = $row["amiLien"];
				$ligne["amiTexteDemande"] = $row["amiTexteDemande"];
				// push single row into final response array
				array_push($response["invitationRecu"], $ligne);
			}
			$response["success"] = 1;
		}else{
			$response["error"] = TRUE;
			$response["error_msg"] = "Aucune nouvelles  invitations recus";
		}
		echo json_encode($response);
		
	}else if($tag == "accepterAmi"){
		$amiFrom = $_POST['amiFrom'];   
		$amiTo = $_POST['amiTo'];   
		$update = mysql_query("UPDATE ami SET amiConfirm=1,amiDate=NOW() WHERE amiFrom='$amiFrom' and amiTo='$amiTo' ");
		$response['message'] = 'Accepter mai ok';
		$response['error'] = false;
	}
	else if($tag == "refuserAmi"){
		$amiFrom = $_POST['amiFrom'];   
		$amiTo = $_POST['amiTo'];   
		$delete = mysql_query("DELETE FROM ami WHERE amiFrom='$amiFrom' AND amiTo='$amiTo'");
		$response['message'] = 'Refus ami ok';
        $response['error'] = false;
	}
	else if($tag == "envoyerDefi"){
		$realisateurDefi = $_POST['realisateurDefi'];   
		$lanceurDefi = $_POST['lanceurDefi'];   
		$defi=$_POST['defi'];	
		$db->envoyerDefi($realisateurDefi,$lanceurDefi,$defi);
	}else if($tag == "cerclePrive"){
		$pseudo = $_POST['pseudo'];
		$result=$db->listeAmiFrom($pseudo);
		$result2=$db->listeAmiTo($pseudo);
		$response["cerclePrive"] = array();
		$tableauAmi=array();
		if($result){
				while ($row = mysql_fetch_array($result)) {
					$ligne = array();
					$ligne["pseudo"] = $row["amiTo"];
					array_push($tableauAmi, $ligne["pseudo"]);
				}
			$response["success"] = 1;
		}
		if($result2){
				while ($row = mysql_fetch_array($result2)) {
					$ligne = array();
					$ligne["pseudo"] = $row["amiFrom"];
					array_push($tableauAmi, $ligne["pseudo"]);
				}
			$response["success"] = 1;
		}
		foreach($tableauAmi as $element)
		{
			$rechercheDefi1=$db->rechercheDefiAmiPrive($element);
			if($rechercheDefi1){
				while ($row = mysql_fetch_array($rechercheDefi1)) {
						$ligne = array();
						$ligne["idDefi"] = $row["idDefi"];
						$ligne["defi"] = $row["defi"];
						$ligne["realisateurDefi"] = $row["realisateurDefi"];
						$ligne["lanceurDefi"] = $row["lanceurDefi"];
						$ligne["commentaireRealisateurDefi"] = $row["commentaireRealisateurDefi"];
						$ligne["cheminPreuve"] = $row["cheminPreuve"];
						$ligne["typePreuve"] = $row["typePreuve"];
						array_push($response["cerclePrive"] , $ligne);
					}		
			}
		}		
		echo json_encode($response);
	}
	else if($tag == "rechercherAmi"){
		$pseudoOuEmail = $_POST['pseudoOuEmail'];   
		$pseudoOuEmailPerso = $_POST['pseudoOuEmailPerso'];   
		$result=$db->rechercherAmiPseudo($pseudoOuEmail,$pseudoOuEmailPerso);
		$result2=$db->rechercherAmiEmail($pseudoOuEmail,$pseudoOuEmailPerso);
		$response["rechercherAmi"] = array();
		if($result){			
			$response["rechercherAmi"] = array();
			while ($row = mysql_fetch_array($result)) {			
				$ligne = array();
				//$idDefi=$row["idDefi"];
				$ligne["pseudo"] = $row["pseudo"];
				$ligne["email"] = $row["email"];
				$ligne["age"] = $row["age"];
				$ligne["avatar"] = $row["avatar"];
			
				// push single row into final response array
				array_push($response["rechercherAmi"], $ligne);
			}
			$response["success"] = 1;
		}
			if($result2){
				while ($row = mysql_fetch_array($result)) {		
					$ligne = array();
					//$idDefi=$row["idDefi"];
					$ligne["pseudo"] = $row["pseudo"];
					$ligne["email"] = $row["email"];
					$ligne["age"] = $row["age"];
					$ligne["avatar"] = $row["avatar"];			
					// push single row into final response array
					array_push($response["rechercherAmi"], $ligne);
				}
				$response["success"] = 1;
			}
			echo json_encode($response);
	}else if($tag == "ajouterAmi"){
		$pseudoDemandeur=$_POST['pseudoDemandeur'];
		$pseudo = $_POST['pseudo'];   
		$message = $_POST['message'];   
		$db->ajouterAmi($pseudoDemandeur,$pseudo,$message);
		
	}else if($tag == "selectUtilisateur"){
		$pseudo=$_POST['pseudo'];
		$result=$db->selectUtilisateur($pseudo);
		   if ($result) {
				$response["selectUtilisateur"] = array();
				while ($row = mysql_fetch_array($result)) {					
					$ligne = array();
					$ligne["avatar"] = $row["avatar"];
					$ligne["pseudo"] = $row["pseudo"];
					array_push($response["selectUtilisateur"], $ligne);
					$response["error"] = FALSE;
				}
			}else{
					$response["error"] = TRUE;
			}
		echo json_encode($response);
	
	}
	else {
        // user failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknow 'tag' value. ";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>