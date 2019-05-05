package com.example.organizer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomScheduleAdapter extends ArrayAdapter {

    private final Activity context;
    private ArrayList<String> subjectsArray;
    private ArrayList<String> descpArray;
    private ArrayList<String> fromArray;
    private ArrayList<String> toArray;

    public CustomScheduleAdapter(Activity context,
                                 ArrayList<String> subjectsArrayParam,
                                 ArrayList<String> descpArrayParam,
                                 ArrayList<String> fromArrayParam,
                                 ArrayList<String> toArrayParam) {

        super(context, R.layout.schedule_row, subjectsArrayParam);

        this.context = context;
        this.subjectsArray = subjectsArrayParam;
        this.descpArray = descpArrayParam;
        this.fromArray = fromArrayParam;
        this.toArray = toArrayParam;
    }



    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.schedule_row, null, true);

        TextView nameField = (TextView) rowView.findViewById(R.id.subjectNameID);
        TextView descpField = (TextView) rowView.findViewById(R.id.descriptionID);
        TextView fromField = (TextView) rowView.findViewById(R.id.scheduleFromID);
        TextView toField = (TextView) rowView.findViewById(R.id.scheduleToID);


        nameField.setText(subjectsArray.get(position));
        descpField.setText(descpArray.get(position));
        fromField.setText(fromArray.get(position));
        toField.setText(toArray.get(position));

        return rowView;
    }
}
