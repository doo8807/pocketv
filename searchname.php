<?php
$con=pg_connect("host=localhost port=5432 dbname=watching user=postgres password=20140235!z");
if(!$con){
	echo "db connect fail:<br />\n";
}
$name = $_POST ['data0'];
$result = pg_exec( $con, "select name from memberinfo where name='{$name}'" );
$row = pg_fetch_assoc($result);
if ($row != null) {
	
	echo 'nameimpossible';
}else{
	echo 'namepossible';
}
pg_close( $con );
?>