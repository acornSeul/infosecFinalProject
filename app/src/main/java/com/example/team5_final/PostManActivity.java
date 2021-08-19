package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.team5_final.util.SaveSharedPreference;

import org.jetbrains.annotations.NotNull;

public class PostManActivity extends AppCompatActivity {
    Button btn_tagQr;
    Button btn_tagList;
    String uniqueId;
    private long backKeyClickTime = 0;
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_postman);

        //from MainActivity
        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        btn_tagQr = findViewById(R.id.btn_tagQr);
        btn_tagList = findViewById(R.id.btn_tagList);

        Toolbar toolbar = findViewById(R.id.toolbar_postman);
        ((AppCompatActivity) PostManActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                SaveSharedPreference.clearUserName(PostManActivity.this);
                Intent intent = new Intent(PostManActivity.this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyClickTime + 2000){
            backKeyClickTime = System.currentTimeMillis();
            Toast.makeText(PostManActivity.this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000){
            ActivityCompat.finishAffinity(this);
        }
    }
}
