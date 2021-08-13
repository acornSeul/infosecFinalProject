package com.example.team5_final;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team5_final.util.RequestHttpURLConnection;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class InvoiceScanActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    String uniqueId;
    String cnt_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경
        qrScan.initiateScan();
    }
    @SneakyThrows
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        JSONObject result_json;

        if(result != null) {
            if(result.getContents() == null) {
                finish();
            } else {
                result_json = new JSONObject(result.getContents());
                String tag_id = result_json.getString("tag_id");
                String cnt_result = getCntForList(result_json.getString("in_num"), uniqueId);

                //택배 기사가 스캔 시
                if (Integer.parseInt(uniqueId) > 7000){
                    // 택배기사가 할당 되지 않은 물량 스캔 시
                    if(cnt_result.equals("\"y\"")){
                        //운송장 update, 리스트 insert
                        insertPostList(result_json.getString("in_num"), uniqueId, result_json.getString("re_address"), result_json.getString("re_name"), result_json.getString("state"));
                        Toast.makeText(InvoiceScanActivity.this, "추가 완료 했습니다.",Toast.LENGTH_SHORT).show();
                        Intent scan_intent = new Intent(InvoiceScanActivity.this, InvoiceScanActivity.class);
                        scan_intent.putExtra("uniqueId", uniqueId);
                        startActivity(scan_intent);
                    }
                    else{
                        // 본인 물량 스캔 시
                        if (cnt_result.equals("\"o\"")){
                            Intent post_intent = new Intent(InvoiceScanActivity.this, PostScanActivity.class);
                            post_intent.putExtra("result", result.getContents());
                            post_intent.putExtra("tag_id", uniqueId);
                            startActivity(post_intent);
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceScanActivity.this)
                                    .setTitle("스캔 오류")
                                    .setMessage("해당 사용자의 배송 물품이 아닙니다.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                            builder.show();
                        }
                    }

                }else{
                    //자신의 정보 스캔 시
                    if (uniqueId.equals(result_json.getString("mem_id"))) {
                        Intent intent = new Intent(InvoiceScanActivity.this, UserScanActivity.class);
                        intent.putExtra("result", result.getContents());
                        intent.putExtra("mem_id", uniqueId);
                        startActivity(intent);
                    }
                    //자신의 정보가 아닌 다른 사람 QR 코드를 스캔할 경우
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceScanActivity.this)
                                .setTitle("접근 오류")
                                .setMessage("잘못된 접근입니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        builder.show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    // 리스트에 들어가있는지 판단
    @SneakyThrows
    public String getCntForList(String in_num, String tag_id){
        String cnt_url = "postlist/getcnt";
        ContentValues cnt_values = new ContentValues();
        cnt_values.put("in_num", in_num);
        cnt_values.put("tag_id", tag_id);

        CntNetworkTask cnt_nt = new CntNetworkTask(cnt_url, cnt_values, "GET");
        return cnt_nt.execute().get();
    }
    //운송장 내 tag_id update, list insert
    @SneakyThrows
    public void insertPostList(String in_num, String tag_id, String address, String name, String state){
        String url = "postlist";
        ContentValues values = new ContentValues();
        values.put("in_num", in_num);
        values.put("tag_id", tag_id);
        values.put("address", address);
        values.put("name", name);
        values.put("state", state);

        NetworkTask list_nt = new NetworkTask(url, values, "POST");
        list_nt.execute();
    }
    // 개수 구하기 통신
    public class CntNetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public CntNetworkTask(String url, ContentValues values, String method) {
            this.url = this.url + url;
            this.values = values;
            this.method = method;
        }

        @SneakyThrows
        @Override
        protected String doInBackground(Void... voids) {
            RequestHttpURLConnection connection = new RequestHttpURLConnection();

            return connection.request(url, values, method);
        }
    }
    //insert, update 통신
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
        }
    }
}
