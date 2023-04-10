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

public class GetPatients extends AsyncTask<Void, Void, ArrayList<Patient>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_PATIENTS = "http://"+IP.ip+"/API_TrustyMed/getPatients.php";
    private Context mContext;

    GetPatients(Context context) {
        this.mContext=context;
    }

    @Override
    protected ArrayList<Patient> doInBackground(Void... voids) {

        ArrayList<Patient> patients= new ArrayList<>();
        try {
            URL url = new URL(API_GET_PATIENTS);
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
                if (jsonObject.has("id") && jsonObject.has("first_name")&& jsonObject.has("last_name")&& jsonObject.has("gender")&& jsonObject.has("phone")&& jsonObject.has("address")&& jsonObject.has("dob")&& jsonObject.has("email")&& jsonObject.has("password") ) {
                    int id = jsonObject.getInt("id");
                    String first_name = jsonObject.getString("first_name");
                    String last_name= jsonObject.getString("last_name");
                    String gender= jsonObject.getString("gender");
                    String phone= jsonObject.getString("phone");
                    String address = jsonObject.getString("address");
                    String dob= jsonObject.getString("dob");
                    String email= jsonObject.getString("email");
                    String password= jsonObject.getString("password");
                    Patient patient = new Patient(id, first_name,last_name,gender,phone,address,dob,email,password);
                    patients.add(patient);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patients;
    }
    @Override
    protected void onPostExecute(ArrayList<Patient> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(this.mContext);
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

}
