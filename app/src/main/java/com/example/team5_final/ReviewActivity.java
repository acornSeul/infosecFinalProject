package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team5_final.util.RequestHttpURLConnection;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class ReviewActivity extends AppCompatActivity {
    TextView txt_purchaseDate;
    TextView txt_producer;
    TextView txt_orderNum;
    RatingBar ratingBar;
    EditText edit_review;
    String in_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        txt_purchaseDate = findViewById(R.id.txt_purchaseDate);
        txt_producer = findViewById(R.id.txt_producer);
        txt_orderNum = findViewById(R.id.txt_orderNum);
        ratingBar = findViewById(R.id.review_rating);
        edit_review = findViewById(R.id.edit_review);
        ratingBar.setRating(3);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

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
