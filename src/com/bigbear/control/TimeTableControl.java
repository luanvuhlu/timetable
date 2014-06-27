package com.bigbear.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigbear.timetable.R;
import com.bigbear.entity.Account;
import com.bigbear.entity.Schedule;
import com.bigbear.entity.Settings;
import com.bigbear.entity.Subject;
import com.bigbear.entity.SubjectTimeTable;
import com.bigbear.entity.Subjects;
import com.bigbear.entity.WeekDay;

/**
 * Created by luan on 30/07/2013.
 */
public class TimeTableControl {
    public static String FILE_SUBJECTS_DATA="SubjectsData";
    private static final String DEBUG_TAG="TIMETABLE_CONTROL";
    private Activity context;
    private List<Subjects> subjectsList;
    public TimeTableControl() {}
    public TimeTableControl(Activity context) {
    	this.context=context;
    	subjectsList=readSubjectsList();
    }
    public List<Subjects> getSubjectsList() {
		return subjectsList;
	}
    public void display(Date date) {
        // Set Title
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Calendar today=Calendar.getInstance();
        today.setTime(new Date());
        String title="Today";
        if (today.get(Calendar.DATE)==calendar.get(Calendar.DATE)
            && today.get(Calendar.MONTH)==calendar.get(Calendar.MONTH)
            && today.get(Calendar.YEAR)==calendar.get(Calendar.YEAR))
            title = "Today";
        else
            if (calendar.get(Calendar.DAY_OF_WEEK)==1)
                title=("Chủ nhật "+", "+" Ngày "+calendar.get(Calendar.DAY_OF_MONTH)+ "/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
            else
                title = "Thứ " + calendar.get(Calendar.DAY_OF_WEEK) + " Ngày " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        context.setTitle(title);

        LinearLayout linearMain=(LinearLayout) context.findViewById(R.id.linearMain);

        // Refresh TableLayout
        int TotalRow=linearMain.getChildCount();
        for(int i=1; i<TotalRow; i++)
            for (int j=1; j<linearMain.getChildCount();){
                linearMain.removeViewAt(j);
                break;
            }
        for (Subjects subjects: subjectsList){
            Settings settings=null;
            settings=ViewSetting.readSettings(context);
            Account account=null;
            if (settings !=null){
                account=settings.accounts.get(subjects.getNameStudent());
                if (account==null || !account.checkDisplay()){
                    continue;
                }
            }
            int TextColor=account.getTextColor();
            int FontSize=account.getFontSize();
            List<SubjectTimeTable> list=null;
            try{
                 list= sortTimeTable(subjects, date);
            }catch(NullPointerException e){
                Log.d("TimeTable", e.toString());
            }
            if (list ==null || list.size()<1){
                continue;
            }
            for (SubjectTimeTable subject : list) {
                RelativeLayout relativeLayout=new RelativeLayout(context);
                LinearLayout.LayoutParams relativeParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                relativeParams.setMargins(0, 0, 0, 1);
                relativeLayout.setPadding(5, 40, 5, 40);
                relativeLayout.setBackgroundColor(Color.BLACK);

                TextView txtHours=new TextView(context);
                txtHours.setText(subject.getHours());
                txtHours.setTextSize(FontSize);
                txtHours.setTextColor(TextColor);
                txtHours.setMaxWidth(160);
                txtHours.setMinWidth(160);
                txtHours.setPadding(5, 0, 0, 0);
                RelativeLayout.LayoutParams hoursParams=new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                hoursParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                hoursParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


                TextView txtSujectName=new TextView(context);
                txtSujectName.setText(subject.getName());
                txtSujectName.setTextColor(TextColor);
                txtSujectName.setTextSize(FontSize);
                RelativeLayout.LayoutParams subjectNameParams=new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                subjectNameParams.setMargins(90, 0, 80, 0);
                subjectNameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                subjectNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                subjectNameParams.addRule(RelativeLayout.LEFT_OF, txtHours.getId());

                TextView txtLocation=new TextView(context);
                txtLocation.setText(subject.getLocation());
                txtLocation.setTextSize(FontSize);
                txtLocation.setTextColor(TextColor);
                txtLocation.setMaxWidth(160);
                txtLocation.setPadding(0, 0, 5, 0);
                RelativeLayout.LayoutParams locationParams=new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                locationParams.setMargins(10, 0, 0, 0);
                locationParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                locationParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                relativeLayout.addView(txtHours, hoursParams);
                relativeLayout.addView(txtSujectName, subjectNameParams);
                relativeLayout.addView(txtLocation, locationParams);
                linearMain.addView(relativeLayout, relativeParams);
            }
        }
    }
    public Subjects readSubjects(String FilePath) {
        Subjects subs = null;
        Workbook workbook = null;
        Subjects tmpSubs = new Subjects();
        File root= Environment.getExternalStorageDirectory();
        StringTokenizer token=new StringTokenizer(FilePath, "/");
        token.nextToken();
        token.nextToken();
        String index=token.nextToken();
        String file=FilePath.substring(FilePath.indexOf(index));
        File inFile=new File(root, file);
        Log.d(DEBUG_TAG, "Reading subject in file");
        try {
            workbook = Workbook.getWorkbook(inFile);
            Sheet sheet = workbook.getSheet(0);
            Cell baseCell = sheet.findCell("STT");
            int col = baseCell.getColumn();
            int row = baseCell.getRow();
            String NameStudent=sheet.getCell(col+2, row-4).getContents()+" - " + sheet.getCell(col+5, row-4).getContents();
            subs=new Subjects(NameStudent);
            System.out.println(col + " " + row);
            for (int i = 1; sheet.getCell(col, row + i).getType() == CellType.NUMBER; i++) {
                Subject sub = null;
                if (sheet.getCell(col+1, row + i).getContents().trim()!="")
                	if(sheet.getCell(col+5, row+i+1).getContents().trim() != "") {
                		sub=new Subject(sheet.getCell(col+1, row+i).getContents(), true);
                    } else
                    	sub=new Subject(sheet.getCell(col+1, row+i).getContents(), false);
                if (sub != null){
                	Log.d(DEBUG_TAG, "Reading subject: "+sub.getName());
                    tmpSubs.addSubject(sub);
                }else{
                	Log.d(DEBUG_TAG, "Subject is null: ");
                }
            }
            for (Subject sub : tmpSubs.getsubjects()) {
                Cell SubCell = sheet.findCell(sub.getCode());
                String nameSub = sheet.getCell(SubCell.getColumn() + 2, SubCell.getRow()).getContents();
                String classSub = null;
                String period[] = new String[2];
                String time_Sub[] = new String[2];
                if (sub.checkSerminal()) {
                    // class sub
                    String tmp1 = sheet.getCell(SubCell.getColumn() + 4, SubCell.getRow()).getContents();
                    String tmp2 = sheet.getCell(SubCell.getColumn() + 4, SubCell.getRow() + 1).getContents();
                    String classSub1 = tmp1.substring(tmp1.lastIndexOf("(") + 1, tmp1.lastIndexOf(")"));
                    String classSub2 = tmp2.substring(tmp2.lastIndexOf("(") + 1, tmp2.lastIndexOf(")"));
                    if (classSub1.length() > classSub2.length()) {
                        classSub = classSub1;
                        time_Sub[1] = sheet.getCell(SubCell.getColumn() + 6, SubCell.getRow()).getContents();
                        time_Sub[0] = sheet.getCell(SubCell.getColumn() + 6, SubCell.getRow() + 1).getContents();
                    } else {
                        classSub = classSub2;
                        time_Sub[0] = sheet.getCell(SubCell.getColumn() + 6, SubCell.getRow()).getContents();
                        time_Sub[1] = sheet.getCell(SubCell.getColumn() + 6, SubCell.getRow() + 1).getContents();
                    }
                    // period
                    period[0] = time_Sub[1].substring(time_Sub[1].indexOf("Từ") + 3, time_Sub[1].indexOf("đến") - 1);
                    period[1] = time_Sub[1].substring(time_Sub[1].indexOf("đến") + 4, time_Sub[1].indexOf(":"));

                } else {
                    // class sub
                    String tmp = sheet.getCell(SubCell.getColumn() + 4, SubCell.getRow()).getContents();
                    classSub = tmp.substring(tmp.lastIndexOf("(") + 1, tmp.lastIndexOf(")"));
                    // time
                    time_Sub[0] = sheet.getCell(SubCell.getColumn() + 6, SubCell.getRow()).getContents();
                    time_Sub[1] = "";
                    // period
                    period[0] = time_Sub[0].substring(time_Sub[0].indexOf("Từ") + 3, time_Sub[0].indexOf("đến") - 1);
                    period[1] = time_Sub[0].substring(time_Sub[0].indexOf("đến") + 4, time_Sub[0].indexOf(":"));
                }
                sub.setName(nameSub);
                
                sub.setClassSub(classSub);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date date1 = format.parse(period[0]);
                Date date2 = format.parse(period[1]);
                sub.setPeriod(date1, date2);
                if ((date2.getTime() - date1.getTime()) / (3600 * 24 * 1000) >= 105) {
                    sub.setWeek15(true);
                }
                sub.setschedule(new Schedule(time_Sub));
                subs.addSubject(sub);
            }
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
            Log.e("BiffException", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ParseException", e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("NullPointerException", e.getMessage());
        }
        Log.d(DEBUG_TAG, "Reading subject in file complete");
        return subs;
    }

    public void writeSubjects(String inFile) {
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        Subjects subs=null;
        List<Subjects> subjectsListLoc=subjectsList;
        subs=readSubjects(inFile);
        if(subs ==null){
            return;
        }
        Account account=new Account();
        Settings settings=null;
        settings=ViewSetting.readSettings(context);
        settings.accounts.put(subs.getNameStudent(), account);
        ViewSetting.writeSettings(settings, context);
        for(Subjects subjects: subjectsListLoc){
            if (subjects.getNameStudent().equals(subs.getNameStudent())){
            	subjectsListLoc.remove(subjects);
                break;
            }
        }
        subjectsListLoc.add(subs);
        try {
            fos= context.openFileOutput(FILE_SUBJECTS_DATA, Context.MODE_PRIVATE);
            oos=new ObjectOutputStream(fos);
            oos.writeObject(subjectsListLoc);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TimeTable", e.getMessage());
        }
    }

    public void writeSubjects(List<Subjects> subsList, String inFile) {
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try {
            fos= context.openFileOutput(FILE_SUBJECTS_DATA, Context.MODE_PRIVATE);
            oos=new ObjectOutputStream(fos);
            oos.writeObject(subsList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException", e.getMessage());
        }
    }

    public List<Subjects> readSubjectsList(){
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        List<Subjects> subjectsList=null;
        try{
            fis=context.openFileInput(FILE_SUBJECTS_DATA);
            ois=new ObjectInputStream(fis);
            subjectsList=(ArrayList<Subjects>)ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(VerifyError e){
        	e.printStackTrace();
        }
        if (subjectsList==null){
            subjectsList=new ArrayList<Subjects>();
        }
        return subjectsList;
    }
    

    public List<SubjectTimeTable> sortTimeTable(Subjects subjects, Date date) {
        List<SubjectTimeTable> list=null;
        if (subjects ==null)
            return list;
        list= new ArrayList<SubjectTimeTable>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) + "";
        for (Subject sub : subjects.getsubjects()) {
            if (date.getTime() < sub.getPeriod()[0].getTime() || date.getTime() > sub.getPeriod()[1].getTime())
                continue;
            Schedule schedule = sub.getschedule();
            for (WeekDay weekday : schedule.getAllweekday()) {
                if (weekday.getName().equals(dayOfWeek)) {
                    String hours = "";
                    for (int hour : weekday.getHours()) {
                        hours += hour + ", ";
                    }
                    hours=hours.substring(0, hours.length()-2);
                    String name = "";
                    if (weekday.checkSerminal()) {
                        name = sub.getName() + " - TL";
                    } else {
                        name = sub.getName();
                    }
                    String location = weekday.getLocation();
                    SubjectTimeTable subTimeTable=new SubjectTimeTable(hours, name, location);
                    list.add(subTimeTable);
                    break;
                }
            }

        }

        // Sort
        if (list.size() <3)
            return list;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                String str1 = list.get(i).getHours();
                int hours1 = Integer.parseInt(str1.substring(0, str1.indexOf(",")));
                String str2 = list.get(j).getHours();
                int hours2 = Integer.parseInt(str2.substring(0, str1.indexOf(",")));
                if (hours1 > hours2) {
                    SubjectTimeTable tmp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tmp);
                }
            }
        }
        return list;
    }
    public List<String> getAccountsList(){
        List<String> accountsList=new ArrayList<String>();
        Settings settings=ViewSetting.readSettings(context);
        for (String Name: settings.accounts.keySet()){
            accountsList.add(Name);
        }
        return accountsList;
    }
}

