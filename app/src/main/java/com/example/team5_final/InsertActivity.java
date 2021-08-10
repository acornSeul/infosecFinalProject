package com.example.team5_final;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team5_final.network.RequestHttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class InsertActivity extends AppCompatActivity {
    EditText input_userId;
    EditText input_pw;
    EditText input_name;
    EditText input_phone;
    EditText input_zip;
    EditText input_birth;
    EditText input_type;
    EditText input_addr;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        input_userId = (EditText)findViewById(R.id.input_id);
        input_pw = (EditText)findViewById(R.id.input_pw);
        input_name = (EditText)findViewById(R.id.input_name);
        input_phone = (EditText)findViewById(R.id.input_phone);
        input_zip = (EditText)findViewById(R.id.input_zip);
        input_birth = (EditText)findViewById(R.id.input_birth);
        input_type = (EditText)findViewById(R.id.input_type);
        input_addr = (EditText)findViewById(R.id.input_addr);
        btn_insert = (Button)findViewById(R.id.btn_insert);

    }
    //member 정보 insert
    public void insertClick(View v) throws JSONException {
        String url = "inserttest";

        ContentValues values = new ContentValues();
        JSONObject json = new JSONObject();
        json.put("userId", input_userId.getText().toString());
        json.put("password", input_pw.getText().toString());
        json.put("name", input_name.getText().toString());
        json.put("phone", input_phone.getText().toString());
        json.put("zipCode", input_zip.getText().toString());
        json.put("birth", input_birth.getText().toString());
        json.put("type", input_type.getText().toString());
        json.put("addr", input_addr.getText().toString());

        values.put("member", json.toString());

        NetworkTask nt = new NetworkTask(url, values);
        nt.execute();
    }

    //웹서버 통신
    public class NetworkTask extends AsyncTask<Void, Void, String> {
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
            result = connection.request(url, values, "POST");

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(InsertActivity.this, "전송 성공!", Toast.LENGTH_SHORT).show();
        }
    }
}
