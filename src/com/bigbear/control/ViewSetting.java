package com.bigbear.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.bigbear.entity.Settings;

/**
 * Created by luan on 31/07/2013.
 */
public class ViewSetting {
    public static String FILE_SETTINGS_DATA="SettingsData";
    public static Settings readSettings(Context context){
        Settings settings=null;
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        try{
            fis=context.openFileInput(FILE_SETTINGS_DATA);
            ois=new ObjectInputStream(fis);
            settings=(Settings) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("TimeTable", e.toString());
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            Log.d("TimeTable", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TimeTable", e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("TimeTable", e.toString());
        }
        if (settings==null)
            settings=new Settings();
        return settings;
    }
    public static boolean writeSettings(Settings settings, Context context){
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try{
            fos=context.openFileOutput(FILE_SETTINGS_DATA, Context.MODE_PRIVATE);
            oos=new ObjectOutputStream(fos);
            oos.writeObject(settings);
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("TimeTable", e.toString());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TimeTable", e.toString());
            return false;
        }
    }
}
