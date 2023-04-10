package com.example.trustymed;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    public ArrayList<Patient> patients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ActionBar actionBar = getSupportActionBar();
        int color = ContextCompat.getColor(this, R.color.app_blue);
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_title);
        TextView titleTextView = actionBar.getCustomView().findViewById(R.id.action_bar_title);
        titleTextView.setText("Trusty Med");




        Intent i = new Intent(SplashScreen.this, Login.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, 2000);

    }
}