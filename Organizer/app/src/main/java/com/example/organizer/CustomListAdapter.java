package com.example.organizer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private DatabaseReference listDataBase;
    private final Activity context;
    private final ArrayList<String> taskArray;
    private final ArrayList<String> descriptionArray;
    private final ArrayList<String> statusArray;


    //constructor
    public CustomListAdapter(Activity context, ArrayList<String> taskArrayParam, ArrayList<String> descpArrayParam, ArrayList<String> statusArrayParam){

        super(context,R.layout.listview_row , taskArrayParam);

        this.context=context;
        this.taskArray = taskArrayParam;
        this.descriptionArray = descpArrayParam;
        this.statusArray = statusArrayParam;

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
        checkBoxField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(statusArray.get(position).equals("Not done")){
                    cb.setChecked(true);
                    statusArray.set(position, "Done");
                    //
                }
                else{
                    cb.setChecked(false);
                    statusArray.set(position, "Not done");
                }
            }
        });

        return rowView;
    }

    public ArrayList<String> getStatusArray(){
        return statusArray;
    }



}
