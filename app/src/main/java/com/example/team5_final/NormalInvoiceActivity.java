package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class NormalInvoiceActivity extends AppCompatActivity {
    TextView txt_in_name;
    TextView txt_in_phone;
    TextView txt_in_address;
    TextView txt_in_zipCode;
    TextView txt_in_re_name;
    TextView txt_in_re_phone;
    TextView txt_in_re_address;
    TextView txt_in_re_zipCode;
    TextView txt_in_p_name;
    TextView txt_in_p_cnt;
    ImageView img_normal;
    String in_num;
    String mem_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normalinvoice);

        Intent intent = getIntent();
        in_num = intent.getStringExtra("in_num");
        mem_id = intent.getStringExtra("mem_id");

        Toolbar toolbar = findViewById(R.id.toolbar_invoice);
        ((AppCompatActivity) NormalInvoiceActivity.this).setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        txt_in_name = findViewById(R.id.txt_in_name);
        txt_in_phone = findViewById(R.id.txt_in_phone);
        txt_in_address = findViewById(R.id.txt_in_address);
        txt_in_zipCode = findViewById(R.id.txt_in_zipCode);
        txt_in_re_name = findViewById(R.id.txt_in_re_name);
        txt_in_re_phone = findViewById(R.id.txt_in_re_phone);
        txt_in_re_address = findViewById(R.id.txt_in_re_address);
        txt_in_re_zipCode = findViewById(R.id.txt_in_re_zipCode);
        txt_in_p_name = findViewById(R.id.txt_in_p_name);
        txt_in_p_cnt = findViewById(R.id.txt_in_p_cnt);
        img_normal = findViewById(R.id.img_normal_qr);

        String url = "invoice/detail/normal";
        ContentValues values = new ContentValues();
        values.put("in_num", in_num);

        NormalNetworkTask normal_nt = new NormalNetworkTask(url, values, "GET");
        normal_nt.execute();
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
    //일반 택배 운송장 조회
    public class NormalNetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public NormalNetworkTask(String url, ContentValues values, String method) {
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
            json.put("mem_id", mem_id);
            json.put("in_num", in_num);

            txt_in_name.setText(json.getString("name"));
            txt_in_phone.setText(json.getString("phone"));
            txt_in_address.setText(json.getString("address"));
            txt_in_zipCode.setText(json.getString("zipCode"));
            txt_in_re_name.setText(json.getString("re_name"));
            txt_in_re_phone.setText(json.getString("re_phone"));
            txt_in_re_address.setText(json.getString("re_address"));
            txt_in_re_zipCode.setText(json.getString("re_zipCode"));
            txt_in_p_name.setText(json.getString("p_name"));
            txt_in_p_cnt.setText(json.getString("p_cnt"));

            //qr code 부분
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            String result = json.toString();
            //BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr + "\n" + text2Qr1 + "\n" + text2Qr2, BarcodeFormat.QR_CODE, 300, 300, hints);
            BitMatrix bitMatrix = multiFormatWriter.encode(result, BarcodeFormat.QR_CODE, 300, 300, hints);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            img_normal.setImageBitmap(bitmap);

        }
    }
}
