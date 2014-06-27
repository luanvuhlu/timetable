package com.bigbear.timetable;

import com.bigbear.control.TimeTableControl;
import com.bigbear.timetable.R;
import com.bigbear.control.ViewSetting;
import com.bigbear.entity.Settings;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by luan on 03/08/2013.
 */
public class SettingsActivity extends Activity {
	private TimeTableControl control;
    private static final int REQUEST_PATH=1;
    String curFileName;
    TextView fileNameChooser;
    String filePathChooser;
    Button brown;
    Settings settings;
    RelativeLayout relayAccounts;
    RelativeLayout relayAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        control=new TimeTableControl(this);
        setContentView(R.layout.activity_settings);
        relayAccounts=(RelativeLayout) findViewById(R.id.relativeAccounts);
        relayAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("com.bigbear.timetable.StudentList");
                startActivity(intent);
            }
        });
        relayAbout=(RelativeLayout) findViewById(R.id.relativeAbout);
        relayAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("com.bigbear.timetable.About");
                startActivity(intent);
            }
        });
        initSettings();
        brown=(Button) findViewById(R.id.btnBrown);
        brown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent("com.bigbear.timetable.FileExploder"), REQUEST_PATH);
            }

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==REQUEST_PATH){
            if (resultCode == RESULT_OK){
                fileNameChooser=(TextView) findViewById(R.id.fileNameChooser);
                curFileName=data.getStringExtra("GetFileName");
                filePathChooser=data.getStringExtra("GetPath")+"/"+curFileName;
                fileNameChooser.setText(filePathChooser);
                Toast.makeText(getBaseContext(), curFileName, Toast.LENGTH_SHORT).show();
                control.writeSubjects(filePathChooser);
            }
        }
    }
    private void initSettings(){
        settings=ViewSetting.readSettings(this);
        if (settings==null){
            settings=new Settings();
        }
    }
}