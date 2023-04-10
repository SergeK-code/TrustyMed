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

public class GetAppointments extends AsyncTask<Void, Void, ArrayList<Appointment>> {
    ProgressDialog progressDialog;
    private static final String API_GET_APPOINTMENTS = "http://"+IP.ip+"/API_TrustyMed/getAppointments.php";
    private Context mContext;

    public GetAppointments(Context context) {
        this.mContext=context;
    }

    @Override
    protected ArrayList<Appointment> doInBackground(Void... voids) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        try {
            URL url = new URL(API_GET_APPOINTMENTS);
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
                int id=jsonObject.getInt("id");

                String date =jsonObject.getString("date");

                String time= jsonObject.getString("time");

                int attend = jsonObject.getInt("attend");

                int cancel= jsonObject.getInt("cancel");

                int cost= jsonObject.getInt("cost");

                int user_id= jsonObject.getInt("user_id");

                int doctor_id= jsonObject.getInt("doctor_id");

                int payment_id= jsonObject.getInt("payment_id");


                Appointment appointment = new Appointment(id,date,time,attend,cancel,cost,user_id,doctor_id,payment_id);
                appointments.add(appointment);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }
    @Override
    protected void onPostExecute(ArrayList<Appointment> result) {
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