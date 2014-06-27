package com.bigbear.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by luan on 30/07/2013.
 */
public class Subjects implements Serializable{
    private ArrayList<Subject> subjects = new ArrayList<Subject>();
    private String NameStudent;

    public Subjects() {}
    public Subjects(String Name){
        this.NameStudent=Name;
    }
    public String getNameStudent(){
        return this.NameStudent;
    }
    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public ArrayList<Subject> getsubjects() {
        return this.subjects;
    }

    @Override
    public String toString() {
        return this.getsubjects().toString();
    }
}
