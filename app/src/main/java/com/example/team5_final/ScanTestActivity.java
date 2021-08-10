package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class ScanTestActivity extends AppCompatActivity {
    TextView textView;
    String uniqueId;

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
        uniqueId = intent.getStringExtra("mem_id");

        JSONObject json = new JSONObject(result);

        if (uniqueId.equals(json.getString("mem_id"))){
            Toast.makeText(this,"스캔 성공", Toast.LENGTH_LONG).show();
            textView.setText(result);
        }
        else{
            Toast.makeText(this,"스캔 실패", Toast.LENGTH_SHORT).show();
        }



    }
}
