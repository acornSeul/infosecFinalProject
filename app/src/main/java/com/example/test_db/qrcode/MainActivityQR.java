package com.example.test_db.qrcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team5_final.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Hashtable;


//public class MainActivityQR extends AppCompatActivity {
//    private Button createQRBtn;
//    private Button scanQRBtn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_qr);
//
//        createQRBtn = (Button) findViewById(R.id.createQR);
//        scanQRBtn = (Button) findViewById(R.id.scanQR);
//
//        createQRBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                Intent intent = new Intent(MainActivityQR.this, CreateQR.class);
//                startActivity(intent);
//            }
//        });
//
//        scanQRBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                Intent intent = new Intent(MainActivityQR.this, ScanQR.class);
//                startActivity(intent);
//            }
//        });
//    }
//}

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
        button = (Button) this.findViewById(R.id.button);
        scanQRBtn = (Button) findViewById(R.id.scanQR);

        scanQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivityQR.this, ScanQR.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String text2Qr = editText.getText().toString();
                String text2Qr1 = editText1.getText().toString();
                String text2Qr2 = editText2.getText().toString();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    Hashtable hints = new Hashtable();
                    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr + "\n" + text2Qr1 + "\n" + text2Qr2, BarcodeFormat.QR_CODE, 300, 300, hints);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    Intent intent = new Intent(context, CreateQR.class);
                    intent.putExtra("pic", bitmap);
                    context.startActivity(intent);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}