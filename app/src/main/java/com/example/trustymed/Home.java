package com.example.trustymed;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

public class Home extends Activity {

    private ImageView image;
    private Button bookAppointment,cancelAppointment,editProfile,logout;
    private Patient patient;
    private TextView greeting;
    private String password;
    private Bundle bundle;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.home);
        bundle= getIntent().getExtras();

        patient = (Patient) getIntent().getSerializableExtra("patient");
        password = bundle.getString("password");

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", patient.getEmail());
        editor.putString("password", password);
        editor.apply();

        bookAppointment= findViewById(R.id.bookAppointment);
        cancelAppointment= findViewById(R.id.cancelAppointment);
        editProfile= findViewById(R.id.editProfile);
        logout=findViewById(R.id.logout);
        greeting= findViewById(R.id.greeting);
        ViewCompat.setBackgroundTintList(bookAppointment, null);
        ViewCompat.setBackgroundTintList(cancelAppointment, null);
        ViewCompat.setBackgroundTintList(editProfile, null);
        ViewCompat.setBackgroundTintList(logout, null);



        if(patient!=null){
            String text="Welcome "+patient.getFirst_name();
            greeting.setText(text);
        }

        bookAppointment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Home.this,BrowseDoctors.class);
                i.putExtra("patient",patient);
                startActivity(i);
            }
        });

        cancelAppointment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Home.this, CancelAppointment.class);
                i.putExtra("patient",patient);
                startActivity(i);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Home.this,EditProfile.class);
                i.putExtra("patient",patient);
                startActivityForResult(i,3);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.apply();

                Intent i = new Intent(Home.this, Login.class);
                Toast.makeText(Home.this,"Logged out",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }
    @Override
    public void onActivityResult(int request,int result,Intent intent){
        switch(request){
            case 3:
                switch(result){
                    case 2:
                        Intent i = new Intent(Home.this,Login.class);
                        startActivity(i);
                    case 1:
                        //do nothing
                }
        }
    }
}
