package com.example.organizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ScheduleSubjectListActivity extends AppCompatActivity {
    private TextView dayNameTextView;
    private ListView subjectsListView;
    private String dayName;
    private ArrayList<String> subjectArray;
    private ArrayList<String> fromArray;
    private ArrayList<String> toArray;
    private DatabaseReference scheduleDataBase;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_subject_list);
        //scheduleDataBase = FirebaseDatabase.getInstance().getReference().child("Schedule");

        dayNameTextView = ((TextView) findViewById(R.id.dayNameID));
        dayName = dayNameTextView.getText().toString().trim();
        dayNameTextView.setText("Monday");

        final CustomScheduleAdapter adapter = new CustomScheduleAdapter(this, subjectArray, fromArray, toArray);
        subjectsListView = (ListView) findViewById(R.id.viewList);
        subjectsListView.setAdapter(adapter);
    }
}
