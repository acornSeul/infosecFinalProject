package com.example.team5_final.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.team5_final.AfterApplyActivity;
import com.example.team5_final.R;
import com.example.team5_final.util.AddressResponse;
import com.example.team5_final.util.NaverMapApi;
import com.example.team5_final.util.RequestHttpURLConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment_apply extends Fragment{
    final int MIN_KEYBOARD_HEIGHT_PX = 150;
    //default 설정할 edittext들
    EditText mem_name;
    EditText mem_phone;
    EditText mem_address;
    EditText mem_zipCode;
    EditText mem_detail;
    //받는 사람 정보
    EditText rename;
    EditText rePhone;
    EditText readdress;
    EditText rezipCode;
    EditText reDetail;
    //상품 정보
    EditText p_name;
    EditText p_cnt;
    EditText p_price;
    //라디오
    RadioGroup group;
    RadioButton encrypt;
    RadioButton normal;
    String encrpyt_type;
    //mem_id
    String uniqueId;
    //기존 정보 or 새로운 정보
    String addr_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_apply1, container, false);

        //버튼
        Button btn_addr = (Button) view.findViewById(R.id.btn_address);
        Button btn_readdr = (Button) view.findViewById(R.id.btn_readress);
        Button btn_default_addr = (Button) view.findViewById(R.id.btn_default_addr);
        Button btn_new_addr = (Button) view.findViewById(R.id.btn_new_addr);
        Button btn_apply = (Button) view.findViewById(R.id.btn_apply_post);
        //안전택배 라디오버튼
        group = (RadioGroup)view.findViewById(R.id.radiogroup_type);
        //보내는 사람 정보
        mem_name = (EditText)view.findViewById(R.id.edit_apply_name);
        mem_phone = (EditText)view.findViewById(R.id.edit_apply_phone);
        mem_zipCode = (EditText)view.findViewById(R.id.edit_apply_zipCode);
        mem_address = (EditText)view.findViewById(R.id.edit_apply_address);
        mem_detail = (EditText)view.findViewById(R.id.edit_apply_detail);
        //받는 사람 정보
        rename = (EditText)view.findViewById(R.id.edit_apply_rename);
        rePhone = (EditText)view.findViewById(R.id.edit_apply_rephone);
        readdress = (EditText)view.findViewById(R.id.edit_apply_readdress);
        rezipCode = (EditText)view.findViewById(R.id.edit_apply_rerezipCode);
        reDetail = (EditText)view.findViewById(R.id.edit_apply_redetail);
        //상품 정보
        p_name = (EditText)view.findViewById(R.id.edit_apply_pname);
        p_cnt = (EditText)view.findViewById(R.id.edit_apply_pcnt);
        p_price = (EditText)view.findViewById(R.id.edit_apply_price);
        //실행 시 전체 공백처리
        clearText();

        //from AfterLoginActivity
        Bundle extra = this.getArguments();

        if (extra != null){
            extra = getArguments();
            uniqueId = extra.getString("uniqueId");
        }

        //새로운 정보 입력 선택시
        btn_new_addr.setOnClickListener((View v) -> {
                addr_type = "new";
                makeBlankText(mem_address);
                makeBlankText(mem_zipCode);
                makeBlankText(mem_name);
                makeBlankText(mem_phone);
        });
        //기본 주소 선택시
        btn_default_addr.setOnClickListener(new View.OnClickListener(){
            @SneakyThrows
            @Override
            public void onClick(View v) {
                addr_type = "default";
                //기존 회원 정보 select
                String applyFor_url = "apply/selectfor";
                ContentValues applyFor_values = new ContentValues();
                applyFor_values.put("mem_id", uniqueId);

                DefaultNetworkTask getDefault_nt = new DefaultNetworkTask(applyFor_url, applyFor_values,"GET");
                getDefault_nt.execute();
            }
        });
        //보내는 사람 주소 검색
        btn_addr.setOnClickListener((View v) ->
                makeDialog(mem_zipCode, mem_address)
        );
        //받는 사람 주소 검색
        btn_readdr.setOnClickListener((View v) ->
                makeDialog(rezipCode, readdress)
        );
        //신청 클릭
        btn_apply.setOnClickListener(new View.OnClickListener(){
            ContentValues apply_values = new ContentValues();
            @SneakyThrows
            @Override
            public void onClick(View v) {
                apply_values.put("mem_id", uniqueId);
                apply_values.put("in_name", mem_name.getText().toString());
                apply_values.put("in_phone", mem_phone.getText().toString());
                apply_values.put("in_zipCode", mem_zipCode.getText().toString());
                apply_values.put("in_address", mem_address.getText().toString() + " " + mem_detail.getText().toString());
                //택배 타입 설정
                int rb = group.getCheckedRadioButtonId();
                switch (rb){
                    case R.id.radio_encrypt:
                        encrpyt_type = "Y";
                        break;
                    case R.id.radio_normal:
                        encrpyt_type = "N";
                        break;
                }
                apply_values.put("encryptYn", encrpyt_type);
                //받는 사람 정보
                apply_values.put("re_name", rename.getText().toString());
                apply_values.put("re_phone", rePhone.getText().toString());
                apply_values.put("re_zipCode", rezipCode.getText().toString());
                apply_values.put("re_address", readdress.getText().toString() + " " + reDetail.getText().toString());
                //물품 정보
                apply_values.put("p_name", p_name.getText().toString());
                apply_values.put("p_cnt", p_cnt.getText().toString());
                apply_values.put("p_price", p_price.getText().toString());

                // 여기 부분!!!!!!!!!
                String apply_url = "apply";
                ApplyNetworkTask apply_nt = new ApplyNetworkTask(apply_url, apply_values, "POST");
                apply_nt.execute();
            }
        });
        return view;
    }
    //도로명 주소 검색
    private void searchAddress(String query, final EditText input_zipcode, final EditText input_addr, TextView out_addr) {
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
                    out_addr.setText(response.body().toString());
                    String[] str = response.body().toString().split("\n");

                    //주소를 잘못 입력했을 경우
                    if (str.length <= 1){
                        out_addr.setText("");
                        Toast.makeText(getActivity(), "주소를 잘못 입력했습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        input_addr.setText(str[0]); //주소 edittext에 도로명주소
                        input_zipcode.setText(str[1]); //우편번호 edittext에 우편번호
                    }
                }
                //주소가 아예 없을 경우
                else{
                    out_addr.setText("");
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
        TextView out_addr = (TextView)dialog.findViewById(R.id.txt_out_addr);
        Button positive = (Button)dialog.findViewById(R.id.btn_addr_ok);
        Button negative = (Button)dialog.findViewById(R.id.btn_addr_negative);

        //입력 버튼
        positive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchAddress(input_addr.getText().toString(), dial_zipCode, dial_addr, out_addr);
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
    //부분 공백 처리
    public void makeBlankText (EditText editText){
        editText.setText("");
    }
    //전체 공백 처리
    public void clearText (){
        mem_name.setText("");
        mem_phone.setText("");
        mem_zipCode.setText("");
        mem_address.setText("");
        mem_detail.setText("");
        //받는 사람 정보
        rename.setText("");
        rePhone.setText("");
        readdress.setText("");
        rezipCode.setText("");
        reDetail.setText("");
        //상품 정보
        p_name.setText("");
        p_cnt.setText("");
        p_price.setText("");
    }
    //Fragment Interface
    interface RefreshInterface{
        public void refreshAdapterFragment_list();
    }
    //기본 정보 조회 통신
    public class DefaultNetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public DefaultNetworkTask(String url, ContentValues values, String method) {
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
            JSONObject default_json = new JSONObject(s);
            //기본 주소 설정
            mem_name.setText(default_json.getString("name"));
            mem_phone.setText(default_json.getString("phone"));
            mem_address.setText(default_json.getString("address"));
            mem_zipCode.setText(default_json.getString("zipcode"));
        }
    }
    //기본 정보 조회 통신
    public class ApplyNetworkTask extends AsyncTask<Void, Void, String> {
        private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
        private ContentValues values;
        private String method;

        public ApplyNetworkTask(String url, ContentValues values, String method) {
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
            JSONObject result_json = new JSONObject(s);
            int i_result = result_json.getInt("i_result");
            int p_result = result_json.getInt("p_result");
            int b_result = result_json.getInt("b_result");
            String encryptYn = result_json.getString("encryptYn");
            String apply_result = "";

            if (encryptYn.equals("Y")){
                if (i_result == 1 && p_result == 1 && b_result == 1){
                    apply_result =  "success";
                }
            }
            else{
                if (i_result == 1 && p_result ==1){
                    apply_result =  "success";
                }
            }

            if (apply_result.equals("success")){
                Intent intent = new Intent(getContext(), AfterApplyActivity.class);
                startActivity(intent);
                clearText();
            }
        }
    }
}