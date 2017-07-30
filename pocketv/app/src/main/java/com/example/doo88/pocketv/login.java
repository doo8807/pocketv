package com.example.doo88.pocketv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class login extends AppCompatActivity implements AsyncResponse{
    private EditText et1, et2;
    private Button btn1, btn2;
    private static final int join_id = 10000;
    private String type;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount acct;
    dbConnect db = new dbConnect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //변수선언
        et1 = (EditText) findViewById(R.id.edittext1);
        et2 = (EditText) findViewById(R.id.edittext2);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);

        //로그인시 아이디,타입 정보 저장해 자동로그인
        SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String id = pref.getString("id", null);
        Log.i("아이디", "" + id);
        if (id != null) {
            Intent intent = new Intent(login.this, main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

        //구글계정연동
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // 필요한 항목이 있으면 아래에 추가
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(login.this)
                .enableAutoManage(login.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // 연결에 실패했을 경우 실행되는 메서드입니다.
                    }
                })
                // 필요한 api가 있으면 아래에 추가
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//구글로그인버튼
        Button signInButton = (Button) findViewById(R.id.button3);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RESOLVE_CONNECTION_REQUEST_CODE);
            }
        });

        //로그인버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = db.json(et1.getText().toString().trim(),et2.getText().toString().trim());
                String link = "http://119.207.144.112:8807/login.php";
                PostResponseAsyncTask login =
                        new PostResponseAsyncTask(login.this, link, data);
                login.execute(link, data);
            }
        });

        //회원가입
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, join.class);
                startActivityForResult(i, join_id);
            }
        });

    }


    //회원가입후 아이디 비밀번호 자동 기입
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {

            case join_id:

                if (resultCode == RESULT_OK) {
                    String id = intent.getExtras().getString("id");
                    et1.setText(id);

                }

                break;

            case RESOLVE_CONNECTION_REQUEST_CODE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
                if (result.isSuccess()) {
                    acct = result.getSignInAccount();
                    Log.i("구글로그인",""+acct.getEmail());
                    //구글계정 정보가 db에 저장되었는지 확인
                    String data1 = db.json(acct.getEmail().trim());
                    String link = "http://119.207.144.112:8807/searchid.php";
                    PostResponseAsyncTask login =
                            new PostResponseAsyncTask(login.this, link, data1);
                    login.execute(link, data1);

                }
                break;
            default:




        }

    }

    @Override
    public void processFinish(String output) {
        Log.i("로그인결과",""+output);
        if(output.equals("idpossible")){
            Intent intent = new Intent(this,nickname.class);
            intent.putExtra("id",acct.getEmail());
            startActivity(intent);
        }else if(output.equals("idimpossible")||output.equals("success")){
            String data = db.json(acct.getEmail().trim());
            String link = "http://119.207.144.112:8807/googlelogin.php";
            PostResponseAsyncTask login =
                    new PostResponseAsyncTask(login.this, link, data);
            login.execute(link, data);
        }else if(output.equals("loginsuccess")){
            //자동로그인을 위한 아이디저장
            SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("id", et1.getText().toString().trim());
            edit.commit();
            Intent intent = new Intent(login.this, main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else if(output.equals("gloginsuccess")){
            //자동로그인을 위한 아이디저장
            SharedPreferences pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("id", acct.getEmail().trim());
            edit.commit();
            Intent intent = new Intent(login.this, main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else if(output.equals("loginfail")){
            Toast.makeText(getApplicationContext(), "로그인이 실패했습니다.", Toast.LENGTH_LONG).show();
        }

    }
}