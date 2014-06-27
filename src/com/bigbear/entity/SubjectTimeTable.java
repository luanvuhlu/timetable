package com.bigbear.entity;

/**
 * Created by luan on 18/08/2013.
 */
public class SubjectTimeTable{
    private String Hours;
    private String Name;
    private String Location;
    public SubjectTimeTable(String Hours, String Name, String Location){
        this.Hours=Hours;
        this.Name=Name;
        this.Location=Location;
    }
    public String getName(){
        return this.Name;
    }
    public String getHours(){
        return this.Hours;
    }
    public String getLocation(){
        return this.Location;
    }

}
