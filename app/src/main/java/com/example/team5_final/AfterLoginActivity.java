package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AfterLoginActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afrerlogin);

        textView = findViewById(R.id.txt_welcome);

        Intent intent = getIntent();
        textView.setText(intent.getStringExtra("name") + "님 안녕하세요.");
    }
}
