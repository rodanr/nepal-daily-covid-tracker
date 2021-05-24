package com.nepaldailycovidcases;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String urlString = "https://corona.lmao.ninja/v2/countries/nepal?today=true&strict=true";
    TextView newCases, newRecovered, newDeaths, totalCases, activeCases, totalRecovered, totalDeaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new jsonFetch().execute();
    }

    protected class jsonFetch extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(urlString);
                urlConnection = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                return new JSONObject(stringBuffer.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            newCases = (TextView) findViewById(R.id.newCases);
            newRecovered = (TextView) findViewById(R.id.newRecovered);
            newDeaths = (TextView) findViewById(R.id.newDeaths);
            totalCases = (TextView) findViewById(R.id.totalCases);
            activeCases = (TextView) findViewById(R.id.activeCases);
            totalRecovered = (TextView) findViewById(R.id.totalRecovered);
            totalDeaths = (TextView) findViewById(R.id.totalDeaths);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (jsonObject != null) {
                try {
//                    Log.e("App", "Success" + jsonObject.getString("todayCases"));
                    newCases.setText("New Cases: " + jsonObject.getString("todayCases"));
                    newRecovered.setText("New Recovered: " + jsonObject.getString("todayRecovered"));
                    newDeaths.setText("New Deaths: " + jsonObject.getString("todayDeaths"));
                    totalCases.setText("Total Cases: " + jsonObject.getString("cases"));
                    activeCases.setText("Active Cases: " + jsonObject.getString("active"));
                    totalRecovered.setText("Total Recovered: " + jsonObject.getString("recovered"));
                    totalDeaths.setText("Total Deaths: " + jsonObject.getString("deaths"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

