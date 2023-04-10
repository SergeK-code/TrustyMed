package com.example.trustymed;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class Doctor implements Serializable {
   private int id;
    private String name;
    private String phone;
    private String bio;
    private int rating;
    private int is_available;
    private int consultation_cost;
    private int speciality_id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getBio() {
        return bio;
    }

    public int getRating() {
        return rating;
    }

    public int getIs_available() {
        return is_available;
    }

    public int getConsultation_cost() {
        return consultation_cost;
    }

    public int getSpeciality_id() {
        return speciality_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }

    public void setConsultation_cost(int consultation_cost) {
        this.consultation_cost = consultation_cost;
    }

    public void setSpeciality_id(int speciality_id) {
        this.speciality_id = speciality_id;
    }



    public Doctor(int id, String name, String phone, String bio, int rating, int is_available, int consultation_cost, int speciality_id) {
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.bio=bio;
        this.rating=rating;
        this.is_available=is_available;
        this.consultation_cost=consultation_cost;
        this.speciality_id=speciality_id;
    }

    @Override
    public String toString(){
        return this.getName();
    }

}

