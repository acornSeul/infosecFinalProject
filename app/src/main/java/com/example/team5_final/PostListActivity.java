package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team5_final.dto.PostList;
import com.example.team5_final.util.RequestHttpURLConnection;
import com.example.team5_final.util.SaveSharedPreference;

import org.jetbrains.annotations.NotNull;
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
    Button btn_finish;
    Button btn_listQr;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        //from PostManActivity
        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        list_recycler = findViewById(R.id.recycler_postlist);
        btn_start = findViewById(R.id.btn_start);
        btn_finish = findViewById(R.id.btn_finish);
        btn_listQr = findViewById(R.id.btn_listQr);

        Toolbar toolbar = findViewById(R.id.toolbar_postlist);
        ((AppCompatActivity) PostListActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        postData = new ArrayList<>();
        setRecyclerView();
        setData();
    }
    public void onClick(View v){
        List<PostList> list = adapter.getPostList();
        switch (v.getId()){
            // 배송 출발
            case R.id.btn_start:
                String url = "/postlist/updatelist";
                ContentValues values = new ContentValues();
                StateNetwork nt;

                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).getSelected()){
                        if (list.get(i).getState().equals("fin")){
                            Toast.makeText(this, "배송 완료된 물품입니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            postData.get(i).setState("leave");
                            values.put("in_num", list.get(i).getIn_num());
                            values.put("state", "leave");
                            nt = new StateNetwork(url, values, "POST");
                            nt.execute();

                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            // 배송 완료
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

                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            // QR 스캔
            case R.id.btn_listQr:
                Intent intent = new Intent(PostListActivity.this, InvoiceScanActivity.class);
                intent.putExtra("uniqueId", uniqueId);
                startActivity(intent);
        }
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
            case android.R.id.home:
                finish();
                return true;
            case R.id.logout:
                SaveSharedPreference.clearUserName(PostListActivity.this);
                Intent intent = new Intent(PostListActivity.this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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

            return result;
        }
    }
}
