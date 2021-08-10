package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class ApplyInfoActivity extends AppCompatActivity {
    String in_num;
    TextView txt_encryptYn;
    TextView txt_createTime;
    TextView txt_name;
    TextView txt_phone;
    TextView txt_address;
    TextView txt_zipCode;
    TextView txt_re_name;
    TextView txt_re_phone;
    TextView txt_re_address;
    TextView txt_re_zipCode;
    TextView txt_p_name;
    TextView txt_p_cnt;
    TextView txt_p_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyinfo);

        txt_encryptYn = findViewById(R.id.txt_encryptYn);
        txt_createTime = findViewById(R.id.txt_createTime);
        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        txt_address = findViewById(R.id.txt_address);
        txt_zipCode = findViewById(R.id.txt_zipCode);
        txt_re_name = findViewById(R.id.txt_re_name);
        txt_re_address = findViewById(R.id.txt_re_address);
        txt_re_phone = findViewById(R.id.txt_re_phone);
        txt_re_zipCode = findViewById(R.id.txt_re_zipCode);
        txt_p_name = findViewById(R.id.txt_p_name);
        txt_p_cnt = findViewById(R.id.txt_p_cnt);
        txt_p_price = findViewById(R.id.txt_p_price);


        Intent intent = getIntent();
        in_num = intent.getStringExtra("in_num");
        invoice_linkNetwork();

    }

    public void invoice_linkNetwork() {
        String i_url = "invoice/detail";
        String p_url = "invoice/product";
        ContentValues values = new ContentValues();
        values.put("in_num", in_num);

        NetworkTask detail_nt = new NetworkTask(i_url, p_url, values, values);
        detail_nt.execute();
    }

    //invoice detail 정보
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
            result = connection.request(url, values, "GET");
            result2 = connection.request(url2, values2, "GET");

            JSONObject i_json = new JSONObject(result);
            JSONObject p_json = new JSONObject(result2);

            i_json.put("p_name", p_json.getString("p_name"));
            i_json.put("p_cnt", String.valueOf(p_json.getInt("p_cnt")));
            i_json.put("p_price", String.valueOf(p_json.getString("p_price")));

            return i_json.toString();
        }

        @SneakyThrows
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject json = new JSONObject(s);

            Log.d("json result", s);

            if (json.getString("encryptYn").equals("Y")) {
                txt_encryptYn.setText("[안전 택배]");
            } else {
                txt_encryptYn.setText("[일반 택배]");
            }
            txt_createTime.setText(json.getString("createDatetime"));
            txt_name.setText(json.getString("name"));
            txt_phone.setText(json.getString("phone"));
            txt_address.setText(json.getString("address"));
            txt_zipCode.setText(json.getString("zipCode"));
            txt_re_name.setText(json.getString("re_name"));
            txt_re_address.setText(json.getString("re_address"));
            txt_re_phone.setText(json.getString("re_phone"));
            txt_re_zipCode.setText(json.getString("re_zipCode"));
            txt_p_name.setText(json.getString("p_name"));
            txt_p_cnt.setText(json.getString("p_cnt"));
            txt_p_price.setText(json.getString("p_price"));
        }
    }
}
