package com.example.doo88.pocketv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class join extends AppCompatActivity implements AsyncResponse {
    private EditText et1, et2, et3, et4;
    private TextView tv1, tv2, tv3;
    private Button btn1;
    dbConnect db = new dbConnect();
    private boolean idv, pwv, namev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //변수선언
        et1 = (EditText) findViewById(R.id.edittext1);
        et2 = (EditText) findViewById(R.id.edittext2);
        et3 = (EditText) findViewById(R.id.edittext3);
        et4 = (EditText) findViewById(R.id.edittext4);
        tv1 = (TextView) findViewById(R.id.textview1);
        tv2 = (TextView) findViewById(R.id.textview2);
        tv3 = (TextView) findViewById(R.id.textview3);
        btn1 = (Button) findViewById(R.id.button1);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);

        //아이디,비밀번호 초기값
        idv = false;
        pwv = false;
        namev = false;
        //아이디 형식체크
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et1.length() > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(et1.getText().toString()).matches()) {
                } else {
                    tv1.setVisibility(View.VISIBLE);
                    tv1.setTextColor(Color.parseColor("#FF0000"));
                    tv1.setText("아이디형식이 옳바르지 않습니다.");
                }
            }
        });

        //아이디 중복체크
        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (et1.length() > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(et1.getText().toString()).matches()) {
                    String data = db.json(et1.getText().toString());
                    String link = "http://119.207.144.112:8807/searchid.php";
                    PostResponseAsyncTask searchid =
                            new PostResponseAsyncTask(join.this, link, data);
                    searchid.execute(link, data);
                }
            }
        });

        //이름 중복체크
        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (et2.length() > 0) {
                    String data = db.json(et2.getText().toString());
                    String link = "http://119.207.144.112:8807/searchname.php";
                    PostResponseAsyncTask searchid =
                            new PostResponseAsyncTask(join.this, link, data);
                    searchid.execute(link, data);
                }
            }
        });

        //비밀번호 체크
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et3.getText().toString().equals(et4.getText().toString())) {
                    pwv = true;
                    tv3.setVisibility(View.VISIBLE);
                    tv3.setTextColor(Color.parseColor("#008000"));
                    tv3.setText("비밀번호가 일치합니다.");
                } else {
                    pwv = false;
                    tv3.setVisibility(View.VISIBLE);
                    tv3.setTextColor(Color.parseColor("#FF0000"));
                    tv3.setText("비밀번호가 일치하지 않습니다.");
                }
            }
        });

        //회원가입버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idv == true && pwv == true && namev == true) {
                    String data = db.json(et1.getText().toString().trim(), et2.getText().toString().trim(), et3.getText().toString().trim(), "A");
                    String link = "http://119.207.144.112:8807/join.php";
                    PostResponseAsyncTask searchid =
                            new PostResponseAsyncTask(join.this, link, data);
                    searchid.execute(link, data);
                } else if (idv == false) {
                    Toast.makeText(getApplicationContext(), "사용할 수 없는 아이디입니다..", Toast.LENGTH_SHORT).show();
                } else if (pwv == false) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (namev == false) {
                    Toast.makeText(getApplicationContext(), "사용할수 없는 이름입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void processFinish(String output) {
        Log.i("회원가입결과", "" + output);
        if (output.equals("idimpossible")) {
            idv = false;
            tv1.setVisibility(View.VISIBLE);
            tv1.setTextColor(Color.parseColor("#FF0000"));
            tv1.setText("사용할 수 없는 아이디입니다.");
        } else if (output.equals("idpossible")) {
            idv = true;
            tv1.setVisibility(View.VISIBLE);
            tv1.setTextColor(Color.parseColor("#008000"));
            tv1.setText("사용 가능한 아이디입니다.");
        }else if (output.equals("nameimpossible")) {
            namev = false;
            tv2.setVisibility(View.VISIBLE);
            tv2.setTextColor(Color.parseColor("#FF0000"));
            tv2.setText("사용할 수 없는 이름입니다.");
        } else if (output.equals("namepossible")) {
            namev = true;
            tv2.setVisibility(View.VISIBLE);
            tv2.setTextColor(Color.parseColor("#008000"));
            tv2.setText("사용 가능한 이름입니다.");
        } else if (output.equals("success")) {
            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("id", et1.getText().toString());
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
