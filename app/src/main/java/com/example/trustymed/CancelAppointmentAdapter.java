package com.example.trustymed;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CancelAppointmentAdapter extends ArrayAdapter<Appointment> {
    private ArrayList<Appointment> myAppointments;
    private ArrayList<Doctor> doctorsList;
    private int patient_id;
    private Context context;
    private String response;

    public CancelAppointmentAdapter(Context context,int patient_id,ArrayList<Appointment> myAppointments,ArrayList<Doctor> doctorsList) {
        super(context, 0, myAppointments);
        this.context = context;
        this.patient_id=patient_id;
        this.myAppointments=myAppointments;
        this.doctorsList=doctorsList;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem=convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.cancel_appointment_list_item, parent, false);
        }

         Appointment appointment = myAppointments.get(position);

        TextView nameTextView = listItem.findViewById(R.id.NameTextView);
        TextView dateTextView = listItem.findViewById(R.id.DateTextView);
        TextView timeTextView = listItem.findViewById(R.id.TimeTextView);
        Button cancelButton = listItem.findViewById(R.id.cancel);

        dateTextView.setText("Date: "+appointment.getDate());
        timeTextView.setText("Time: "+appointment.getTime());
        for(Doctor dr: doctorsList){
            if(appointment.getDoctor_id()==dr.getId()){
                nameTextView.setText("Dr. "+dr.getName());
                break;
            }
        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Appointment Cancellation");
                alertDialogBuilder.setMessage("Do you want to cancel this appointment?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteAppointment del = new DeleteAppointment(context,appointment.getId());
                        try {
                            response= del.execute().get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(response.contains("successfully")){
                            myAppointments.remove(position);
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, response,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return listItem;
    }
}
