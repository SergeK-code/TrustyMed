package com.example.trustymed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DoctorAdapter extends ArrayAdapter<Doctor> {

    private Context context;
    private ArrayList<Doctor> doctors;
    private ArrayList<Specialty> specialties;
    String currentSpecialty;

    public DoctorAdapter(Context context, ArrayList<Doctor> doctors,ArrayList<Specialty> specialties) {
        super(context, 0, doctors);
        this.context = context;
        this.doctors = doctors;
        this.specialties = specialties;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.doctor_list_item, parent, false);
        }

        Doctor currentDoctor = doctors.get(position);

        ImageView doctorImage = (ImageView) listItem.findViewById(R.id.coverImageView);
        doctorImage.setImageResource(R.drawable.doctor_logo);


        TextView doctorName = (TextView) listItem.findViewById(R.id.NameTextView);
        doctorName.setText("Name: "+currentDoctor.getName());

        TextView doctorSpecialty = (TextView) listItem.findViewById(R.id.SpecialtyTextView);
        for(Specialty specialty : specialties){
            if(specialty.getId()==currentDoctor.getSpeciality_id()){
                 currentSpecialty= specialty.getName();
                break;
            }
        }
        doctorSpecialty.setText("Specialty: "+currentSpecialty);

        TextView doctorRating = (TextView) listItem.findViewById(R.id.RatingTextView);
        String rating = String.valueOf(currentDoctor.getRating());
        doctorRating.setText("Rating: "+rating);

        return listItem;
    }
}
