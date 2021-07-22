package com.example.team5_final;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_db.qrcode.MainActivityQR;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText edit_id;
    EditText edit_pw;
    Button btn_login;
    TextView txt_alert;
    TextView txt_alertpw;
    RadioGroup group;
    RadioButton radio_c;
    RadioButton radio_t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_id = findViewById(R.id.edit_userId);
        edit_pw = findViewById(R.id.edit_pw);
        btn_login = findViewById(R.id.btn_login);
        txt_alert = findViewById(R.id.txt_alert);
        txt_alertpw = findViewById(R.id.txt_alertpw);
        group = findViewById(R.id.radioGroup);
        group.clearCheck();

    }
    // 로그인 구현
    public void onLogin(View v){
        String loginId = edit_id.getText().toString();
        String lgoinPw = edit_pw.getText().toString();
        String type = "";
        int rb = group.getCheckedRadioButtonId();

        switch (rb){
            case R.id.radio_c:
                type = "C";
                break;
            case R.id.radio_t:
                type = "T";
                break;
            default:
                Toast.makeText(this, "고객 유형을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }

        if (!type.equals("")){
            //비밀번호만 공란인 경우
            if (!loginId.equals("") && lgoinPw.equals("")){
                txt_alert.setText("");
                txt_alertpw.setTextSize(Dimension.SP,12);
                txt_alertpw.setText("비밀번호를 입력해주세요.");
            }
            //아이디가 공란인 경우
            else if (loginId.equals("") && lgoinPw.equals("")){
                txt_alert.setTextSize(Dimension.SP,12);
                txt_alert.setText("아이디를 입력해주세요.");
            }
            //둘다 공란이 아닌 경우
            else if (!loginId.equals("") && !lgoinPw.equals("")){
                String url = "login";
                ContentValues values = new ContentValues();

                values.put("in_id", loginId);
                values.put("in_pw", lgoinPw);
                values.put("in_type", type);

                NetworkTask nt = new NetworkTask(url, values);
                nt.execute();
            }
        }

    }
    //qr_test
    public void onNext(View v){
        Intent intent_qr = new Intent(MainActivity.this, MainActivityQR.class);
        startActivity(intent_qr);
    }

    //웹서버 통신
    public class NetworkTask extends AsyncTask<Void, Void, String>{
        private String url = "http://192.168.0.3:8080/";
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = this.url + url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result;
            RequestHttpURLConnection connection = new RequestHttpURLConnection();
            result = connection.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject json = new JSONObject(s);
                String result = json.getString("result");

                /*
                 * result : x (아이디는 맞으나 비밀번호가 일치 하지 않음)
                 *          n (아이디도 존재하지 않음)
                 *          o (로그인 성공)
                 */
                if(result.equals("x") || result.equals("n")){
                    edit_id.setText("");
                    edit_pw.setText("");
                    txt_alertpw.setText("");
                    txt_alert.setTextSize(Dimension.SP,12);
                    txt_alert.setText("아이디가 존재하지 않거나, 잘못된 비밀번호 입니다.");
                }
                else if (result.equals("o")){
                    Intent intent = new Intent(MainActivity.this, AfterLoginActivity.class);
                    intent.putExtra("name", json.getString("name"));

                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}