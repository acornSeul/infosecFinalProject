package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PostManActivity extends AppCompatActivity {
    Button btn_tagQr;
    Button btn_tagList;
    String uniqueId;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_postman);

        //from MainActivity
        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        btn_tagQr = findViewById(R.id.btn_tagQr);
        btn_tagList = findViewById(R.id.btn_tagList);

        btn_tagQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostManActivity.this, InvoiceScanActivity.class);
                intent.putExtra("uniqueId", uniqueId);
                startActivity(intent);
            }
        });

        btn_tagList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostManActivity.this, PostListActivity.class);
                intent.putExtra("uniqueId", uniqueId);
                startActivity(intent);
            }
        });
    }
}
