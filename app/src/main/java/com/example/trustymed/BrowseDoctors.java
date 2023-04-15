package com.example.trustymed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BrowseDoctors extends Activity {


    private Spinner specialtiesList;
    private ListView doctorsList;
    private Button home;
    private ArrayList<Doctor> doctors= new ArrayList<>();
    private ArrayList<Doctor> doctorsSelected= new ArrayList<>();
    public static Doctor selectedDoctor;
    public DoctorAdapter doctorAdapter;
    public ArrayList<Specialty> specialties= new ArrayList<>();
    public ArrayAdapter<Specialty> specialty_adapter;
    private Specialty selectedSpecialty;
    private GetSpecialties s;
    private GetDoctors d;
    private Patient patient;
    private TextView noDoctors;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.browse_doctors);
        initViews();

        s = new GetSpecialties(BrowseDoctors.this);
        d = new GetDoctors(BrowseDoctors.this);
        try {
            specialties= s.execute().get();
            doctors=d.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        patient = (Patient) getIntent().getSerializableExtra("patient");

        listOfSpecialties();

        specialtiesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        selectedSpecialty = (Specialty) parent.getAdapter().getItem(position);
                        int specialtyId = selectedSpecialty.getId();

                        listOfDoctors(specialtyId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        doctorAdapter = new DoctorAdapter(this, doctorsSelected,specialties);
        doctorsList.setAdapter(doctorAdapter);

        doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDoctor = (Doctor) parent.getAdapter().getItem(position);
                displayDoctorDetails();
                    }
                });

        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

    }

        @Override
        public void onActivityResult ( int request, int result, Intent intent){
            switch (request) {
                case 1:
                    switch (result){
                        case 1:
                            break;
                        case 2:
                            finish();
                            break;
                    }
            }

    }

    private void initViews(){
        specialtiesList = findViewById(R.id.specialty);
        doctorsList = findViewById(R.id.doctors);
        home= findViewById(R.id.home);
        noDoctors=findViewById(R.id.noDoctors);
    }

    private void listOfSpecialties(){

        specialty_adapter = new ArrayAdapter<Specialty>(this, android.R.layout.simple_spinner_item,specialties);
        specialty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialtiesList.setAdapter(specialty_adapter);
    }

    private void listOfDoctors(int specialtyId){


        doctorsSelected.clear();

        for (Doctor D : doctors) {
            if (D.getSpeciality_id() == specialtyId)
                doctorsSelected.add(D);
        }
        if(doctorsSelected.size()<=0){
            doctorsList.setVisibility(View.GONE);
            noDoctors.setVisibility(View.VISIBLE);
        }
        else{
            doctorsList.setVisibility(View.VISIBLE);
            noDoctors.setVisibility(View.GONE);
        }
        doctorAdapter.notifyDataSetChanged();
    }

    private void displayDoctorDetails(){
        Intent i = new Intent(BrowseDoctors.this, DoctorDetails.class);
        i.putExtra("selectedDoctor", selectedDoctor);
        i.putExtra("patient", patient);
        i.putExtra("specialty", selectedSpecialty.getName());
        startActivityForResult(i, 1);
    }
}