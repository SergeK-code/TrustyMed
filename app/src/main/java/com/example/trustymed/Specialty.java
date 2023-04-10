package com.example.trustymed;

public class Specialty {

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private int id;
    private String name;

    public Specialty(int id,String name) {
        this.id=id;
        this.name=name;
    }

    @Override
    public String toString(){
        return this.getName();
    }


}
