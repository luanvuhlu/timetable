package com.bigbear.timetable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.bigbear.timetable.R;
import com.bigbear.control.TimeTableControl;
import com.bigbear.control.ViewSetting;
import com.bigbear.entity.Account;
import com.bigbear.entity.Settings;
import com.bigbear.entity.Subjects;


/**
 * Created by luan on 12/08/2013.
 */
public class AccountSettings extends Activity {
	private TimeTableControl control;
    Spinner textColorSpinner;
    CheckBox display;
    SeekBar fontSize;
    TextView txtDemo;
    LinearLayout linearDelete;
    Settings settings;
    Account account;
    int textColor=-1;
    String NameAccount;
    String[][] colors_array={
            {"Black", "-16777216"},
            {"DK Gray", "-12303292"},
            {"Gray", "-7829368"},
            {"LT Gray", "-3355444"},
            {"White", "-1"},
            {"Red", "-65536"},
            {"Green", "-16711936"},
            {"Blue", "-16776961"},
            {"Yellow", "-256"},
            {"Cyan", "-16711681"},
            {"Magenta", "-65281"},
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        control=new TimeTableControl(this);
        setContentView(R.layout.activity_account);

        settings=ViewSetting.readSettings(this);
        display=(CheckBox) findViewById(R.id.checkBox);
        fontSize=(SeekBar) findViewById(R.id.seekBar);
        textColorSpinner=(Spinner) findViewById(R.id.spinner);
        txtDemo =(TextView) findViewById(R.id.txtDemo);

        Bundle extras=getIntent().getExtras();
        if (extras==null){
            return;
        }else{
            NameAccount=extras.getString(StudentList.ACCOUNT_KEY);
        }
        account=settings.accounts.get(NameAccount);
        if (account==null){
            return;
        }
        setTitle(NameAccount);
        linearDelete=(LinearLayout) findViewById(R.id.linearDelete);
        linearDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirm();
            }
        });
        display.setChecked(account.checkDisplay());
        fontSize.setProgress(account.getFontSize());
        textColor=account.getTextColor();

        String[] colors=new String[colors_array.length];
        for (int i=0; i<colors_array.length; i++){
            colors[i]=colors_array[i][0];
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colors);
        textColorSpinner.setAdapter(adapter);
        // Load TEXT COLOR from setting
        for (int i=0; i<colors_array.length; i++){
            if (colors_array[i][1].equals(textColor+"")){
                textColorSpinner.setSelection(i);
                break;
            }
        }
        // Set Color for Text Demo
        textColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtDemo.setTextColor(Integer.parseInt(colors_array[i][1]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Set Text Size for Text Demo
        fontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtDemo.setTextSize(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    // Write data
    private void saveData(){
        boolean changeColor=true;
        boolean changeDisplay=false;
        boolean changeFontSize=false;
        // Check change DISPLAY
        if (display.isChecked() != account.checkDisplay()){
            changeDisplay=true;
        }
        // Check change FONT SIZE
        if (fontSize.getProgress() != account.getFontSize()){
            changeFontSize=true;
        }
        // Check change COLOR
        for (int i=0; i<colors_array.length; i++){
            if (colors_array[i][0].equals(textColorSpinner.getSelectedItem().toString())
                    && colors_array[i][1].equals(account.getTextColor()+"")){
                changeColor=false;
                break;

            }
        }
        // If change, WRITE
        if (changeColor || changeDisplay || changeFontSize){
            account.setDisplay(display.isChecked());
            account.setFontSize(fontSize.getProgress());
            int color=-1;
            int pos=textColorSpinner.getSelectedItemPosition();
            color=Integer.parseInt(colors_array[pos][1]);
            account.setTextColor(color);
            settings.accounts.put(NameAccount, account);
            ViewSetting.writeSettings(settings, this);
        }
    }
    // Delete Account
    private boolean delete(){
        // Delete in SETTING
        settings.accounts.remove(NameAccount);
        ViewSetting.writeSettings(settings, this);
        // Delete in DATA
        List<Subjects> subjectsList=control.getSubjectsList();
        for (Subjects subjects: subjectsList){
            if (subjects.getNameStudent().equals(NameAccount)){
                subjectsList.remove(subjects);
                control.writeSubjects(subjectsList, TimeTableControl.FILE_SUBJECTS_DATA);
                break;
            }
        }
        this.finish();
        return true;
    }
    // Show confirm Delete
    private void showDeleteConfirm(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure about deleting "+NameAccount)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    // If Stop, Write Data
    @Override
    public void onStop(){
        super.onStop();
        saveData();
    }
}
