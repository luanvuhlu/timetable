package com.bigbear.entity;

import java.io.Serializable;

/**
 * Created by luan on 12/08/2013.
 */
public class Account implements Serializable {
    private int TextColor;
    private int FontSize;
    private boolean Display;
    public Account(){
        this.Display=true;
        this.TextColor=-1;
        this.FontSize=18;
    }
    public void setTextColor(int TextColor){
        this.TextColor=TextColor;
    }
    public void setFontSize(int FontSize){
        this.FontSize=FontSize;
    }
    public void setDisplay(boolean Display){
        this.Display=Display;
    }
    public int getTextColor(){
        return this.TextColor;
    }
    public int getFontSize(){
        return this.FontSize;
    }
    public boolean checkDisplay(){
        return this.Display;
    }
}
