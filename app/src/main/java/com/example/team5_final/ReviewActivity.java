package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.team5_final.util.RequestHttpURLConnection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import lombok.SneakyThrows;

public class ReviewActivity extends AppCompatActivity {
    TextView txt_purchaseDate;
    TextView txt_producer;
    TextView txt_orderNum;
    EditText edit_review;
    Button btn_reveiwSelect1;
    String in_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        txt_purchaseDate = findViewById(R.id.txt_purchaseDate);
        txt_producer = findViewById(R.id.txt_producer);
        txt_orderNum = findViewById(R.id.txt_orderNum);
        edit_review = findViewById(R.id.edit_review);
        btn_reveiwSelect1 = findViewById(R.id.btn_reveiwSelect1);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        ((AppCompatActivity) ReviewActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        btn_reveiwSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_reveiwSelect1.isSelected()){
                    btn_reveiwSelect1.setTextColor(Color.parseColor("#9370DB"));
                    btn_reveiwSelect1.setSelected(false);

                }
                else{
                    btn_reveiwSelect1.setTextColor(Color.parseColor("#F8F8FF"));
                    btn_reveiwSelect1.setSelected(true);
                }
            }
        });
        //from UserScanActivity
        Intent intent = getIntent();
        in_num = intent.getStringExtra("in_num");

        String review_url = "review";
        ContentValues review_values = new ContentValues();
        review_values.put("in_num", in_num);

        NetworkTask nt = new NetworkTask(review_url, review_values, "GET");
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
    //onclick 이벤트
    // 후기작성 기본정보 setting
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

            txt_purchaseDate.setText(json.getString("purchaseDate"));
            txt_orderNum.setText(json.getString("order_num"));
            txt_producer.setText(json.getString("name"));
        }
    }
}
