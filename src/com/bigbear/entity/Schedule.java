package com.bigbear.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by luan on 30/07/2013.
 */
public class Schedule implements Serializable{
    private ArrayList<WeekDay> weekDays = new ArrayList<WeekDay>();

    public Schedule() {
    }

    public Schedule(String[] time) {
        this.addweekDay(time[0], false);
        if (!time[1].isEmpty()) {
            this.addweekDay(time[1], true);
        }
    }

    public void addweekDay(String time, boolean serminal) {
        StringTokenizer tokenLine = new StringTokenizer(time, "\n");
        tokenLine.nextToken();
        while (tokenLine.hasMoreTokens()) {
            try {
                String line = tokenLine.nextToken();
                WeekDay wd = new WeekDay(line, serminal);
                this.weekDays.add(wd);
            } catch (NoSuchElementException ex) {
            }
        }

    }

    public ArrayList<WeekDay> getAllweekday() {
        return this.weekDays;
    }

}
