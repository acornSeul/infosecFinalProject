package com.example.team5_final.fragment;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team5_final.R;
import com.example.team5_final.RecyclerAdapter;
import com.example.team5_final.dto.UserInvoice;
import com.example.team5_final.util.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class Fragment_list extends Fragment {
    private final static String FRAGMENT_TAG = "frag_list";
    String uniqueId;
    private RecyclerView recyclerView = null;
    private RecyclerAdapter recyclerAdapter = null;
    private List<UserInvoice> dataList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_list1, container, false);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar3);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //from AfterLoginActivity
        Bundle extra = this.getArguments();
        if (extra != null){
            extra = getArguments();
            uniqueId = extra.getString("uniqueId");
        }

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        dataList = new ArrayList<>();

        setRecyclerView();
        refreshAdapter(uniqueId);

        return view;
    }
    public void refreshAdapter(String uniqueId){
        if(getActivity() != null){
            //리스트 불러오기
            String list_url = "invoice/list";
            ContentValues list_values = new ContentValues();
            list_values.put("mem_id", uniqueId);

            NetworkTask list_nt = new NetworkTask(list_url, list_values, "GET");
            list_nt.execute();
        }
    }
    private void setRecyclerView(){
        recyclerAdapter = new RecyclerAdapter(getContext(), R.layout.item_recycler_view, dataList, uniqueId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);

    }
    //신청 목록 리스트 통신
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
                dataList.add(new UserInvoice(json.getString("in_num"), json.getString("name"), json.getString("re_name"), json.getString("encryptYn")));
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }
}