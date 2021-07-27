package com.example.team5_final;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 구현 전 테스트 공간
public class TestActivity extends AppCompatActivity {
    TextView home1;
    TextView postcode;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        home1 = findViewById(R.id.home1);
        postcode = findViewById(R.id.postcode);
        editText = findViewById(R.id.edit_addr);
        button = findViewById(R.id.btn_naver);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchAddress(editText.getText().toString(),postcode,home1);
            }
        });
    }

    private void searchAddress(String query, final TextView postcode, final TextView home1) {
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
                String post = response.body().toString();
                String address = post.split("\n")[0];
                String postCode = post.split("\n")[1];
                Log.e("res",response.body().toString());
                home1.setText(address); //주소 텍스트뷰에 도로명주소
                postcode.setText(postCode); //우편번호 텍스트뷰에 우편번호
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {

            }
        });
    }
}
