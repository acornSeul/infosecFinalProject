package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class PostListDetailActivity extends AppCompatActivity {
    TextView txt_name;
    TextView txt_phone;
    TextView txt_address;
    TextView txt_zipCode;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_listdetail);

        //from PostRecyclerAdapter
        Intent intent = getIntent();
        String in_num = intent.getStringExtra("in_num");


        Toolbar toolbar = findViewById(R.id.toolbar_listdetail);
        ((AppCompatActivity) PostListDetailActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        txt_name = findViewById(R.id.postlist_name);
        txt_phone = findViewById(R.id.postlist_phone);
        txt_address = findViewById(R.id.postlist_address);
        txt_zipCode = findViewById(R.id.postlist_zipCode);

        String result = castToInNum(in_num);
        getCustomerInfo(result);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getCustomerInfo(String in_num){
        String url = "postlist/viewdetail";
        ContentValues values = new ContentValues();
        values.put("in_num", in_num);

        NormalNetworkTask nt = new NormalNetworkTask(url, values, "GET");
        nt.execute();
    }
    public String castToInNum(String in_num){
        String str = in_num.replace("[ ", "");
        String str2 = str.replace(" ]", "");

        return str2;
    }
    //일반 택배 운송장 조회
    public class NormalNetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public NormalNetworkTask(String url, ContentValues values, String method) {
            this.url = this.url + url;
            this.values = values;
            this.method = method;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String result;
            RequestHttpURLConnection connection = new RequestHttpURLConnection();
            result = connection.request(url, values, method);

            return result;
        }

        @SneakyThrows
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject json = new JSONObject(s);

            txt_name.setText(json.getString("re_name"));
            txt_phone.setText(json.getString("re_phone"));
            txt_address.setText(json.getString("re_address"));
            txt_zipCode.setText("("+json.getString("re_zipCode")+")");
        }
    }
}
