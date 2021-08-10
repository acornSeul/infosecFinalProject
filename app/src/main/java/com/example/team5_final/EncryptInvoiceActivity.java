package com.example.team5_final;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team5_final.network.RequestHttpURLConnection;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.Hashtable;

import lombok.SneakyThrows;

public class EncryptInvoiceActivity extends AppCompatActivity {
    String in_num;
    TextView txt_en_address;
    TextView txt_en_zipCode;
    ImageView img_encrypt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryptinvoice);

        Intent intent = getIntent();
        in_num = intent.getStringExtra("in_num");

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
    @SneakyThrows
    public void createQR(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr + "\n" + text2Qr1 + "\n" + text2Qr2, BarcodeFormat.QR_CODE, 300, 300, hints);
        BitMatrix bitMatrix = multiFormatWriter.encode("result", BarcodeFormat.QR_CODE, 300, 300, hints);

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        img_encrypt.setImageBitmap(bitmap);
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
}
