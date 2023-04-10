package com.example.trustymed;

public class PaymentType {
    private int id;
    private String payment_method;

    public PaymentType(int id,String payment_method){
        this.id=id;
        this.payment_method=payment_method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_methods(String payment_method) {
        this.payment_method = payment_method;
    }

    @Override
    public String toString(){
        return this.getPayment_method();
    }
}
