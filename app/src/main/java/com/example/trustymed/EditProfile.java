package com.example.trustymed;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

public class EditProfile extends Activity {
    private static final List<String> Gender =Arrays.asList("male","female","other");
    private int selectedGenderPosition;
    private GetPatients getP;
    private EditText fname,lname,address,phone,email,currpass,newpass,newpassconf;
    private TextView error;
    private DatePicker dob;
    private ArrayAdapter<String> genderAdapter;
    private Spinner gender;
    private Button save,home;
    private String selectedGender,selectedDate;
    private UpdatePatient upP;
    private String response;
    private Calendar calendar,today;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");;
    private ArrayList<Patient> patients= new ArrayList<>();
    private Patient currPatient;


    private String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private String nameRegex = "^[a-zA-Z]*$";
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.edit_profile);
        initViews();

        currPatient= (Patient) getIntent().getSerializableExtra("patient");

        setViews(currPatient);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedGender = (String) parent.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        if(calendar!=null) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Set the date on the Date Picker
            dob.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {

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

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                patients.clear();
                getP= new GetPatients(EditProfile.this);
                try {
                    patients = getP.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }


                updatePatient();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

    }

    private void initViews(){
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        dob=findViewById(R.id.dob);
        gender= findViewById(R.id.gender);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        email= findViewById(R.id.email);
        currpass= findViewById(R.id.currpass);
        newpass= findViewById(R.id.newpass);
        newpassconf=findViewById(R.id.newpasconf);
        error= findViewById(R.id.error);
        save= findViewById(R.id.save);
        home=findViewById(R.id.home);
    }

    private void setViews(Patient currPatient){
        fname.setText(currPatient.getFirst_name());
        lname.setText(currPatient.getLast_name());
        address.setText(currPatient.getAddress());
        phone.setText(currPatient.getPhone());
        email.setText(currPatient.getEmail());

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

        Date date;
        try {
            date = sdf.parse(currPatient.getDob());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        selectedDate = sdf.format(calendar.getTime()).trim();

        listOfGender();
    }

    private void listOfGender(){
        genderAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        for(selectedGenderPosition=0;selectedGenderPosition<Gender.size();selectedGenderPosition++){
            if(currPatient.getGender().equalsIgnoreCase(Gender.get(selectedGenderPosition))){
                gender.setSelection(selectedGenderPosition);
                break;
            }
        }
    }

    private void updatePatient(){

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        error.setText("");
        error.setVisibility(View.GONE);
        if(!(fname.getText().toString().trim().isEmpty() && lname.getText().toString().trim().isEmpty()
                && address.getText().toString().trim().isEmpty()
                && phone.getText().toString().trim().isEmpty() && email.getText().toString().trim().isEmpty())){

            Pattern emailPattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPattern.matcher(email.getText().toString().trim());
            Pattern namePattern = Pattern.compile(nameRegex);
            Matcher fnameMatcher = namePattern.matcher(fname.getText().toString().trim());
            Matcher lnameMatcher = namePattern.matcher(lname.getText().toString().trim());
            String curr2Ypass = currPatient.getPassword().toString().trim();
            String curr2apass= curr2Ypass.replaceFirst("2y","2a");

            if(currpass.getText().toString().trim().isEmpty()){
                String t = error.getText().toString();
                error.setText(t+"\nEnter current password to perform any change");
                error.setVisibility(View.VISIBLE);
            }
            else if(!BCrypt.checkpw(currpass.getText().toString().trim(),curr2apass)){
                String t = error.getText().toString();
                error.setText(t+"\nCurrent password is wrong");
                error.setVisibility(View.VISIBLE);
            }

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

            }

            for(Patient patient : patients){
                if(email.getText().toString().trim().equals(patient.getEmail()) && patient.getId()!=currPatient.getId()){
                    String t = error.getText().toString();
                    error.setText(t+"\nEmail already taken");
                    error.setVisibility(View.VISIBLE);
                }
            }

            if( !newpass.getText().toString().trim().isEmpty() && newpass.getText().toString().trim().length()<8){
                String t = error.getText().toString();
                error.setText(t+"\nPassword must be more than 8 characters");
                error.setVisibility(View.VISIBLE);
            }

            if(!newpassconf.getText().toString().trim().equals(newpass.getText().toString().trim())){
                String t = error.getText().toString();
                error.setText(t+"\nNew password do not match");
                error.setVisibility(View.VISIBLE);
            }
        }
        else{
            error.setText("You must fill all the fields");
            error.setVisibility(View.VISIBLE);
        }

        if(error.getVisibility()==View.GONE){
            String pass,pass2y;
            if(!newpass.getText().toString().trim().isEmpty()){
                editor.putString("password", newpass.getText().toString().trim());
                pass= BCrypt.hashpw(newpass.getText().toString().trim(),BCrypt.gensalt(10));
                pass2y= pass.replaceFirst("2a","2y");
            }
            else{
                pass2y=currPatient.getPassword();
            }
            editor.putString("email", email.getText().toString().trim());
            editor.apply();
            upP= new UpdatePatient(EditProfile.this,currPatient.getId(), fname.getText().toString().trim(), lname.getText().toString().trim(),selectedGender,phone.getText().toString().trim(),address.getText().toString().trim(),selectedDate,email.getText().toString().trim(),pass2y);
            try {
                response= upP.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Toast.makeText(EditProfile.this,response,Toast.LENGTH_SHORT).show();
            Log.e("responseEdit",response);
            setResult(2);
            finish();
        }
    }
}
