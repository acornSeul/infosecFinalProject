package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class UserScanActivity extends AppCompatActivity {
    String uniqueId;
    Button btn_review;
    Button btn_refund;
    TextView scan_name;
    TextView scan_phone;
    TextView scan_address;
    TextView scan_zipCode;
    TextView scan_re_name;
    TextView scan_re_phone;
    TextView scan_re_address;
    TextView scan_re_zipCode;
    TextView scan_p_name;
    TextView scan_p_cnt;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userscan);

        btn_review = findViewById(R.id.btn_review);
        btn_refund = findViewById(R.id.btn_refund);
        scan_name = findViewById(R.id.scan_name);
        scan_phone = findViewById(R.id.scan_phone);
        scan_address = findViewById(R.id.scan_address);
        scan_zipCode = findViewById(R.id.scan_zipCode);
        scan_re_name = findViewById(R.id.scan_re_name);
        scan_re_phone = findViewById(R.id.scan_re_phone);
        scan_re_address = findViewById(R.id.scan_re_address);
        scan_re_zipCode = findViewById(R.id.scan_re_zipCode);
        scan_p_name = findViewById(R.id.scan_p_name);
        scan_p_cnt = findViewById(R.id.scan_p_cnt);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        ((AppCompatActivity) UserScanActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        uniqueId = intent.getStringExtra("mem_id");

        JSONObject json = new JSONObject(result);
        setInvoice(json);
        Log.d("seul json result", json.toString());

        //후기 작성 버튼
        btn_review.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                Log.d("json result", json.toString());
                Intent review_intent = new Intent(UserScanActivity.this, ReviewActivity.class);
                review_intent.putExtra("in_num", json.getString("in_num"));
                startActivity(review_intent);
            }
        });
        // 환불 신청 버튼
        btn_refund.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                Intent refund_intent = new Intent(UserScanActivity.this, RefundActivity.class);
                refund_intent.putExtra("in_num", json.getString("in_num"));
                startActivity(refund_intent);
            }
        });
    }
    @SneakyThrows
    public void setInvoice(JSONObject json){
        scan_name.setText(json.getString("name"));
        scan_phone.setText(json.getString("phone"));
        scan_address.setText(json.getString("address"));
        scan_zipCode.setText(json.getString("zipCode"));
        scan_re_name.setText(json.getString("re_name"));
        scan_re_phone.setText(json.getString("re_phone"));
        scan_re_address.setText(json.getString("re_address"));
        scan_re_zipCode.setText(json.getString("re_zipCode"));
        scan_p_name.setText(json.getString("p_name"));
        scan_p_cnt.setText(json.getString("p_cnt"));
    }
}
