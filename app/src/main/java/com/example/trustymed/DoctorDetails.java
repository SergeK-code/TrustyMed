package com.example.trustymed;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class DoctorDetails extends Activity {
    private NestedScrollView scrollView;
    private ImageView doctorImage;
    private TextView doctorName,doctorSpecialty,doctorRating,doctorPhone;
    private DatePicker datePicker;
    private GridView hoursGrid;
    private Button book,home,back;
    private Doctor selectedDoctor;
    private Patient patient;
    private String selectedSpecialty;
    private ArrayList<Appointment> appointments;
    private GetAppointments a;
    private ArrayAdapter hoursGrid_adapter;
    private String currentDate;
    public static final List<String> hours= Arrays.asList("09:00:00","10:00:00","11:00:00","12:00:00","13:00:00","14:00:00","15:00:00","16:00:00","17:00:00");
    private ArrayList<String> hoursapp= new ArrayList<>();
    private Calendar calendar,today;
    private SimpleDateFormat sdf;
    private boolean found;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.doctor_details);
        scrollView= findViewById(R.id.scrollView);
        doctorImage= findViewById(R.id.doctorImage);
        doctorName= findViewById(R.id.doctorName);
        doctorSpecialty= findViewById(R.id.doctorSpecialty);
        doctorRating= findViewById(R.id.doctorRating);
        doctorPhone= findViewById(R.id.doctorPhone);
        datePicker= findViewById(R.id.datePicker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setSpinnersShown(true);
            datePicker.setCalendarViewShown(false);
        } else {
            // For older Android versions, use reflection to set the mode
            try {
                Field datePickerModeField = DatePicker.class.getDeclaredField("mDatePickerMode");
                datePickerModeField.setAccessible(true);
                datePickerModeField.setInt(datePicker, 2);// 2 for spinner mode
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        hoursGrid= findViewById(R.id.hours);
        book= findViewById(R.id.book);
        home=findViewById(R.id.home);
        back=findViewById(R.id.back);


        selectedDoctor= (Doctor) getIntent().getSerializableExtra("selectedDoctor");
        patient = (Patient) getIntent().getSerializableExtra("patient");
        selectedSpecialty = (String) getIntent().getStringExtra("specialty");

        today = Calendar.getInstance();
        datePicker.setMinDate(today.getTimeInMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // to avoid unexpected parsing behavior
        hoursapp.clear();
        int day = datePicker.getDayOfMonth();
        int monthv = datePicker.getMonth() + 1; // Add 1 to get 1-based month index
        int yearv = datePicker.getYear();

        a = new GetAppointments(DoctorDetails.this);
        try {
            appointments= a.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        today.set(yearv, monthv - 1, day); // Subtract 1 to get 0-based month index
        String todayDate = sdf.format(today.getTime()).trim();

        for(String h: hours){
            found=false;
            for(Appointment a : appointments){
                if(a.getDoctor_id()==selectedDoctor.getId() && a.getDate().equals(todayDate) && Objects.equals(a.getTime(),h)){
                    hoursapp.add(h.replaceFirst(":00","") + " \nTaken");
                    found=true;
                    break;
                }

            }
            if(!found){
                hoursapp.add(h.replaceFirst(":00","")+ " \nAvailable");
            }
        }


        hoursGrid_adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hoursapp);
        hoursGrid.setAdapter(hoursGrid_adapter);


        doctorImage.setImageResource(R.drawable.doctor_logo);
        doctorName.setText("Name: "+selectedDoctor.getName());
        doctorSpecialty.setText(selectedSpecialty);

            String rating = String.valueOf(selectedDoctor.getRating());
            doctorRating.setText("Rating: "+rating);
            doctorPhone.setText("Phone: "+selectedDoctor.getPhone());

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                hoursapp.clear();
                int day = datePicker.getDayOfMonth();
                int monthv = datePicker.getMonth() + 1; // Add 1 to get 1-based month index
                int yearv = datePicker.getYear();

                calendar = Calendar.getInstance();
                calendar.set(yearv, monthv - 1, day); // Subtract 1 to get 0-based month index
                currentDate = sdf.format(calendar.getTime()).trim();


                for(String h: hours){
                    found=false;
                    for(Appointment a : appointments){
                        if(a.getDoctor_id()==selectedDoctor.getId() && a.getDate().equals(currentDate) && Objects.equals(a.getTime(),h)){
                            hoursapp.add(h.replaceFirst(":00","") + " \nTaken");
                            found=true;
                            break;
                        }

                    }
                    if(!found){
                        hoursapp.add(h.replaceFirst(":00","")+ " \nAvailable");
                    }
                }

                //hoursGrid.setVisibility(View.VISIBLE);
                hoursGrid_adapter.notifyDataSetChanged();

            }
        });




        book.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(DoctorDetails.this,MakeAppointment.class);
                i.putExtra("doctor_name",selectedDoctor.getName());
                i.putExtra("doctor_id",selectedDoctor.getId());
                i.putExtra("consultation_cost",selectedDoctor.getConsultation_cost());
                i.putExtra("patient_id",patient.getId());
                startActivityForResult(i,1);
            }
        });

        hoursGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Scroll to the bottom of the ScrollView
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, scrollView.getBottom());
                    }
                });
            }
        });

        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setResult(2);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setResult(1);
                finish();
            }
        });
    }
    @Override
    public void onActivityResult(int request,int result,Intent i){
        switch (request){
            case 1:
                switch(result){
                    case 1:
                        Toast.makeText(DoctorDetails.this,"you did not book any appointment",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        setResult(2);
                        finish();
                        break;
                    case 3:
                        finish();
                        break;
                }
        }
    }
}
