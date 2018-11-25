package com.github.kko7.manaflux_android.ConnectionHandler;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetSummonerInfo extends AsyncTask<String, Void, String> {
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... urls){
        String url= urls[0];
        StringBuilder result;
        String inputLine;
        try {
            URL myUrl = new URL(url);
            HttpURLConnection connection =(HttpURLConnection)
            myUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            result = stringBuilder;
        }
        catch(IOException e){
            e.printStackTrace();
            result = null;
        }
        return String.valueOf(result);
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }

}