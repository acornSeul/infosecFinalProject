package com.example.team5_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edit_id;
    EditText edit_pw;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_id = (EditText)findViewById(R.id.edit_userId);
        edit_pw = (EditText)findViewById(R.id.edit_pw);
        btn_login = (Button)findViewById(R.id.btn_login);


    }
    public void onLogin(View v){
        if (!edit_id.getText().toString().equals("") && edit_pw.getText().toString().equals("")){
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if (edit_id.getText().toString().equals("")|| edit_pw.getText().toString().equals("")){
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
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
        }

    }

}