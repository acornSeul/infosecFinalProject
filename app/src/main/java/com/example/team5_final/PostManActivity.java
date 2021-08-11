package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PostManActivity extends AppCompatActivity {
    Button btn_tag_qr;
    String uniqueId;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_postman);

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        btn_tag_qr = findViewById(R.id.btn_tag_qr);

        btn_tag_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostManActivity.this, InvoiceScanActivity.class);
                intent.putExtra("uniqueId", uniqueId);
                startActivity(intent);
            }
        });
    }
}
