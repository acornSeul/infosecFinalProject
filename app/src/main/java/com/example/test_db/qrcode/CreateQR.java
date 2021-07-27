package com.example.test_db.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team5_final.R;

//public class CreateQR extends AppCompatActivity {
//    private ImageView iv;
//    private String text;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_qr);
//
//        iv = (ImageView)findViewById(R.id.qrcode);
//        text = "www.naver.com";
//
//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try{
//            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//            iv.setImageBitmap(bitmap);
//        }catch (Exception e){}
//    }
//}

public class CreateQR extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        Bitmap bitmap = getIntent().getParcelableExtra("pic");
        imageView.setImageBitmap(bitmap);
    }
}