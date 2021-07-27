package com.example.team5_final;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                txt_alertpw.setText("");
                txt_alert.setText("");

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
    //qr_test, AWS test
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_qr:
                Intent intent_qr = new Intent(MainActivity.this, MainActivityQR.class);
                startActivity(intent_qr);
                break;
            case R.id.btn_test:
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
                break;
        }

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
            Log.d("test2", "seul test2 : " + s);

            try {
                JSONObject json = new JSONObject(s);
                String result = json.getString("result");
                int cnt = json.getInt("cnt");

/*               * result : x (아이디는 맞으나 비밀번호가 일치 하지 않음)
                 *          n (아이디도 존재하지 않음)
                 *          o (로그인 성공)  */
                if (cnt < 5){
                    if(result.equals("x")){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("비밀번호 오류입니다. \n 비밀번호 오류 5회 초과시 계정이 잠깁니다.( " + cnt + " / 5 )")
                                .setPositiveButton("확인", null)
                                .show();

                    }
                    else if (result.equals("n")){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("존재하지 않는 아이디입니다.")
                                .setPositiveButton("확인", null)
                                .show();
                    }
                    else if (result.equals("o")){
                        Intent intent = new Intent(MainActivity.this, AfterLoginActivity.class);
                        intent.putExtra("name", json.getString("name"));
                        intent.putExtra("uniqueId", json.getString("uniqueId"));

                        startActivity(intent);
                    }
                }
                else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage("비밀번호 오류 횟수 초과로 인해 계정이 잠깁니다.\n관리자에게 문의해주세요.")
                            .setPositiveButton("확인", null)
                            .show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    // url 통신
    public class NetworkTask2 extends AsyncTask<Void, Void, String>{
        private String url;
        //private ContentValues values;
        private String method;

        public NetworkTask2(String url, String method) {
            this.url = url;
            //this.values = values;
            this.method = method;
        }

        @Override
        protected String doInBackground(Void... voids) {
            BufferedReader in = null;

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection)obj.openConnection();

                con.setRequestMethod(method);

                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                String line;
                while((line = in.readLine()) != null) {
                    Log.d("test", "seul test : " + line);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    // url 통신
    public class NetworkTask3 extends AsyncTask<Void, Void, String>{
        private String url;
        private ContentValues values;

        public NetworkTask3(String url, ContentValues values) {
            this.url = url;
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
            Log.d("test", "seul result : " + s);
        }
    }
}