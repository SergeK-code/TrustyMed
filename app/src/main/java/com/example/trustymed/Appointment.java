package com.example.trustymed;

public class Appointment {
    int id;
    String date;
    String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAttend() {
        return attend;
    }

    public void setAttend(int attend) {
        this.attend = attend;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    int attend;
    int cancel;
    int cost;
    int user_id;
    int doctor_id;
    int payment_id;

    public Appointment(int id,String date,String time,int attend,int cancel,int cost,int user_id,int doctor_id,int payment_id){
        this.id=id;
        this.date=date;
        this.time=time;
        this.attend=attend;
        this.cancel=cancel;
        this.cost=cost;
        this.user_id=user_id;
        this.doctor_id=doctor_id;
        this.payment_id=payment_id;
    }
}
