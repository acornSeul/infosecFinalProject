package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.team5_final.util.RequestHttpURLConnection;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Hashtable;

import lombok.SneakyThrows;

//안전 택배 운송장
public class EncryptInvoiceActivity extends AppCompatActivity {
    String in_num;
    String uniqueId;
    TextView txt_en_address;
    TextView txt_en_zipCode;
    ImageView img_encrypt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryptinvoice);

        //from RecyclerAdapter
        Intent intent = getIntent();
        in_num = intent.getStringExtra("in_num");
        uniqueId = intent.getStringExtra("mem_id");

        Toolbar toolbar = findViewById(R.id.toolbar_encypt);
        ((AppCompatActivity) EncryptInvoiceActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        txt_en_address = findViewById(R.id.txt_en_address);
        txt_en_zipCode = findViewById(R.id.txt_en_zipCode);
        img_encrypt = findViewById(R.id.img_encrypt);

        String url = "invoice/detail/encrypt";
        ContentValues values = new ContentValues();
        values.put("in_num", in_num);

        EncryptNetowrkTask en_nt = new EncryptNetowrkTask(url, values, "GET");
        en_nt.execute();

        createQR();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createQR(){
        //qr코드에 넣을 정보 조회
        String qr_url = "invoice/detail/normal";
        ContentValues values = new ContentValues();
        values.put("in_num", in_num);

        QrNetworkTask qr_nt = new QrNetworkTask(qr_url, values, "GET");
        qr_nt.execute();
    }
    //안전 택배 운송장 조회
    public class EncryptNetowrkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public EncryptNetowrkTask(String url, ContentValues values, String method) {
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

            if (s.equals("null")){
                txt_en_address.setText("");
                txt_en_zipCode.setText("");
            }
            else{
                JSONObject json = new JSONObject(s);

                txt_en_address.setText(json.getString("re_address"));
                txt_en_zipCode.setText(json.getString("re_zipCode"));
            }
        }
    }
    //qr코드에 넣을 정보 조회
    public class QrNetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public QrNetworkTask(String url, ContentValues values, String method) {
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
            JSONObject json = new JSONObject(s);
            json.put("mem_id", uniqueId);
            json.put("in_num", json.getString("in_num"));

            Log.d("qr json result", json.toString());

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(json.toString(), BarcodeFormat.QR_CODE, 300, 300, hints);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            img_encrypt.setImageBitmap(bitmap);
        }
    }
}
