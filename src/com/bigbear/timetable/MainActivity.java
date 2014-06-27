package com.bigbear.timetable;


import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bigbear.control.TimeTableControl;
import com.bigbear.entity.Subjects;

public class MainActivity extends Activity {
    Calendar calendar = Calendar.getInstance();
    Date today = new Date();
    Subjects subjects;
    static int ACTION_TYPE_RIGHT=1;
    static int ACTION_TYPE_LEFT=2;
    static int RANGE=100;
    int action_type;
    float startX;
    float startY;
    
    TimeTableControl control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date date = new Date();
        calendar.setTime(date);
        control=new TimeTableControl(this);
        control.display(date);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);
        super.dispatchTouchEvent(event);
        float x=event.getRawX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX=event.getRawX();
                startY=event.getRawY();
                action_type=0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (startX - x > RANGE)
                    action_type = ACTION_TYPE_LEFT;
                else if (x - startX > RANGE)
                    action_type = ACTION_TYPE_RIGHT;
                break;
            case MotionEvent.ACTION_UP:
                if (action_type==ACTION_TYPE_LEFT)
                    onSwipeLeft();
                else if (action_type==ACTION_TYPE_RIGHT)
                    onSwipeRight();
                break;
            default:
                break;
        }
        return scrollView.onTouchEvent(event);
    }

    private void changeDay(int day) {
        calendar.add(Calendar.DATE, day);
        Date date = calendar.getTime();
        control.display(date);
    }
    private void onSwipeRight(){
        changeDay(-1);
    }
    private void onSwipeLeft(){
        changeDay(1);
    }

    // Menu Settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
          switch(item.getItemId()){
            case R.id.action_goto:
                DatePickerDialog dateDialog=new DatePickerDialog(this, mDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.show();
                return true;
            case R.id.action_settings:
                startActivity(new Intent("com.bigbear.timetable.SettingsActivity"));
                return true;
              case R.id.action_subName:
                  startActivity(new Intent("com.bigbear.timetable.SubjectListActivity"));
                  return true;
            default:
                Toast.makeText(getBaseContext(), "Don't know !", Toast.LENGTH_SHORT).show();

        }
        return false;
    }
    private DatePickerDialog.OnDateSetListener mDateListener=new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            Calendar calen=Calendar.getInstance();
            calen.set(y, m, d);
            dateSet(calen.getTime());
        }
    };
    private void dateSet(Date date){
        calendar.setTime(date);
        control.display(date);
       

    }
    
}
