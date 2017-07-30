package com.example.doo88.pocketv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class broadcaststart extends Activity implements AsyncResponse {

    private EditText et1;
    private Button btn1;
    dbConnect db = new dbConnect();
    private String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.broadcaststart);

        et1 = (EditText) findViewById(R.id.edittext1);
        btn1 = (Button) findViewById(R.id.button1);

        //셰어드프리퍼런스에 저장된 아이디 값을 가져온다.
        SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        id = pref.getString("id", "0");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et1.getText().toString().length()>0) {
                    String data = db.json(id, et1.getText().toString().trim());
                    String link = "http://119.207.144.112:8807/makebroadcast.php";
                    PostResponseAsyncTask login =
                            new PostResponseAsyncTask(broadcaststart.this, link, data);
                    login.execute(link, data);
                }else{
                    Toast.makeText(getApplicationContext(),"방이름을 입력하지 않았습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void processFinish(String output) {
        Log.i("방생성",""+output);
        Intent intent = new Intent(this, broadcast.class);
        intent.putExtra("roomnumber",output);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

    }
}
