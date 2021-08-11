package com.example.team5_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class InvoiceScanActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    String uniqueId;

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
                Toast.makeText(this, "내용이 없습니다.", Toast.LENGTH_LONG).show();
            } else {
                result_json = new JSONObject(result.getContents());
                //택배 기사가 스캔 시
                if (Integer.parseInt(uniqueId) > 7000){
                    Intent post_intent = new Intent(InvoiceScanActivity.this, PostManActivity.class);
                    startActivity(post_intent);

                }else{
                    //자신의 정보 스캔 시
                    if (uniqueId.equals(result_json.getString("mem_id"))) {
                        Intent intent = new Intent(InvoiceScanActivity.this, ScanTestActivity.class);
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
}
