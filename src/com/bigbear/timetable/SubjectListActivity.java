package com.bigbear.timetable;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.bigbear.control.TimeTableControl;
import com.bigbear.entity.Subject;
import com.bigbear.entity.Subjects;

/**
 * Created by luan on 18/08/2013.
 */
public class SubjectListActivity extends ListActivity {
	TimeTableControl control;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        control=new TimeTableControl(this);
        List<String> subName= new ArrayList<String>();
        List<Subjects> subjectsList= control.getSubjectsList();
        if(subjectsList==null || subjectsList.isEmpty()){
            subName.add("Not any subject !");
        }else
        for(Subjects subs:subjectsList){
            subName.add(subs.getNameStudent().toUpperCase());
            int i=0;
            for(Subject sub:subs.getsubjects()){
                subName.add(" "+(++i)+". "+sub.getName()+" - "+sub.getClassSub());
            }
            subName.add(" ");
        }

        setListAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, subName));

    }
}
