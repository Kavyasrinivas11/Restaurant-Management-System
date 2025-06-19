<?php 

require_once 'db_connect.php';

$response = array();

if(isset($_GET['apicall'])){	
	switch($_GET['apicall']){
		case 'signup':
			$data = json_decode(file_get_contents('php://input'), true);
			$status = 0;
			if(strtolower($data['type']) != 'restaurant') {
				$status = 1;
			}
			$qry = "INSERT INTO `register`(`name`, `phone`, `email`, `password`, `user_type`, `status`) VALUES ('".$data['name']."','".$data['contact']."','".$data['email']."','".$data['password']."','".strtolower($data['type'])."',$status)";
			//$ClientID = strip_tags(mysqli_real_escape_string($con,$data['ClientID']));
			$sql_res = $con->query($qry);
			if($sql_res) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Signup successful");
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"true","ResponseMsg"=>"Something went wrong");
			}
			echo json_encode($returnArr);
			break;
		case 'login':
			$data = json_decode(file_get_contents('php://input'), true);
			$qry = "select * from `register` where email='".$data['email']."' and password='".$data['password']."' and status = 1";
			$sql_res = $con->query($qry);
			$rows = $sql_res->fetch_assoc();
			if(mysqli_num_rows($sql_res) > 0) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>$rows['id'], "AddMsg" => $rows['user_type']);
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"false","ResponseMsg"=>"Login failed");
			}
			echo json_encode($returnArr);
			break;
		case 'unapprovedlist':
			$dataArray = [];
			$data = json_decode(file_get_contents('php://input'), true);
			$qry = "select id,name, email, phone from `register` where status = 0 and user_type = 'restaurant'";
			$sql_res = $con->query($qry);
			while($rows = $sql_res->fetch_assoc()) {
				$dataArray[] = $rows;
			}
			
			$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Login Success", "ResData" => array("unapprovedList" => $dataArray));
			echo json_encode($returnArr);
			break;
		case 'approvehotel':
			$data = json_decode(file_get_contents('php://input'), true);
			$id = $data['rid'];
			$qry = "update `register` set status = 1 where id =  $id";
			$sql_res = $con->query($qry);
			if($sql_res) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Restaurant approved");
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"true","ResponseMsg"=>"Something went wrong");
			}
			echo json_encode($returnArr);
			break;
		case 'rejecthotel':
			$data = json_decode(file_get_contents('php://input'), true);
			$id = $data['rid'];
			$qry = "update `register` set status = -1 where id =  $id";
			$sql_res = $con->query($qry);
			if($sql_res) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Restaurant Rejected");
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"true","ResponseMsg"=>"Something went wrong");
			}
			echo json_encode($returnArr);
			break;
		case 'addcat':
			$data = json_decode(file_get_contents('php://input'), true);
			$qry = "INSERT INTO `category`(`restaurant_id`, `category`) VALUES (".$data['rid'].",'".$data['name']."')";
			$sql_res = $con->query($qry);
			if($sql_res) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Category added");
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"true","ResponseMsg"=>"Something went wrong");
			}
			echo json_encode($returnArr);
			break;
		case 'catlist':
			$dataArray = [];
			$data = json_decode(file_get_contents('php://input'), true);
			$qry = "select * from `category` where restaurant_id = ".$data['rid'];
			$sql_res = $con->query($qry);
			while($rows = $sql_res->fetch_assoc()) {
				$dataArray[] = $rows;
			}
			$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Data Fetch Success", "ResData" => array("resCatList" => $dataArray));
			echo json_encode($returnArr);
			break;
		case 'deletecat':
				$data = json_decode(file_get_contents('php://input'), true);
				$qry = "DELETE from `category` where id = ".$data['cid'];
				$sql_res = $con->query($qry);
				if($sql_res) {
					$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Category Deleted Successfully");
				}
				else {
					$returnArr = array("ResponseCode"=>"401","Result"=>"false","ResponseMsg"=>"Something went wrong");
				}
				echo json_encode($returnArr);
				break;
		case 'addproduct':
			$data = json_decode(file_get_contents('php://input'), true);
			$image_name=strip_tags(mysqli_real_escape_string($con,$data['imgname']));
			$image = $data['image'];
			$rid = $data['rid'];
			$name = $data['name'];
			$cat = $data['cat'];
			$type = $data['type'];
			$qty = $data['qty'];
			$price = $data['price'];

			$decodedImage = base64_decode($image);
			$file_nam = file_put_contents("uploads/".$image_name.".jpg", $decodedImage);
			$qry = "INSERT INTO `product`(`rid`, `name`, `category`, `image`, `type`, `qty`, `price`) VALUES ('$rid','$name','$cat','$image_name','$type','$qty',$price)";
			$sql_res = $con->query($qry);
			if($sql_res) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Category Deleted Successfully");
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"false","ResponseMsg"=>"Something went wrong");
			}
			echo json_encode($returnArr);
			break;
		case 'getproduct':
			$data = json_decode(file_get_contents('php://input'), true);
			$rid = $data['rid'];
			$qry = "SELECT * FROM product where rid=$rid";
			$sql_res = $con->query($qry);
			while($rows = $sql_res->fetch_assoc()) {
				$dataArray[] = $rows;
			}
			if($sql_res) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Product fetch successfull", "ResData" => array("resProdList" => $dataArray));
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"false","ResponseMsg"=>"Something went wrong");
			}
			echo json_encode($returnArr);
			break;
		case 'gethotels':
			$data = json_decode(file_get_contents('php://input'), true);
			$qry = "SELECT `id`, `name` FROM register where user_type='restaurant'";
			$sql_res = $con->query($qry);
			while($rows = $sql_res->fetch_assoc()) {
				$dataArray[] = $rows;
			}
			if($sql_res) {
				$returnArr = array("ResponseCode"=>"200","Result"=>"true","ResponseMsg"=>"Product fetch successfull", "ResData" => array("resList" => $dataArray));
			}
			else {
				$returnArr = array("ResponseCode"=>"401","Result"=>"false","ResponseMsg"=>"Something went wrong");
			}
			echo json_encode($returnArr);
			break;
}
}