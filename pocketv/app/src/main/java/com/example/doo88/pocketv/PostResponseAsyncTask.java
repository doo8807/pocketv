package com.example.doo88.pocketv;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by doo88 on 2017-02-18.
 */

public class PostResponseAsyncTask extends AsyncTask<String, Void, String> {
    private AsyncResponse delegate;
    private ProgressDialog progressDialog;
    StringBuffer sb1 = new StringBuffer();
    private Context context;
    private String link, data, insertdata;


    public PostResponseAsyncTask(AsyncResponse delegate,
                                 String link, String insertdata) {

        this.delegate = delegate;
        this.context = (Context) delegate;
        this.link = link;
        this.insertdata = insertdata;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {

        s = s.trim();

        delegate.processFinish(s);

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String link = (String) params[0];
            String aaa = (String) params[1];

            try {
                JSONArray jsonArray = new JSONArray(aaa);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (i == 0) {
                        data = "data" + i + "=" + jsonObject.getString("data").toString();
                    } else {
                        data += "&" + "data" + i + "=" + jsonObject.getString("data").toString();
                    }
                }
                Log.i("제이슨", "" + data);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));


            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {

                sb1.append(line);

                break;
            }
            return sb1.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }
}