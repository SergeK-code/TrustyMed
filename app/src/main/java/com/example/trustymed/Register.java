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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

public class Register extends Activity {
    private static final List<String> Gender =Arrays.asList("male","female","other");
    private GetPatients getP;
    private EditText fname,lname,address,phone,email,newpass,newpassconf;
    private TextView error;
    private DatePicker dob;
    private ArrayAdapter<String> genderAdapter;
    private Spinner gender;
    private Button register,back;
    private String selectedGender,selectedDate;
    private SetPatient setP;
    private String response;
    private Calendar calendar,today;
    private SimpleDateFormat sdf;
    ArrayList<Patient> patients= new ArrayList<>();

    private String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private String nameRegex = "^[a-zA-Z]*$";
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register);

        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        dob=findViewById(R.id.dob);
        gender= findViewById(R.id.gender);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        email= findViewById(R.id.email);
        newpass= findViewById(R.id.newpass);
        newpassconf=findViewById(R.id.newpasconf);
        error= findViewById(R.id.error);
        register= findViewById(R.id.register);
        back=findViewById(R.id.back);


        today = Calendar.getInstance();
        dob.setMaxDate(today.getTimeInMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // to avoid unexpected parsing behavior
        selectedDate= sdf.format(today.getTimeInMillis()).trim();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dob.setSpinnersShown(true);
            dob.setCalendarViewShown(false);
        } else {
            // For older Android versions, use reflection to set the mode
            try {
                Field datePickerModeField = DatePicker.class.getDeclaredField("mDatePickerMode");
                datePickerModeField.setAccessible(true);
                datePickerModeField.setInt(dob, 2);// 2 for spinner mode
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        genderAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedGender = (String) parent.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                patients.clear();
                getP= new GetPatients(Register.this);
                try {
                     patients = getP.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                error.setText("");
                error.setVisibility(View.GONE);
                if(!(fname.getText().toString().trim().isEmpty() && lname.getText().toString().trim().isEmpty()
                        && address.getText().toString().trim().isEmpty()
                        && phone.getText().toString().trim().isEmpty() && email.getText().toString().trim().isEmpty()
                        && newpass.getText().toString().trim().isEmpty()
                        && newpassconf.getText().toString().trim().isEmpty())){

                    Pattern emailPattern = Pattern.compile(emailRegex);
                    Matcher emailMatcher = emailPattern.matcher(email.getText().toString().trim());
                    Pattern namePattern = Pattern.compile(nameRegex);
                    Matcher fnameMatcher = namePattern.matcher(fname.getText().toString().trim());
                    Matcher lnameMatcher = namePattern.matcher(lname.getText().toString().trim());



                    if(!fnameMatcher.matches()){
                        String t = error.getText().toString();
                        error.setText(t+"\nName should only contain letters");
                        error.setVisibility(View.VISIBLE);
                    }

                    if(!lnameMatcher.matches()){
                        String t = error.getText().toString();
                        error.setText(t+"\nLast name should only contain letters");
                        error.setVisibility(View.VISIBLE);
                    }

                    if(!emailMatcher.matches()){
                        String t = error.getText().toString();
                        error.setText(t+"\nEmail should be in format: example.example@something.com");
                        error.setVisibility(View.VISIBLE);
                    }

                    for(Patient patient : patients){
                        if(email.getText().toString().trim().equals(patient.getEmail())){
                            String t = error.getText().toString();
                            error.setText(t+"\nEmail already taken");
                        }
                    }

                    if( newpass.getText().toString().trim().length()<8){
                        String t = error.getText().toString();
                        error.setText(t+"\nPassword must be more than 8 characters");
                        error.setVisibility(View.VISIBLE);
                    }

                    if(!newpassconf.getText().toString().trim().equals(newpass.getText().toString().trim())){
                        String t = error.getText().toString();
                        error.setText(t+"\nPasswords do not match");
                        error.setVisibility(View.VISIBLE);
                    }


                    if(error.getVisibility()==View.GONE){
                        String pass2a,pass2y;
                        pass2a= BCrypt.hashpw(newpass.getText().toString().trim(),BCrypt.gensalt(10));
                        pass2y= pass2a.replaceFirst("2a","2y");
                        setP= new SetPatient(Register.this, fname.getText().toString().trim(), lname.getText().toString().trim(),selectedGender,phone.getText().toString().trim(),address.getText().toString().trim(),selectedDate,email.getText().toString().trim(),pass2y);
                        try {
                            response= setP.execute().get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Register.this,response,Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register.this,SplashScreen.class);
                        startActivity(i);
                    }
                }
                else{
                    error.setText("You must fill all the fields");
                    error.setVisibility(View.VISIBLE);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        dob.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                int day = datePicker.getDayOfMonth();
                int monthv = datePicker.getMonth() + 1; // Add 1 to get 1-based month index
                int yearv = datePicker.getYear();

                calendar = Calendar.getInstance();
                calendar.set(yearv, monthv - 1, day); // Subtract 1 to get 0-based month index
                selectedDate = sdf.format(calendar.getTime()).trim();

            }
        });




    }
}
