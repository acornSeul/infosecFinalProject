package com.example.team5_final.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.team5_final.InvoiceScanActivity;
import com.example.team5_final.R;

public class Fragment_home extends Fragment{
    Button btn_scan;
    String uniqueId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_home1, container, false);

        btn_scan = (Button)view.findViewById(R.id.btn_scan);

        Bundle extra = this.getArguments();

        if (extra != null){
            extra = getArguments();
            uniqueId = extra.getString("uniqueId");
        }


        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("seul id", uniqueId);
                Intent intent = new Intent(getContext(), InvoiceScanActivity.class);
                intent.putExtra("uniqueId", uniqueId);
                startActivity(intent);
            }
        });

        return view;
    }

}