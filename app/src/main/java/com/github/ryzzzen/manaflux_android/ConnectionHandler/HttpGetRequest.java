package com.github.ryzzzen.manaflux_android.ConnectionHandler;

import android.os.AsyncTask;
import android.util.Log;

import com.github.ryzzzen.manaflux_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final String TAG = "HttpGetRequest";
    TextView statusLabel;
    
    public HttpGetRequest(TextView sL) {
        statusLabel = sL;
    }


    @Override
    protected String doInBackground(String... urls){
        String url= urls[0];// get the URL String here
        StringBuilder result;
        String inputLine;
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(url);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
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
        JSONObject summonerInfo;
        try {
            summonerInfo = new JSONObject(String.valueOf(result));
            String summonerName = summonerInfo.getString("summonerName");
            Log.d(TAG, summonerName);
            statusLabel.setText(summonerName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
