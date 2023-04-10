package com.example.trustymed;

import java.io.Serializable;

public class Patient implements Serializable {
    private int id;
    private String first_name;
    private String last_name;
    private String gender;
    private String phone;
    private String address;
    private String dob;
    private String email;
    private String password;

    public Patient(int id,String first_name,String last_name,String gender,String phone,String address,String dob,String email,String password){
        this.id=id;
        this.first_name=first_name;
        this.last_name=last_name;
        this.gender=gender;
        this.phone=phone;
        this.address=address;
        this.dob=dob;
        this.email=email;
        this.password=password;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
