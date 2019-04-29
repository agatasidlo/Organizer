package com.example.organizer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private DatabaseReference listDataBase;
    private final Activity context;
    private final ArrayList<String> taskArray;
    private final ArrayList<String> descriptionArray;
    private static ArrayList<String> statusArray;
    private final ArrayList<String> keyArray;


    //constructor
    public CustomListAdapter(Activity context, ArrayList<String> taskArrayParam, ArrayList<String> descpArrayParam, ArrayList<String> statusArrayParam,  ArrayList<String> keyArrayParam){

        super(context,R.layout.listview_row , taskArrayParam);

        this.context=context;
        this.taskArray = taskArrayParam;
        this.descriptionArray = descpArrayParam;
        this.statusArray = statusArrayParam;
        this.keyArray = keyArrayParam;
    }

    //populate data into each row
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        TextView nameTextField = (TextView) rowView.findViewById(R.id.rowTextView1);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.rowTextView2);
        CheckBox checkBoxField = (CheckBox) rowView.findViewById( R.id.CheckBoxId);

        nameTextField.setText(taskArray.get(position));
        infoTextField.setText(descriptionArray.get(position));

        listDataBase = FirebaseDatabase.getInstance().getReference().child("List");
        if(statusArray.get(position).equals("Not done")){
            checkBoxField.setChecked(false);
        }
        else{
            checkBoxField.setChecked(true);
        }
        checkBoxField.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    statusArray.set(position,"Done");
                    ListActivity.setStatusList(statusArray);
                    listDataBase.child(keyArray.get(position)).child("Status").getRef().setValue("Done");
                }
                else
                {
                    statusArray.set(position,"Not done");
                    ListActivity.setStatusList(statusArray);
                    listDataBase.child(keyArray.get(position)).child("Status").getRef().setValue("Not done");
                }
            }
        });
        return rowView;
    }

    public static ArrayList<String> getStatusArray(){
        return statusArray;
    }



}
