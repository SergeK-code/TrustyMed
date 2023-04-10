package com.example.trustymed;

import android.os.AsyncTask;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {
     public static String ip= "192.168.1.109";
  /*  public static String ip;

    public static void getServerIpAddress(final OnIpAddressReceivedListener listener) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    InetAddress localhost = InetAddress.getLocalHost();
                    return localhost.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String ipAddress) {
                super.onPostExecute(ipAddress);
                if (ipAddress != null) {
                    ip = ipAddress;
                    listener.onIpAddressReceived(ipAddress);
                }
            }
        };

        task.execute();
    }

    public interface OnIpAddressReceivedListener {
        void onIpAddressReceived(String ipAddress);
    } */
}
