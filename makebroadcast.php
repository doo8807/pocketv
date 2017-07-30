<?php
$con=pg_connect("host=localhost port=5432 dbname=watching user=postgres password=20140235!z");
if(!$con){
	echo "db connect fail:<br />\n";
}
$id = $_POST ['data0'];
$title=$_POST['data1'];
$date = date ( 'Y-m-d H:i:s' );
$result = pg_exec( $con, "select name from memberinfo where id='$id'" );
if ($result) {
    while ($row = pg_fetch_row($result)) {
        $name=$row[0];
    }
	$result1= pg_exec( $con, "INSERT INTO broadcast(id,name,hits,view,type,title,thumnail,date) VALUES ('$id','$name','0','0','L','$title',' ','$date')" );
	if($result1){
	    $result2 = pg_exec( $con, "select no from broadcast where title='$title' and date='$date'" );
	    if($result2){
	        while ($row1 = pg_fetch_row($result2)) {
	            $roomnumber=$row1[0];	       
	        }
	        $result3= pg_exec( $con, "UPDATE broadcast SET address='http://119.207.144.112:8807/hls/$roomnumber.m3u8', thumnail='$roomnumber.jpg' where title='$title' and date='$date'" );
	        if($result3){
	            echo $roomnumber;
	        }
	    }else{
	        echo 'failure1';
	    }
	}else{
	    echo pg_last_error();
	}
} else {
	echo 'failure3';
}
pg_close( $con );
?>