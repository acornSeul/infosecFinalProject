package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team5_final.dto.PostList;
import com.example.team5_final.util.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

//배송 목록 화면
public class PostListActivity extends AppCompatActivity {
    RecyclerView list_recycler = null;
    PostRecyclerAdapter adapter = null;
    ArrayList<PostList> postData = null;
    String uniqueId;
    Button btn_start;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        //from PostManActivity
        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        list_recycler = findViewById(R.id.recycler_postlist);
        btn_start = findViewById(R.id.btn_start);

        postData = new ArrayList<>();
        setRecyclerView();
        setData();
    }
    public void onClick(View v){
        List<PostList> list = adapter.getPostList();
        switch (v.getId()){
            case R.id.btn_start:
                String url = "/postlist/updatelist";
                ContentValues values = new ContentValues();
                StateNetwork nt;

                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).getSelected()){
                        postData.get(i).setState("leave");
                        values.put("in_num", list.get(i).getIn_num());
                        values.put("state", "leave");
                        nt = new StateNetwork(url, values, "POST");
                        nt.execute();

                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.btn_finish:
                String fin_url = "/postlist/updatelist";
                ContentValues fin_values = new ContentValues();
                StateNetwork fin_nt;

                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).getSelected()){
                        postData.get(i).setState("fin");
                        fin_values.put("in_num", list.get(i).getIn_num());
                        fin_values.put("state", "fin");
                        fin_nt = new StateNetwork(fin_url, fin_values, "POST");
                        fin_nt.execute();
                        Log.d("finish in method", "finish!");
                        adapter.notifyDataSetChanged();
                    }
                }
        }
    }
    public void setData(){
        String url = "postlist/viewlist";
        ContentValues values = new ContentValues();
        values.put("tag_id", uniqueId);

        NetworkTask nt = new NetworkTask(url, values, "GET");
        nt.execute();
    }
    private  void setRecyclerView(){
        adapter = new PostRecyclerAdapter(postData);
        list_recycler.setLayoutManager(new LinearLayoutManager(PostListActivity.this));
        list_recycler.setHasFixedSize(true);
        list_recycler.setAdapter(adapter);
    }
    // 배송 목록 조회
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public NetworkTask(String url, ContentValues values, String method) {
            this.url = this.url + url;
            this.values = values;
            this.method = method;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String result;
            RequestHttpURLConnection connection = new RequestHttpURLConnection();
            result = connection.request(url, values, method);

            return result;
        }

        @SneakyThrows
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = new JSONObject(jsonArray.get(i).toString());
                postData.add(new PostList(json.getString("in_num"), json.getString("address"), json.getString("name"), json.getString("state"), json.getString("tag_id")));
                adapter.notifyDataSetChanged();
            }
        }
    }
    // 배송 상태 업데이트
    public class StateNetwork extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public StateNetwork(String url, ContentValues values, String method) {
            this.url = this.url + url;
            this.values = values;
            this.method = method;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String result;
            RequestHttpURLConnection connection = new RequestHttpURLConnection();
            result = connection.request(url, values, method);

            Log.d("finish in network", "network finish!!");
            return result;
        }
    }
}
