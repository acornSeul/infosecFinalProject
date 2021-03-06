package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.team5_final.util.RequestHttpURLConnection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import lombok.SneakyThrows;

public class RefundActivity extends AppCompatActivity {
    TextView txt_ref_orNum;
    TextView txt_ref_pName;
    TextView txt_ref_findDate;
    TextView txt_ref_name;
    TextView txt_ref_phone;
    TextView txt_ref_address;
    TextView txt_ref_zipCode;
    String in_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        //from UserScanActivity
        Intent intent = getIntent();
        in_num = intent.getStringExtra("in_num");

        Log.d("seul in_num", in_num);

        txt_ref_orNum = findViewById(R.id.txt_ref_orNum);
        txt_ref_pName = findViewById(R.id.txt_ref_pName);
        txt_ref_findDate = findViewById(R.id.txt_ref_findate);
        txt_ref_name = findViewById(R.id.txt_ref_name);
        txt_ref_phone = findViewById(R.id.txt_ref_phone);
        txt_ref_address = findViewById(R.id.txt_ref_address);
        txt_ref_zipCode = findViewById(R.id.txt_ref_zipCode);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        ((AppCompatActivity) RefundActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        String url = "refund";
        ContentValues values = new ContentValues();
        values.put("i.in_num", in_num);

        NetworkTask nt = new NetworkTask(url, values, "GET");
        nt.execute();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // ???????????? ??? ????????? setting
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public NetworkTask(String url, ContentValues values, String method) {
            this.url = this.url + url;
            this.values = values;
            this.method = method;
        }

        @SneakyThrows
        @Override
        protected String doInBackground(Void... voids) {
            String result;
            String result2;
            RequestHttpURLConnection connection = new RequestHttpURLConnection();
            result = connection.request(url, values, method);

            return result;
        }

        @SneakyThrows
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject json = new JSONObject(s);

            txt_ref_orNum.setText(json.getString("order_num"));
            txt_ref_pName.setText(json.getString("p_name"));
            txt_ref_findDate.setText(json.getString("fin_datetime"));
            txt_ref_name.setText(json.getString("name"));
            txt_ref_phone.setText(json.getString("phone"));
            txt_ref_address.setText(json.getString("address"));
            txt_ref_zipCode.setText(json.getString("zipCode"));
        }
    }
}
