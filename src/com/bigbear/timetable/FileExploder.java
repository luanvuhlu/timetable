package com.bigbear.timetable;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.bigbear.timetable.R;

/**
 * Created by luan on 03/08/2013.
 */
public class FileExploder extends ListActivity {
    private File curDir;
    private FileArrayAdapter adapter;
    File rootFile;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rootFile=Environment.getExternalStorageDirectory();
        curDir=rootFile;
        fill(curDir);
    }
    private void fill(File f){
        File[] dirs=f.listFiles();
        List<Item> dir=new ArrayList<Item>();
        List<Item> fls=new ArrayList<Item>();
        try{
            for (File ff:dirs){
                Date lastModDate=new Date(ff.lastModified());
                DateFormat formater=DateFormat.getDateInstance();
                String date_modify=formater.format(lastModDate);
                if (ff.isDirectory()){
                    File[] fbuf=ff.listFiles();
                    int buf=0;
                    if (fbuf!=null){
                        buf=fbuf.length;
                    }else
                        buf=0;
                    String num_item=String.valueOf(buf);
                    if (buf==0)
                        num_item+=" item";
                    else
                        num_item+=" items";
                    dir.add(new Item(ff.getName(), num_item, date_modify, ff.getAbsolutePath(), "folder_icon"));
                }
                else{
                    fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                }
            }
        }catch (Exception e){

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        StringTokenizer token=new StringTokenizer(curDir.getPath(), "/");
        token.nextToken();
        String sdCardName=token.nextToken();
        if (!f.getName().equalsIgnoreCase(sdCardName))
            dir.add(0, new Item("..", "Parent Directory", "", f.getParent(), "directory_up"));
        adapter=new FileArrayAdapter(FileExploder.this, R.layout.activity_filechooser, dir);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Item o=adapter.getItem(position);
        if (o.getImage().equalsIgnoreCase("folder_icon") || o.getImage().equalsIgnoreCase("directory_up")){
            curDir=new File(o.getPath());
            fill(curDir);
        }else
            onFileClick(o);
    }
    private void onFileClick(Item o){
        Intent  intent=new Intent();
        intent.putExtra("GetPath", curDir.toString());
        intent.putExtra("GetFileName", o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

}
class FileArrayAdapter extends ArrayAdapter<Item> {
    private Context c;
    private int id;
    private List<Item> items;

    public FileArrayAdapter(Context context, int textViewResourceId, List<Item> objects){
        super(context, textViewResourceId, objects);
        c=context;
        id=textViewResourceId;
        items=objects;
    }
    public Item getItem(int i){
        return items.get(i);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v=convertView;
        if (v==null){
            LayoutInflater vi=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(id, null);
        }
        final Item o=items.get(position);
        if(o !=null){
            TextView t1=(TextView) v.findViewById(R.id.FolderLabel);
            TextView t2=(TextView) v.findViewById(R.id.FolderNumber);
            TextView t3=(TextView) v.findViewById(R.id.FolderDate);

            ImageView imageCity=(ImageView) v.findViewById(R.id.imageView);
            String uri="drawable/"+o.getImage();
            int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
            Drawable image = c.getResources().getDrawable(imageResource);
            imageCity.setImageDrawable(image);
            if(t1!=null)
                t1.setText(o.getName());
            if(t2!=null)
                t2.setText(o.getData());
            if(t3!=null)
                t3.setText(o.getDate());

        }
        return v;

    }
}
class Item implements Comparable<Item>{
    private String name;
    private String data;
    private String date;
    private String path;
    private String image;
    public Item(String n, String d, String dt, String p, String i){
        name=n;
        data=d;
        date=dt;
        path=p;
        image=i;
    }
    public String getName()
    {
        return name;
    }
    public String getData()
    {
        return data;
    }
    public String getDate()
    {
        return date;
    }
    public String getPath()
    {
        return path;
    }
    public String getImage() {
        return image;
    }
    public int compareTo(Item o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}