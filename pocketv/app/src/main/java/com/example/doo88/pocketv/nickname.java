package com.example.doo88.pocketv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class nickname extends AppCompatActivity implements AsyncResponse{

    private EditText et1,et2,et3;
    private TextView tv1,tv2;
    private Button btn1;
    dbConnect db = new dbConnect();
    private Boolean namev,pwv;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nickname);

        et1=(EditText)findViewById(R.id.edittext1);
        et2=(EditText)findViewById(R.id.edittext2);
        et3=(EditText)findViewById(R.id.edittext2);
        tv1=(TextView)findViewById(R.id.textview1);
        tv2=(TextView)findViewById(R.id.textview2);
        btn1=(Button)findViewById(R.id.button1);
        namev=false;

        //아이디전달받기
        Intent intent=getIntent();
        id=intent.getStringExtra("id");

        //이름 중복체크
        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (et1.length() > 0) {
                    String data = db.json(et1.getText().toString());
                    String link = "http://119.207.144.112:8807/searchname.php";
                    PostResponseAsyncTask searchid =
                            new PostResponseAsyncTask(nickname.this, link, data);
                    searchid.execute(link, data);
                }
            }
        });

        //비밀번호 체크
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et2.getText().toString().equals(et3.getText().toString())) {
                    pwv = true;
                    tv2.setVisibility(View.VISIBLE);
                    tv2.setTextColor(Color.parseColor("#008000"));
                    tv2.setText("비밀번호가 일치합니다.");
                } else {
                    pwv = false;
                    tv2.setVisibility(View.VISIBLE);
                    tv2.setTextColor(Color.parseColor("#FF0000"));
                    tv2.setText("비밀번호가 일치하지 않습니다.");
                }
            }
        });

        //가입버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namev==true&&pwv==true){
                    //구글로그인 첫 접속시에 db에 계정정보가 없으면 계정정보를 db에 저장한다.
                    String data = db.json(id.trim(),et1.getText().toString().trim(),et2.getText().toString().trim(),"G");
                    String link = "http://119.207.144.112:8807/join.php";
                    PostResponseAsyncTask login =
                            new PostResponseAsyncTask(nickname.this, link, data);
                    login.execute(link, data);
                }else if (pwv == false) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (namev == false) {
                    Toast.makeText(getApplicationContext(), "사용할수 없는 이름입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void processFinish(String output) {
        if (output.equals("nameimpossible")) {
            namev = false;
            tv1.setVisibility(View.VISIBLE);
            tv1.setTextColor(Color.parseColor("#FF0000"));
            tv1.setText("사용할 수 없는 이름입니다.");
        } else if (output.equals("namepossible")) {
            namev = true;
            tv1.setVisibility(View.VISIBLE);
            tv1.setTextColor(Color.parseColor("#008000"));
            tv1.setText("사용 가능한 이름입니다.");
        } else if(output.equals("success")){
            String data = db.json(id.trim());
            String link = "http://119.207.144.112:8807/googlelogin.php";
            PostResponseAsyncTask login =
                    new PostResponseAsyncTask(nickname.this, link, data);
            login.execute(link, data);
        }else if(output.equals("gloginsuccess")){
            //자동로그인을 위한 아이디저장
            SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("id", id.trim());
            edit.commit();
            Intent intent = new Intent(nickname.this, main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}
