<?php
$con=pg_connect("host=localhost port=5432 dbname=watching user=postgres password=20140235!z");
if(!$con){
	echo "db connect fail:<br />\n";
}
$id = $_POST ['data0'];
$name=$_POST['data1'];
$pw = $_POST ['data2'];
$type = $_POST ['data3'];
$date = date ( 'Y-m-d H:i:s' );
$result =  pg_exec( $con, "INSERT INTO memberinfo(id,name,pw,type,date) VALUES ('$id','$name','$pw','$type','$date')" );
if ($result) {
	echo 'success';
} else {
	echo 'failure';
}
pg_close( $con );
?>