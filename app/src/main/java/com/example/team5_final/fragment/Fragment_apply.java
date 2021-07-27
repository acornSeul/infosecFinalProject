package com.example.team5_final.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team5_final.AddressResponse;
import com.example.team5_final.NaverMapApi;
import com.example.team5_final.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment_apply extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_apply1, container, false);

        Button btn_addr = (Button) view.findViewById(R.id.btn_address);
        Button btn_readdr = (Button) view.findViewById(R.id.btn_readress);
        //보내는 사람 정보
        EditText address = (EditText)view.findViewById(R.id.edit_apply_address);
        EditText zipCode = (EditText)view.findViewById(R.id.edit_apply_zipCode);
        //받는 사람 정보
        EditText readdress = (EditText)view.findViewById(R.id.edit_apply_readdress);
        EditText rezipCode = (EditText)view.findViewById(R.id.edit_apply_rerezipCode);

        //주소 찾기 버튼
        btn_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog(zipCode, address);
            }
        });
        //받는 사람 주소 검색
        btn_readdr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                makeDialog(rezipCode, readdress);
            }
        });

        return view;
    }
    //도로명 주소 검색
    private void searchAddress(String query, final EditText input_zipcode, final EditText input_addr) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        retrofit.create(NaverMapApi.class).searchAddress(query).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (!response.body().toString().equals("")){
                    String[] str = response.body().toString().split("\n");

                    //주소를 잘못 입력했을 경우
                    if (str.length <= 1){
                        Toast.makeText(getActivity(), "주소를 잘못 입력했습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        input_addr.setText(str[0]); //주소 edittext에 도로명주소
                        input_zipcode.setText(str[1]); //우편번호 edittext에 우편번호
                    }
                }
                //주소가 아예 없을 경우
                else{
                    Toast.makeText(getActivity(), "주소를 잘못 입력했습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {

            }
        });
    }

    //다이얼로그 생성 및 처리
    public void makeDialog(final EditText dial_zipCode, final EditText dial_addr){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_layout);
        dialog.show();

        EditText input_addr = (EditText)dialog.findViewById(R.id.edit_input_addr);
        Button positive = (Button)dialog.findViewById(R.id.btn_addr_ok);
        Button negative = (Button)dialog.findViewById(R.id.btn_addr_negative);

        //입력 버튼
        positive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchAddress(input_addr.getText().toString(), dial_zipCode, dial_addr);
                dialog.dismiss();
            }
        });

        //취소 버튼
        negative.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}