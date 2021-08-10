package com.example.team5_final;

import android.content.ContentValues;
import android.os.AsyncTask;

public class RestNetworkTask extends AsyncTask <Void, Void, String>{
    private String url = "https://hqlb195661.execute-api.us-east-2.amazonaws.com/Develop/";
    private ContentValues values;
    private String method;

    public RestNetworkTask(String url, ContentValues values, String method) {
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
