package com.example.test_db;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {
    EditText editText;
    EditText name_text;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        textView = (TextView)findViewById(R.id.txt_output);
        name_text = (EditText) findViewById(R.id.txt_name);
        editText = (EditText)findViewById(R.id.txt_input);
        button = (Button)findViewById(R.id.btn_insert);

    }
    //파라미터 전달 테스트
    public void updateClick(View v){
        String url = "updatetest";
        ContentValues value = new ContentValues();

        value.put("mem_id", editText.getText().toString());
        value.put("name", name_text.getText().toString());

        NetworkTask nt = new NetworkTask(url, value);
        nt.execute();

        selectMem();
    }
    //다시 검색
    public void selectMem(){
        String url = "wheretest";
        ContentValues value = new ContentValues();

        value.put("mem_id", editText.getText().toString());

        NetworkTask nt = new NetworkTask(url, value);
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
