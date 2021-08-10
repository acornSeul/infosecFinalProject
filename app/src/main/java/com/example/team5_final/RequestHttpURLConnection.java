package com.example.team5_final;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import lombok.SneakyThrows;

public class RequestHttpURLConnection {
    @SneakyThrows
    public String request(String _url, ContentValues _params, String method){

        HttpURLConnection urlConn = null;
        StringBuffer sbParams = new StringBuffer();
        JSONObject json = new JSONObject();
        String key;
        String value;
        /*
         * 1. StringBuffer에 파라미터 연결
         * */
        // 보낼 데이터가 없으면 파라미터를 비운다.
        if (_params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
           if (method.equals("GET")){
               // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
               boolean isAnd = false;
               // 파라미터 키와 값.
               for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                   key = parameter.getKey();
                   value = parameter.getValue().toString();

                   // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                   if (isAnd)
                       sbParams.append("&");

                   sbParams.append(key).append("=").append(value);

                   // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                   if (!isAnd)
                       if (_params.size() >= 2)
                           isAnd = true;
               }
           }
           else{
               for (Map.Entry<String, Object> params : _params.valueSet()){
                   key = params.getKey();
                   value = params.getValue().toString();

                   json.put(key, value);
               }
           }
        }
        /*
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try{
            // [2-1]. urlConn 설정.
            if (method.equals("POST")){
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);

                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                // [2-2]. parameter 전달 및 데이터 읽어오기.
                String strParams = json.toString(); //json 형식으로 저장
                
                Log.d("params test : ", strParams);
                Log.d("method test ", method);
                //urlConn.setDoOutput(true);
                //OutputStream os = urlConn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream());
                writer.write(strParams);
                writer.flush();
                writer.close();

                urlConn.connect();

                Log.d("pass", "params pass");
            }
            else if (method.equals("GET")){
                URL url = new URL(_url + "?" + sbParams.toString());
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");
            }

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d("request fail", "fail!!");
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                page += line;
            }
            Log.d("page result!!!!!!!", page);

            return page;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return null;
    }
}
