package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import lombok.SneakyThrows;

public class PostScanActivity extends AppCompatActivity {
    TextView scan_name;
    TextView scan_phone;
    TextView scan_address;
    TextView scan_zipCode;
    Button btn_qrscan;
    Button btn_list;
    String uniqueId;
    String result;
    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postscan);

        scan_name = findViewById(R.id.post_scanName);
        scan_phone = findViewById(R.id.post_scanPhone);
        scan_address = findViewById(R.id.post_scanAddress);
        scan_zipCode = findViewById(R.id.post_scanZipCode);
        btn_qrscan = findViewById(R.id.btn_tagQr);
        btn_list = findViewById(R.id.btn_tagList);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        ((AppCompatActivity) PostScanActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        //from InvoiceScanActivity
        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("tag_id");
        result = intent.getStringExtra("result");
        JSONObject json = new JSONObject(result);

        setUserInfo(json);
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_tagQr:
                Intent qr_intent = new Intent(PostScanActivity.this, InvoiceScanActivity.class);
                qr_intent.putExtra("uniqueId", uniqueId);
                startActivity(qr_intent);
                break;
            case R.id.btn_tagList:
                Intent list_intent = new Intent(PostScanActivity.this, PostListActivity.class);
                list_intent.putExtra("uniqueId", uniqueId);
                startActivity(list_intent);
                break;
        }
    }
    @SneakyThrows
    public void setUserInfo(JSONObject json){
        scan_name.setText(json.getString("re_name"));
        scan_phone.setText(json.getString("re_phone"));
        scan_address.setText(json.getString("re_address"));
        scan_zipCode.setText(json.getString("re_zipCode"));
    }
}
