<?php
$con=pg_connect("host=localhost port=5432 dbname=watching user=postgres password=20140235!z");
if(!$con){
	echo "db connect fail:<br />\n";
}
$id = $_POST ['data0'];
$pw = $_POST ['data1'];
$result =  pg_exec( $con, "SELECT id FROM memberinfo WHERE id='$id' AND pw ='$pw'" );
$row = pg_fetch_assoc($result);
if ($row != null) {
	
	echo 'loginsuccess';
}else{
	echo 'loginfail';
}
pg_close( $con );
?>