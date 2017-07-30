<?php
$con=pg_connect("host=localhost port=5432 dbname=watching user=postgres password=20140235!z");
if(!$con){
	echo "db connect fail:<br />\n";
}
$id = $_POST ['data0'];
$result =  pg_exec( $con, "SELECT id FROM memberinfo WHERE id='$id'" );
$row = pg_fetch_assoc($result);
if ($row != null) {
	
	echo 'gloginsuccess';
}else{
	echo 'gloginfail';
}
pg_close( $con );
?>