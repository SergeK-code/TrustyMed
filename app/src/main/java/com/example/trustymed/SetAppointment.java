package com.example.trustymed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SetAppointment extends AsyncTask<Void,Void, String> {
    private String selectedDate;
    private String selectedTime;
    private String response;
    private int attend;
    private int cancel;
    private int consultation_cost;
    private int patient_id;
    private int doctor_id;
    private int selectedPaymentTypeId;
    private ProgressDialog progressDialog;
    Context mcontext;
    private String API_SET_APPOINTMENT="http://"+IP.ip+"/API_TrustyMed/setAppointment.php";

    public SetAppointment(Context context,String selectedDate,String selectedTime,int consultation_cost,int patient_id,int doctor_id,int selectedPaymentTypeId){
        this.selectedDate=selectedDate;
        this.selectedTime=selectedTime;
        this.attend=0;
        this.cancel=0;
        this.consultation_cost=consultation_cost;
        this.patient_id=patient_id;
        this.doctor_id=doctor_id;
        this.selectedPaymentTypeId=selectedPaymentTypeId;
        this.mcontext=context;
    }
    @Override
    protected String doInBackground(Void... voids) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", this.selectedDate);
            jsonObject.put("time", this.selectedTime);
            jsonObject.put("attend", this.attend);
            jsonObject.put("cancel", this.cancel);
            jsonObject.put("cost", this.consultation_cost);
            jsonObject.put("user_id", this.patient_id);
            jsonObject.put("doctor_id", this.doctor_id);
            jsonObject.put("payment_id", this.selectedPaymentTypeId);

            // Convert the JSON object to a string
            String jsonInputString = jsonObject.toString();

            // Define the API endpoint URL
            URL url = new URL(API_SET_APPOINTMENT);

            // Open a connection to the API endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set the content type to application/json
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            // Enable input and output streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Write the JSON data to the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response from the API endpoint
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            response = stringBuilder.toString();

            // Close the connection
            connection.disconnect();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return response;
    }
        @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(this.mcontext);
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
