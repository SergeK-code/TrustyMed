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

public class GetDoctors extends AsyncTask<Void, Void, ArrayList<Doctor>> {
    ProgressDialog progressDialog;
    private static final String API_GET_DOCTORS = "http://"+IP.ip+"/API_TrustyMed/getDoctors.php";
    private Context mContext;
    public GetDoctors(Context context) {
       this.mContext=context;
    }
    @Override
    protected ArrayList<Doctor> doInBackground(Void... voids) {
            ArrayList<Doctor> doctors = new ArrayList<>();
        try {
            URL url = new URL(API_GET_DOCTORS);
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
                // if (jsonObject.has("id") && jsonObject.has("name") && jsonObject.has("phone") && jsonObject.has("isAvailable") && jsonObject.has("Consultation_cost") && jsonObject.has("Speciality_id")) {
                int id=jsonObject.getInt("id");
                String name =jsonObject.getString("name");
                String phone= jsonObject.getString("phone");
                String bio = jsonObject.getString("bio");
                int rating= jsonObject.getInt("rating");
                int is_available= jsonObject.getInt("is_available");
                int consultation_cost= jsonObject.getInt("consultation_cost");
                int speciality_id= jsonObject.getInt("speciality_id");

                Doctor doctor = new Doctor(id,name,phone,bio,rating,is_available,consultation_cost,speciality_id);
                doctors.add(doctor);

                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctors;
    }
    @Override
    protected void onPostExecute(ArrayList<Doctor> result) {
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