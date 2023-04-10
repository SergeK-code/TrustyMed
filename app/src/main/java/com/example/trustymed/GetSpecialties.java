package com.example.trustymed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetSpecialties extends AsyncTask<Void, Void, ArrayList<Specialty>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_SPECIALITIES = "http://"+IP.ip+"/API_TrustyMed/getSpecialties.php";
    private Context mContext;
    public GetSpecialties(Context context) {
        this.mContext=context;
    }
    @Override
    protected ArrayList<Specialty> doInBackground(Void... voids) {
        ArrayList<Specialty> specialties= new ArrayList<>();
        try {
            URL url = new URL(API_GET_SPECIALITIES);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String response = stringBuilder.toString();
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("id") && jsonObject.has("name") ) {
                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    Specialty specialty = new Specialty(id, name);
                    specialties.add(specialty);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return specialties;
    }
    @Override
    protected void onPostExecute(ArrayList<Specialty> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


}
