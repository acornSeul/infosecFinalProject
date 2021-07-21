package com.example.test_db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.result);
        editText = (EditText)findViewById(R.id.edit_id);
        button2 =  (Button)findViewById(R.id.btn_next2);
        button3 = (Button)findViewById(R.id.btn_next3);

    }
    //where test 버튼 클릭시
    public void onClick(View v){
        String url = "wheretest";
        ContentValues value = new ContentValues();
        value.put("mem_id", editText.getText().toString());

        NetworkTask nt = new NetworkTask(url, value);
        nt.execute();
    }

    //다음으로 화면 이동
    public void nextClick(View v) throws JSONException {
        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
    }

    //다음2로 화면 이동
    public void nextClick2(View v){
        Intent intent2 = new Intent(this, InsertActivity.class);
        startActivity(intent2);
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
            textView.setText(s);
        }

    }

}