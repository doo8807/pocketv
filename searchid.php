<?php
$con=pg_connect("host=localhost port=5432 dbname=watching user=postgres password=20140235!z");
if(!$con){
	echo "db connect fail:<br />\n";
}
$id = $_POST ['data0'];
$result = pg_exec( $con, "select id from memberinfo where id='{$id}'" );
$row = pg_fetch_assoc($result);
if ($row != null) {
	
	echo 'idimpossible';
}else{
	echo 'idpossible';
}
pg_close( $con );
?>