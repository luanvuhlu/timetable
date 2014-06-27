package com.bigbear.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by luan on 30/07/2013.
 */
public class WeekDay implements Serializable{
    private String Name;
    private ArrayList<Integer> Hours = new ArrayList<Integer>();
    private String Location;
    private boolean Serminal = false;

    public WeekDay(String time, boolean serminal) {
        this.setSerminal(serminal);
        StringTokenizer token = new StringTokenizer(time, " ");
        while (token.hasMoreTokens()) {
            try {
                String str = token.nextToken();
                if (str.equals("Thứ"))
                    this.setName(token.nextToken());
                if (str.equals("tiết")) {
                    this.setHours(token.nextToken());
                }
                if (str.equals("tại"))
                    this.setLocation(token.nextToken());
            } catch (NoSuchElementException ex) {

            }
        }
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setHours(String hours) {
        StringTokenizer token = new StringTokenizer(hours, ",");
        while (token.hasMoreTokens()) {
            this.Hours.add(Integer.parseInt(token.nextToken()));
        }
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public void setSerminal(boolean Serminal) {
        this.Serminal = Serminal;
    }

    public String getName() {
        return this.Name;
    }

    public ArrayList<Integer> getHours() {
        return this.Hours;
    }

    public String getLocation() {
        return this.Location;
    }

    public boolean checkSerminal() {
        return this.Serminal;
    }

}
