package com.example.trustymed;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MakeAppointment extends Activity {
    private Bundle bundle;
    private TextView title;
    private DatePicker datePicker;
    private Spinner timeSpinner,paymentSpinner;
    private Button confirm,back,home;
    private SetAppointment setAppointment;
    private GetPaymentTypes getPaymentTypes;
    private ArrayList<PaymentType> paymentType;
    private Calendar today,tomorrow;
    private final List<String> hours= DoctorDetails.hours;
    private ArrayAdapter<String> timeAdapter;
    private ArrayAdapter<PaymentType> paymentAdapter;
    private String selectedTime;
    private String selectedDate;
    private int selectedPaymentTypeId;
    private GetAppointments currentAppointments;
    public ArrayList<Appointment> currentAppointmentsList;
    private boolean taken= false;
    private int doctor_id,patient_id,consultation_cost;
    private String doctor_name;
    private String response;
    private SimpleDateFormat sdf;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.make_appointment);

        bundle = getIntent().getExtras();
        title= findViewById(R.id.title);
        datePicker= findViewById(R.id.datePicker);
        timeSpinner=findViewById(R.id.timeSpinner);
        paymentSpinner=findViewById(R.id.paymentSpinner);
        confirm= findViewById(R.id.confirm);
        home= findViewById(R.id.home);
        back= findViewById(R.id.back);
        doctor_name= bundle.getString("doctor_name");
        doctor_id= bundle.getInt("doctor_id");
        consultation_cost= bundle.getInt("consultation_cost");
        patient_id= bundle.getInt("patient_id");
        title.setText("Book an appointment with\nDoctor "+doctor_name);

        tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        datePicker.setMinDate(tomorrow.getTimeInMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // to avoid unexpected parsing behavior
        selectedDate = sdf.format(tomorrow.getTime()).trim();

        timeAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,hours);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                int day = datePicker.getDayOfMonth();
                int monthv = datePicker.getMonth() + 1; // Add 1 to get 1-based month index
                int yearv = datePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(yearv, monthv - 1, day); // Subtract 1 to get 0-based month index


                selectedDate = sdf.format(calendar.getTime()).trim();
            }
        });

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime= (String) parent.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getPaymentTypes= new GetPaymentTypes(this);
        try {
            paymentType= getPaymentTypes.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

            paymentAdapter= new ArrayAdapter<PaymentType>(this,android.R.layout.simple_spinner_item,paymentType);
            paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            paymentSpinner.setAdapter(paymentAdapter);

            paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PaymentType payment= (PaymentType) parent.getAdapter().getItem(position);
                    selectedPaymentTypeId = payment.getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                currentAppointments= new GetAppointments(MakeAppointment.this);
                try {
                    currentAppointmentsList= currentAppointments.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                    for(Appointment a : currentAppointmentsList){
                        if(doctor_id==a.getDoctor_id() && Objects.equals(a.getDate(), selectedDate) && Objects.equals(a.getTime(), selectedTime)){
                            taken=true;
                            break;
                        }
                    }
                    if(taken==true){
                        String message="Sorry this appointment is already booked";
                        recreate();
                        Toast.makeText(MakeAppointment.this,message,Toast.LENGTH_SHORT).show();
                    }
                    else {

                        setAppointment = new SetAppointment(MakeAppointment.this,selectedDate, selectedTime, consultation_cost, patient_id, doctor_id, selectedPaymentTypeId);
                        try {
                            response= setAppointment.execute().get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(response!=null) {
                            Toast.makeText(MakeAppointment.this, response, Toast.LENGTH_SHORT).show();
                        }
                        setResult(3);
                        finish();
                    }

                }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setResult(1);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setResult(2);
                finish();
            }
        });

    }
}
