package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.team5_final.fragment.AfterLoginActivity;
import com.example.team5_final.util.RequestHttpURLConnection;
import com.example.team5_final.util.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {
    private long backKeyClickTime = 0;
    EditText edit_id;
    EditText edit_pw;
    Button btn_login;
    TextView txt_alert;
    TextView txt_alertpw;
    CheckBox chk_auto;
    RadioGroup group;
    RadioButton radio_c;
    RadioButton radio_t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SaveSharedPreference.getUserName(MainActivity.this).length() != 0){
            if (Integer.parseInt(SaveSharedPreference.getUserName(MainActivity.this)) >= 7000){
                Intent login_intent = new Intent(MainActivity.this, PostManActivity.class);
                login_intent.putExtra("uniqueId", SaveSharedPreference.getUserName(MainActivity.this));
                startActivity(login_intent);

            }else{
                Intent login_intent = new Intent(MainActivity.this, AfterLoginActivity.class);
                login_intent.putExtra("uniqueId", SaveSharedPreference.getUserName(MainActivity.this));
                startActivity(login_intent);
            }
        }

        edit_id = findViewById(R.id.edit_userId);
        edit_pw = findViewById(R.id.edit_pw);
        btn_login = findViewById(R.id.btn_login);
        txt_alert = findViewById(R.id.txt_alert);
        txt_alertpw = findViewById(R.id.txt_alertpw);
        chk_auto = findViewById(R.id.chk_auto);
        group = findViewById(R.id.radioGroup);
        group.clearCheck();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        edit_id.setText("");
        edit_pw.setText("");
    }
    // ????????? ??????
    @SneakyThrows
    public void onLogin(View v){
        String loginId = edit_id.getText().toString();
        String loginPw = edit_pw.getText().toString();
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
                Toast.makeText(this, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
        }

        if (!type.equals("")){
            //??????????????? ????????? ??????
            if (!loginId.equals("") && loginPw.equals("")){
                txt_alert.setText("");
                txt_alertpw.setTextSize(Dimension.SP,12);
                txt_alertpw.setText("??????????????? ??????????????????.");
            }
            //???????????? ????????? ??????
            else if (loginId.equals("") && loginPw.equals("")){
                txt_alert.setTextSize(Dimension.SP,12);
                txt_alert.setText("???????????? ??????????????????.");
            }
            //?????? ????????? ?????? ??????
            else if (!loginId.equals("") && !loginPw.equals("")){
                txt_alertpw.setText("");
                txt_alert.setText("");

                ContentValues login_values = new ContentValues();
                ContentValues cnt_values = new ContentValues();

                login_values.put("in_id", loginId);
                login_values.put("in_pw", loginPw);
                login_values.put("in_type", type);
                //cnt ????????????
                cnt_values.put("in_id", loginId);
                cnt_values.put("in_type", type);

                NetworkTask login_nt = new NetworkTask("login", "login/getcnt", login_values, cnt_values);
                login_nt.execute();
            }
        }
    }
    //???????????? ??? ??????
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyClickTime + 2000){
            backKeyClickTime = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, "???????????? ????????? ?????? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000){
            ActivityCompat.finishAffinity(this);
        }
    }
    //????????? ?????? ??????
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private String url2 = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private ContentValues values2;

        public NetworkTask(String url, String url2, ContentValues values, ContentValues values2) {
            this.url = this.url + url;
            this.url2 = this.url2 + url2;
            this.values = values;
            this.values2 = values2;

        }
        @SneakyThrows
        @Override
        protected String doInBackground(Void... voids) {
            String result;
            String result2;
            RequestHttpURLConnection connection = new RequestHttpURLConnection();
            result = connection.request(url, values, "POST");
            result2 = connection.request(url2, values2, "GET");

            JSONObject result_json = new JSONObject(result);
            JSONObject new_json = new JSONObject();
            if (!result2.equals("null")){
                JSONObject result2_json = new JSONObject(result2);
                new_json.put("loginCnt", result2_json.getString("loginCnt"));
            }
            new_json.put("response", result_json.getString("response"));
            new_json.put("mem_id", result_json.getString("mem_id"));
            new_json.put("tag_id", result_json.getString("tag_id"));

            return new_json.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject total_result = null;
            try {
                total_result = new JSONObject(s);
            } catch (JSONException e) {
            }

            if(total_result.length() == 3){
                try {
                    loginEvent_noId(total_result.getString("response"), total_result.getString("mem_id"),total_result.getString("tag_id"));
                } catch (JSONException e) {
                }
            }
            else{
                try {
                    loginEvent_existId(total_result.getString("response"),Integer.parseInt(total_result.getString("loginCnt")),total_result.getString("mem_id"), total_result.getString("tag_id"));
                } catch (JSONException e) {
                }
            }
        }
    }
    //????????? ?????? - ???????????? ?????? ??????
    public void loginEvent_existId (String result, int cnt, String mem_id, String tag_id){
        if (cnt < 5){
            if(result.equals("x")){
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("???????????? ???????????????. \n ???????????? ?????? 5??? ????????? ????????? ????????????.( " + cnt + " / 5 )")
                        .setPositiveButton("??????", null)
                        .show();
            }
            else if (result.equals("o")){
                //????????? ?????????
                if (!mem_id.equals("")){
                    if (chk_auto.isChecked() == true) {
                        SaveSharedPreference.setUserName(MainActivity.this, mem_id);
                    }
                    Intent intent = new Intent(MainActivity.this, AfterLoginActivity.class);
                    intent.putExtra("uniqueId", mem_id);
                    startActivity(intent);
                }
                //???????????? ?????????
                else{
                    if (chk_auto.isChecked() == true) {
                        SaveSharedPreference.setUserName(MainActivity.this, tag_id);
                    }
                    Intent intent = new Intent(MainActivity.this, PostManActivity.class);
                    intent.putExtra("uniqueId", tag_id);
                    startActivity(intent);
                }
            }
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("???????????? ?????? ?????? ????????? ?????? ????????? ????????????.\n??????????????? ??????????????????.")
                    .setPositiveButton("??????", null)
                    .show();
        }
    }
    public void loginEvent_noId (String result, String mem_id, String tag_id){
        if (result.equals("n")){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("???????????? ?????? ??????????????????.")
                    .setPositiveButton("??????", null)
                    .show();
        }
    }
}
