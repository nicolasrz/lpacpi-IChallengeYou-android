<?php
 
   $file_path = "avatar/";
    $photoProfil = $_POST['photoProfil'];
   //$file_path = $file_path . 'LeNomQueTuVeux.extensionDuFicher');
   $file_path = $file_path . $photoProfil;
   if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
       echo "success";
   } else{
       echo "fail";
   }
?>