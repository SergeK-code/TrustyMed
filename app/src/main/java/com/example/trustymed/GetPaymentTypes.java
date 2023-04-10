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

public class GetPaymentTypes extends AsyncTask<Void, Void, ArrayList<PaymentType>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_PAYMENT_TYPES = "http://"+IP.ip+"/API_TrustyMed/getPaymentTypes.php";
    private Context mContext;

    public GetPaymentTypes(Context context) {
       this.mContext=context;
    }

    @Override
    protected ArrayList<PaymentType> doInBackground(Void... voids) {
        ArrayList<PaymentType> paymentTypes= new ArrayList<>();
        try {
            URL url = new URL(API_GET_PAYMENT_TYPES);
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
                if (jsonObject.has("id") && jsonObject.has("payment_method") ) {
                    int id = jsonObject.getInt("id");
                    String payment_method = jsonObject.getString("payment_method");
                    PaymentType paymentType = new PaymentType(id,payment_method);
                    paymentTypes.add(paymentType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentTypes;

    }
    @Override
    protected void onPostExecute(ArrayList<PaymentType> result) {
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
