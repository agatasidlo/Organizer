package com.example.organizer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CustomCalendarAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<String> notesArray;
    private final ArrayList<String> daysArray;
    private static ArrayList<String> monthsArray;
    private final ArrayList<String> yearArray;

    public CustomCalendarAdapter(Activity context, ArrayList<String> notesArrayParam, ArrayList<String> daysArrayParam,
                                 ArrayList<String> monthsArrayParam, ArrayList<String> yearsArrayParam){
        super(context, R.layout.calendarlist_row, notesArrayParam);


        this.context = context;
        this.notesArray = notesArrayParam;
        this.daysArray = daysArrayParam;
        this.monthsArray = monthsArrayParam;
        this.yearArray = yearsArrayParam;

    }

    //populate data into each row
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        final View rowView=inflater.inflate(R.layout.calendarlist_row, null,true);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.rowTextView1);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.rowTextView2);
        nameTextField.setText(notesArray.get(position));
        String data = daysArray.get(position) +"/"+ monthsArray.get(position)+"/"+yearArray.get(position);
        infoTextField.setText(data);

        return rowView;
    }



}
