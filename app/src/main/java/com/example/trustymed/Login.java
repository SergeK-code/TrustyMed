package com.example.trustymed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.mindrot.jbcrypt.BCrypt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private TextView error;
    private Button login;
    private Button register;
    private GetPatients p;
    private ArrayList<Patient> patients;
    private boolean found=false;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login);
        bundle = getIntent().getExtras();

        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        error= findViewById(R.id.error);
        login= findViewById(R.id.login);
        register= findViewById(R.id.register);
        ViewCompat.setBackgroundTintList(login, null);
        ViewCompat.setBackgroundTintList(register, null);



        ActionBar actionBar = getSupportActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.blue)));
        assert actionBar != null;
        actionBar.hide();




        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                p= new GetPatients(Login.this);
                try {
                    patients = p.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                String emailInput = email.getText().toString().trim();
                String passInput = password.getText().toString().trim();

                if (patients.size()<=0) {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Cannot connect to database");
                } else {
                    for (Patient p : patients) {
                        String hashedPassword2y= p.getPassword();
                        String hashedPassword2a= hashedPassword2y.replaceFirst("2y","2a");

                        if (p.getEmail().equals(emailInput) && BCrypt.checkpw(passInput,hashedPassword2a)) {
                            if (error.getVisibility() == View.VISIBLE) {
                                error.setVisibility(View.GONE);
                            }
                            found = true;
                            displayHome(p);
                            break;
                        }
                    }
                    if (found == false) {
                        error.setVisibility(View.VISIBLE);
                        error.setText("Wrong credentials");
                    }
                }
            }
        });

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        String emailPref = preferences.getString("email", null);
        String passwordPref = preferences.getString("password", null);

        if (emailPref != null && passwordPref != null) {
            email.setText(emailPref);
            password.setText(passwordPref);
            login.performClick();
        } else {
            //do nothing
        }

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });
    }

    private void displayHome(Patient p){
        Intent i = new Intent(Login.this, Home.class);
        i.putExtra("patient", p);
        i.putExtra("password",password.getText().toString().trim());
        startActivity(i);
    }
}
