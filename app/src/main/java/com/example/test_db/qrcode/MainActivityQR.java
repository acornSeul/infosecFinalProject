package com.example.test_db.qrcode;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team5_final.R;
import com.example.team5_final.util.CreateQR;
import com.example.team5_final.util.RequestHttpURLConnection;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Hashtable;

import lombok.SneakyThrows;


public class MainActivityQR extends AppCompatActivity {

    private Button button;
    private Button scanQRBtn;
    private EditText editText;
    private EditText editText1;
    private EditText editText2;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_qr);

        final Context context = this;
        editText = (EditText) this.findViewById(R.id.editText);
        editText1 = (EditText) this.findViewById(R.id.editText1);
        editText2 = (EditText) this.findViewById(R.id.editText2);
        button = (Button) this.findViewById(R.id.btn_apply);
        scanQRBtn = (Button) findViewById(R.id.scanQR);

        scanQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivityQR.this, ScanQR.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @SneakyThrows
            @Override
            public void onClick(View v) {
                String text2Qr = editText.getText().toString();
                String text2Qr1 = editText1.getText().toString();
                String text2Qr2 = editText2.getText().toString();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                String url = "applyFirst";
                ContentValues values = new ContentValues();
                values.put("mem_id", "2003");
                NetworkTask nt = new NetworkTask(url, values);

                String result = null;
                // 실행한 결과값 받아오기
                result = nt.execute().get();

                Log.d("test", "seul result : " + result);

                    Hashtable hints = new Hashtable();
                    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                    //BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr + "\n" + text2Qr1 + "\n" + text2Qr2, BarcodeFormat.QR_CODE, 300, 300, hints);
                    BitMatrix bitMatrix = multiFormatWriter.encode(result, BarcodeFormat.QR_CODE, 300, 300, hints);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                    Intent intent = new Intent(context, CreateQR.class);
                    intent.putExtra("pic", bitmap);
                    context.startActivity(intent);

            }
        });
    }

    //웹서버 통신
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "http://192.168.0.3:8080/";
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = this.url + url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result;
            RequestHttpURLConnection connection = new RequestHttpURLConnection();
            result = connection.request(url, values, "POST");

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}