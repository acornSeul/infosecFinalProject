package com.example.team5_final;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AfterApplyActivity extends AppCompatActivity {
    Button btn_confirm_apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterapply);

        btn_confirm_apply = findViewById(R.id.btn_confirm_apply);

        btn_confirm_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
