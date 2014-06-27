package com.bigbear.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by luan on 30/07/2013.
 */
public class Subject implements Serializable {
    private String Code;
    private String Name;
    private String ClassSub;
    private boolean hasSerminal;
    private Date[] Period = new Date[2];
    private Schedule schedule;
    private boolean Week15 = false;

    public Subject() {
    }

    public Subject(String Code, boolean hasSerminal) {
        this.Code = Code;
        this.hasSerminal = hasSerminal;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setClassSub(String ClassSub) {
        this.ClassSub = ClassSub;
    }

    public void setHasSerminal(boolean hasSerminal) {
        this.hasSerminal = hasSerminal;
    }

    public void setPeriod(Date t1, Date t2) {
        this.Period[0] = t1;
        this.Period[1] = t2;
    }

    public void setschedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setWeek15(boolean Week15) {
        this.Week15 = Week15;
    }

    public String getCode() {
        return this.Code;
    }

    public String getName() {
        return this.Name;
    }

    public String getClassSub() {
        return this.ClassSub;
    }

    public Schedule getschedule() {
        return this.schedule;
    }

    public boolean checkWeek15() {
        return this.Week15;
    }

    public boolean checkSerminal() {
        return this.hasSerminal;
    }

    public Date[] getPeriod() {
        return this.Period;
    }

}
