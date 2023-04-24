package com.example.trustymed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CancelAppointment extends Activity{
    private ListView appointmentsList;
    private CancelAppointmentAdapter cancelAppointmentAdapter;
    private Patient p;
    private int patient_id;
    private ArrayList<Appointment> appointments,myAppointments= new ArrayList<>();
    private ArrayList<Doctor> doctorsList;
    private Button home;
    private TextView noAppointments;

    @Override
    public void onCreate(Bundle saveInstanceState){
    super.onCreate(saveInstanceState);
    setContentView(R.layout.cancel_appointment);

    appointmentsList= findViewById(R.id.myAppointments);
    noAppointments= findViewById(R.id.noAppointments);

    p= (Patient) getIntent().getSerializableExtra("patient");
    patient_id=p.getId();


        GetAppointments a = new GetAppointments(this);
        GetDoctors d = new GetDoctors(this);
        try {
            appointments= a.execute().get();
            doctorsList= d.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        for(Appointment app : appointments){
            if(app.getUser_id()==patient_id){
                myAppointments.add(app);
            }
        }


        if(myAppointments.size()<=0){
            appointmentsList.setVisibility(View.GONE);
            noAppointments.setVisibility(View.VISIBLE);
        }
        else{
            noAppointments.setVisibility(View.GONE);
            appointmentsList.setVisibility(View.VISIBLE);
        }

    cancelAppointmentAdapter= new CancelAppointmentAdapter(this,patient_id,myAppointments,doctorsList);
    appointmentsList.setAdapter(cancelAppointmentAdapter);

    home=findViewById(R.id.home);
    home.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            finish();
        }
    });
    }

}

