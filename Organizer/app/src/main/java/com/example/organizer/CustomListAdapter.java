package com.example.organizer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<String> taskArray;
    private final ArrayList<String> descriptionArray;


    //constructor
    public CustomListAdapter(Activity context, ArrayList<String> taskArrayParam, ArrayList<String> descpArrayParam){

        super(context,R.layout.listview_row , taskArrayParam);

        this.context=context;
        this.taskArray = taskArrayParam;
        this.descriptionArray = descpArrayParam;

    }

    //populate data into each row
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        TextView nameTextField = (TextView) rowView.findViewById(R.id.rowTextView1);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.rowTextView2);

        nameTextField.setText(taskArray.get(position));
        infoTextField.setText(descriptionArray.get(position));

        return rowView;

    };
}
