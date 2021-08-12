package com.example.team5_final;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//배송 목록 화면
public class PostListActivity extends AppCompatActivity {
    RecyclerView list_recycler;
    PostRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        list_recycler = findViewById(R.id.recycler_postlist);
        list_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new PostRecyclerAdapter();
        setData();
        list_recycler.setAdapter(adapter);
    }
    public void setData(){

    }
}
