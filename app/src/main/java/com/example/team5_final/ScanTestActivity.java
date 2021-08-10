package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.SneakyThrows;

public class ScanTestActivity extends AppCompatActivity {
    TextView textView;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_test);

        String name;
        String address;
        String phone;

        textView = findViewById(R.id.txt_scan_result);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");


        //JSONObject json = new JSONObject(result);


        textView.setText(result);



    }
}
