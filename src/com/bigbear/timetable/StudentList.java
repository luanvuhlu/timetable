package com.bigbear.timetable;

import java.util.List;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bigbear.control.TimeTableControl;
import com.bigbear.control.ViewSetting;
import com.bigbear.entity.Settings;
import com.bigbear.entity.Subjects;


/**
 * Created by luan on 11/08/2013.
 */
public class StudentList extends ListActivity {
    Settings settings;
    List<String> accountsList;
    private int positionSub;
    List<Subjects> subsList;
    private TimeTableControl control;
    public static final String ACCOUNT_KEY="account";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        control=new TimeTableControl(this);
        subsList=control.readSubjectsList();
        ListView listView=getListView();
        listView.setTextFilterEnabled(true);

        settings=ViewSetting.readSettings(this);
        accountsList=control.getAccountsList();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, accountsList));
    }
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id){
        if (!settings.accounts.isEmpty()){
            positionSub=position;
            AccountSetting();
        }
    }
    private void AccountSetting(){
        String Name;
        Name=accountsList.get(positionSub);
        Intent intent=new Intent("com.bigbear.timetable.AccountSettings");
        intent.putExtra(ACCOUNT_KEY, Name);
        startActivity(intent);

    }
}
